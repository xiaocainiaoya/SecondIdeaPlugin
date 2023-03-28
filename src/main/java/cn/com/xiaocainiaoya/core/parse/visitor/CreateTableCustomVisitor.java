package cn.com.xiaocainiaoya.core.parse.visitor;

import cn.com.xiaocainiaoya.core.parse.visitor.vo.CreateTableVo;
import cn.com.xiaocainiaoya.util.PluginUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

import java.util.List;
import java.util.Map;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 16:27
 */
public class CreateTableCustomVisitor extends MySqlASTVisitorAdapter {

    CreateTableVo createTableVo;

    private String defaultDatabase;

    private static String TAB = "\t\t\t\t\t\t\t\t\t";

    private static String WRAP_AND_TAB = "\n" + TAB;

    private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS {} ({}{}) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT= {};";

    private static String PRIMARY_KEY = WRAP_AND_TAB + "PRIMARY KEY({}) USING {},";

    private static String OTHER_KEY = "KEY {} ({}) USING {} COMMENT {},";

    private static String OTHER_KEY_2 = "KEY {} ({}) USING {},";

    private static Map<String, String> COMMON_COLUMN_MAP = MapUtil
            .builder("`CREATE_USER_ID`", WRAP_AND_TAB + "`CREATE_USER_ID` varchar(50) DEFAULT NULL COMMENT '创建者id',")
            .put("`CREATE_USER_NAME`", WRAP_AND_TAB + "`CREATE_USER_NAME` varchar(100) NOT NULL COMMENT '创建者名称',")
            .put("`UPDATE_USER_ID`", WRAP_AND_TAB + "`UPDATE_USER_ID` varchar(50) DEFAULT NULL COMMENT '更新者id',")
            .put("`UPDATE_USER_NAME`", WRAP_AND_TAB + "`UPDATE_USER_NAME` varchar(100) DEFAULT NULL COMMENT '更新者名称',")
            .put("`CREATE_TIME`", WRAP_AND_TAB + "`CREATE_TIME` datetime NOT NULL COMMENT '创建时间',")
            .put("`UPDATE_TIME`", WRAP_AND_TAB + "`UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',")
            .put("`ZONE_NAME`", WRAP_AND_TAB + "`ZONE_NAME` varchar(100) NOT NULL COMMENT '区划名称',")
            .put("`ZONE_CODE`", WRAP_AND_TAB + "`ZONE_CODE` varchar(30) NOT NULL COMMENT '区划',")
            .build();

    public CreateTableCustomVisitor(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    @Override
    public boolean visit(MySqlCreateTableStatement x) {
        String fullTableName = x.getTableSource().toString();

        // 获取每一列
        List<SQLTableElement> tableElements = x.getTableElementList();
        StringBuilder columnAndKeyBuilder = new StringBuilder();
        String primaryKey = "";
        String otherKey = "";

        Map<String, String> commonColumnMap = BeanUtil.copyProperties(COMMON_COLUMN_MAP, Map.class);

        for(SQLTableElement element : tableElements){
            if(element instanceof SQLColumnDefinition){
                if(columnAndKeyBuilder.length() > 0){
                    columnAndKeyBuilder.append(",");
                }
                columnAndKeyBuilder.append(WRAP_AND_TAB).append(element.toString());
                String name = ((SQLColumnDefinition) element).getName().toString().toUpperCase();
                if(!name.startsWith("`")){
                    name = "`" + name + "`";
                }
                if(commonColumnMap.containsKey(name)){
                    commonColumnMap.remove(name);
                }
            }
            // 提取主键索引
            if(element instanceof MySqlPrimaryKey){
                //primaryKey.append(element.toString()).append(",");
                String key = "";
                MySqlPrimaryKey mySqlPrimaryKey = (MySqlPrimaryKey) element;
                for(SQLSelectOrderByItem item : mySqlPrimaryKey.getColumns()){
                    key = key + item.getExpr().toString() + ",";
                }
                // 移除尾部的逗号
                if(key.lastIndexOf(",") == key.length() - 1){
                    key = key.substring(0, key.length() - 1);
                }
                primaryKey = StrUtil.format(PRIMARY_KEY, key, mySqlPrimaryKey.getIndexType());
                continue;
            }
            // 提取其他索引
            if(element instanceof MySqlKey){
                String key = "";
                MySqlKey mySqlKey = (MySqlKey) element;
                for(SQLSelectOrderByItem item : mySqlKey.getColumns()){
                    key = key + item.getExpr().toString() + ",";
                }
                // 移除尾部的逗号
                if(key.lastIndexOf(",") == key.length() - 1){
                    key = key.substring(0, key.length() - 1);
                }
                otherKey = spliceOtherKey(otherKey, mySqlKey, key);
            }
        }
        // 补充通用字段
        if(columnAndKeyBuilder.length() > 0){
            // 在补充通用字段之前需要先添加一个逗号
            columnAndKeyBuilder.append(",");
        }
        for(String value : commonColumnMap.values()){
            columnAndKeyBuilder.append(value);
        }

        String columnAndKey = columnAndKeyBuilder.toString();
        columnAndKey = columnAndKey + primaryKey + otherKey;
        if(columnAndKey.lastIndexOf(",") == columnAndKey.length() - 1){
            columnAndKey = columnAndKey.substring(0, columnAndKey.length() - 1);
        }

        String database = StrUtil.EMPTY;
        String tableName = StrUtil.EMPTY;
        String renderFullTableName = StrUtil.EMPTY;

        try{
            // 有的时候创建表的是语句里可能没有写数据库名
            if(fullTableName.contains(".")){
                renderFullTableName = fullTableName;
                String[] split = fullTableName.split("\\.");
                database = split[0];
                tableName = split[1];
            }else{
                renderFullTableName = PluginUtil.addReverseQuotes(defaultDatabase) + "." + PluginUtil.addReverseQuotes(fullTableName);
                tableName = fullTableName;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        createTableVo = CreateTableVo.builder()
                .sql(StrUtil.format(CREATE_TABLE, renderFullTableName, columnAndKey, WRAP_AND_TAB, x.getComment()))
                .fullTableName(PluginUtil.removeReverseQuotes(fullTableName))
                .tableName(PluginUtil.removeReverseQuotes(tableName))
                .database(PluginUtil.removeReverseQuotes(database))
                .build();

        return true;
    }

    private String spliceOtherKey(String otherKey, MySqlKey mySqlKey, String key) {
        if(ObjectUtil.isEmpty(mySqlKey.getComment())){
            return otherKey + WRAP_AND_TAB + StrUtil.format(OTHER_KEY_2, mySqlKey.getName(), key, mySqlKey.getIndexType());
        }
        return otherKey + WRAP_AND_TAB + StrUtil.format(OTHER_KEY, mySqlKey.getName(), key, mySqlKey.getIndexType(), mySqlKey.getComment());
    }

    public CreateTableVo getCreateTableVo() {
        return createTableVo;
    }

}
