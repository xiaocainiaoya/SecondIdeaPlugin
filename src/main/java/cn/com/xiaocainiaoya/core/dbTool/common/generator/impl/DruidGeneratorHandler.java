package cn.com.xiaocainiaoya.core.dbTool.common.generator.impl;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorBuilder;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.HandleVo;
import cn.com.xiaocainiaoya.core.parse.ParseHelper;
import cn.com.xiaocainiaoya.core.parse.visitor.vo.FieldVisitorVo;
import cn.com.xiaocainiaoya.core.parse.visitor.vo.IndexVisitorVo;
import cn.com.xiaocainiaoya.core.parse.visitor.vo.InsertDataVisitorVo;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 15:48
 */
public class DruidGeneratorHandler extends AbstractGeneratorHandler {


//    @Override
//    public String insertField(DataConfig config) {
//        return null;
//    }

    private static String DEL_SQL_TEMPLATE = "DELETE FROM {} WHERE id IN ({});";

    public DruidGeneratorHandler(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
    }

    @Override
    public String insertBatchData(DataConfig config) {
        String[] sqls = config.getSql().replace("\n", "").split("\\);");

        List<String> resultSqlList = new ArrayList<>();

        for(String sql : sqls){
            if("\n".equals(sql)){
                continue;
            }

            if(sql.startsWith("\n")){
                sql = sql.substring(1);
            }

            if(!sql.endsWith(")") && StrUtil.isNotBlank(sql)){
                sql = sql + ")";
            }

            resultSqlList.add(sql);
        }

        List<InsertDataVisitorVo> insertDataVisitorVos = new ArrayList<>();

        for(String sql : resultSqlList){
            InsertDataVisitorVo insertDataVisitorVo = ParseHelper.getInsertInfo(sql);
            insertDataVisitorVos.add(insertDataVisitorVo);
        }

        String delSql = StrUtil.format(DEL_SQL_TEMPLATE, insertDataVisitorVos.get(0).getFullTableName(), insertDataVisitorVos.stream().map(InsertDataVisitorVo::getId).collect(Collectors.joining(",")));


        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, resultSqlList);
        dataMap.put(FILE_DESC, config.getFileName());
        dataMap.put(DEL_SQL, delSql);
        return generatorBuilder.render("insertBatchData.vm", dataMap, config);
    }

    @Override
    public String insertFieldBatch(DataConfig config) {
        String[] sqls = config.getSql().split(";");
        List<HandleVo> handleList = ListUtil.list(false);

        for(int i = 0; i < sqls.length; i++){
            List<FieldVisitorVo> fieldVisitors = ParseHelper.getField(sqls[i]);

            for(FieldVisitorVo fieldVisitor : fieldVisitors){
                 HandleVo handleVo =  HandleVo.builder()
                    .sql(fieldVisitor.getFieldSql())
                    .columnName(fieldVisitor.getFieldName().toString().replace("`", ""))
                    .tableName(fieldVisitor.getTableName().toString().replace("`", ""))
                    .build();
            handleList.add(handleVo);
            }
        }
        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        //dataMap.put(TABLE_NAME, tableName);
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("insertFieldBatch.vm", dataMap, config);
    }

    @Override
    public String updateFieldLength(DataConfig config) {
        String[] sqls = config.getSql().split(";");
        List<HandleVo> handleList = ListUtil.list(false);
        for(int i = 0; i < sqls.length; i++){
            List<FieldVisitorVo> fieldVisitors = ParseHelper.getAlterField(sqls[i]);

            for(FieldVisitorVo fieldVisitor : fieldVisitors){
                HandleVo handleVo =  HandleVo.builder()
                        .sql(fieldVisitor.getFieldSql())
                        .columnName(fieldVisitor.getFieldName().toString().replace("`", ""))
                        .tableName(fieldVisitor.getTableName().toString().replace("`", ""))
                        .build();
                handleList.add(handleVo);
            }
        }
        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, handleList.get(0).getTableName());
        dataMap.put(FILE_DESC, config.getFileName());

        return generatorBuilder.render("updateFieldLength.vm", dataMap, config);
    }

    @Override
    public String insertIndex(DataConfig config) {
        String[] sqls = config.getSql().split(";");
        List<HandleVo> handleList = ListUtil.list(false);

        for(int i = 0; i < sqls.length; i++){
            List<IndexVisitorVo> indexVisitors = ParseHelper.getIndex(sqls[i]);

            for(IndexVisitorVo indexVisitor : indexVisitors){
                HandleVo handleVo =  HandleVo.builder()
                        .sql(indexVisitor.getIndexSql())
                        .columnName(indexVisitor.getIndexName().toString().replace("`", ""))
                        .tableName(indexVisitor.getTableName().toString().replace("`", ""))
                        .build();
                handleList.add(handleVo);
            }
        }

        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, handleList.get(0).getTableName());
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("insertIndex.vm", dataMap, config);
    }

    @Override
    public String updateIndex(DataConfig config) {
        String[] sqls = config.getSql().split(";");
        List<HandleVo> handleList = ListUtil.list(false);

        for(int i = 0; i < sqls.length; i++){
            List<IndexVisitorVo> indexVisitors = ParseHelper.getIndex(sqls[i]);

            for(IndexVisitorVo indexVisitor : indexVisitors){
                HandleVo handleVo =  HandleVo.builder()
                        .sql(indexVisitor.getIndexSql())
                        .columnName(indexVisitor.getIndexName().toString().replace("`", ""))
                        .tableName(indexVisitor.getTableName().toString().replace("`", ""))
                        .build();
                handleList.add(handleVo);
            }
        }

        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, handleList.get(0).getTableName());
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("updateIndex.vm", dataMap, config);
    }
}
