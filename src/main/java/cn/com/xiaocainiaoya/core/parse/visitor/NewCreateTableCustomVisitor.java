package cn.com.xiaocainiaoya.core.parse.visitor;

import cn.com.xiaocainiaoya.core.parse.visitor.vo.NewCreateTableVo;
import cn.com.xiaocainiaoya.core.parse.visitor.vo.TableKey;
import cn.com.xiaocainiaoya.enums.ErrorEnum;
import cn.com.xiaocainiaoya.util.PluginUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 16:27
 */
public class NewCreateTableCustomVisitor extends MySqlASTVisitorAdapter {

    NewCreateTableVo createTableVo;

    private String defaultDatabase;

    private static Map<String, String> COMMON_COLUMN_MAP = MapUtil
            .builder("`CREATE_USER_ID`", "`CREATE_USER_ID` varchar(50) DEFAULT NULL COMMENT '创建者id',")
            .put("`CREATE_USER_NAME`", "`CREATE_USER_NAME` varchar(100) NOT NULL COMMENT '创建者名称',")
            .put("`UPDATE_USER_ID`", "`UPDATE_USER_ID` varchar(50) DEFAULT NULL COMMENT '更新者id',")
            .put("`UPDATE_USER_NAME`", "`UPDATE_USER_NAME` varchar(100) DEFAULT NULL COMMENT '更新者名称',")
            .put("`CREATE_TIME`", "`CREATE_TIME` datetime NOT NULL COMMENT '创建时间',")
            .put("`UPDATE_TIME`", "`UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',")
            .put("`ZONE_NAME`", "`ZONE_NAME` varchar(100) NOT NULL COMMENT '区划名称',")
            .put("`ZONE_CODE`", "`ZONE_CODE` varchar(30) NOT NULL COMMENT '区划',")
            .build();

    public NewCreateTableCustomVisitor(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    @Override
    public boolean visit(MySqlCreateTableStatement x) {
        String fullTableName = x.getTableSource().toString();
        // 获取每一列
        List<SQLTableElement> tableElements = x.getTableElementList();
        Map<String, String> commonColumnMap = BeanUtil.copyProperties(COMMON_COLUMN_MAP, Map.class);

        List<String> fields = Lists.newArrayList();
        List<TableKey> primaryKeys = Lists.newArrayList();
        List<TableKey> otherKeys = Lists.newArrayList();
        for(SQLTableElement element : tableElements){
            // 如果是字段行
            if(element instanceof SQLColumnDefinition){
                String name = ((SQLColumnDefinition) element).getName().toString().toUpperCase();
                if(!name.startsWith("`")){
                    name = "`" + name + "`";
                }
                // 如果脚本中已经包含这个字段, 则移除
                if(commonColumnMap.containsKey(name)){
                    commonColumnMap.remove(name);
                }
                fields.add(element.toString() + ",");
            }
            // 提取主键索引
            if(element instanceof MySqlPrimaryKey){
                MySqlPrimaryKey mySqlPrimaryKey = (MySqlPrimaryKey) element;
                TableKey tableKey = TableKey.builder()
                        .KeyField(assemblePrimaryKey(mySqlPrimaryKey.getColumns()))
                        .build();
                primaryKeys.add(tableKey);
                continue;
            }
            // 提取其他索引
            if(element instanceof MySqlKey){
                MySqlKey mySqlKey = (MySqlKey) element;
                otherKeys.add(TableKey.builder()
                        .keyName(mySqlKey.getName().toString())
                        .KeyField(assemblePrimaryKey(mySqlKey.getColumns()))
                        .build());
            }
        }
        // 补充通用字段
        for(String value : commonColumnMap.values()){
            fields.add(value);
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
        Assert.notNull(x.getComment(), ErrorEnum.COMMENT_IS_EMPTY.getValue());
        createTableVo = NewCreateTableVo.builder()
                .renderFullTableName(renderFullTableName)
                .fullTableName(PluginUtil.removeReverseQuotes(fullTableName))
                .tableName(PluginUtil.removeReverseQuotes(tableName))
                .database(PluginUtil.removeReverseQuotes(database))
                .primaryKeys(primaryKeys)
                .otherKeys(CollectionUtil.isEmpty(otherKeys) ? null : otherKeys)
                .fields(fields)
                .tableComment(x.getComment().toString())
                .build();


        return true;
    }

    private String assemblePrimaryKey(List<SQLSelectOrderByItem> columns) {
        String key = StrUtil.EMPTY;
        for(SQLSelectOrderByItem item : columns){
            key = key + item.getExpr().toString() + ",";
        }
        // 移除尾部的逗号
        if(key.lastIndexOf(",") == key.length() - 1){
            key = key.substring(0, key.length() - 1);
        }
        return key;
    }

    public NewCreateTableVo getCreateTableVo() {
        return createTableVo;
    }

}
