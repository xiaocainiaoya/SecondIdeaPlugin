package cn.com.xiaocainiaoya.core.dbTool.common.generator.impl;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorBuilder;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorHandler;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 15:49
 */
public abstract class AbstractGeneratorHandler implements GeneratorHandler {

    protected GeneratorBuilder generatorBuilder;

    protected static String SQL = "sql";

    protected static String SQL_LIST = "sqlList";

    protected static String FILE_DESC = "fileDesc";

    protected static String DATA_SCHEMA = "database";

    protected static String DEL_SQL = "delSqlList";

    protected static String TABLE_NAME = "tableName";

    protected static String COLUMN_NAME = "columnName";

    @Override
    public String insertData(DataConfig config) {
        return null;
    }

    @Override
    public String insertBatchData(DataConfig config) {
        return null;
    }

//    @Override
//    public String insertField(DataConfig config) {
//        return null;
//    }

    @Override
    public String insertFieldBatch(DataConfig config) {
        return null;
    }

    @Override
    public String createTable(DataConfig config) {
        return null;
    }

    @Override
    public String updateFieldLength(DataConfig config) {
        return null;
    }

    @Override
    public String insertIndex(DataConfig config) {
        return null;
    }

    @Override
    public String updateIndex(DataConfig config) {
        return null;
    }

    @Override
    public String multilineSql(DataConfig config) {
        System.out.println("多行SQL：(输入完成后请点击回车键)");
        List<String> inputs = GeneratorBuilder.scannerMoreLine(config);

        StringBuilder sb = new StringBuilder();
        for(String input : inputs){
            sb.append(input).append("\n");
        }
        Map<String, Object> dataMap = MapUtil.of(SQL, sb.toString());
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("multilineSql.vm", dataMap, config);
    }

    @Override
    public String other(DataConfig config) {
        Map<String, Object> dataMap = MapUtil.of(SQL, StrUtil.EMPTY);
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("insertData.vm", dataMap, config);
    }
}
