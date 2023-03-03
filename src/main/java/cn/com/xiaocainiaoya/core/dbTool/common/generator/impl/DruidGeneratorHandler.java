package cn.com.xiaocainiaoya.core.dbTool.common.generator.impl;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorBuilder;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.HandleVo;
import cn.com.xiaocainiaoya.core.parse.ParseHelper;
import cn.com.xiaocainiaoya.core.parse.visitor.vo.CreateTableVo;
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
            // 最后一行为换行符的情况
            if("\n".equals(sql)){
                continue;
            }
            // 每一条sql开头为换行符的情况
            if(sql.startsWith("\n")){
                sql = sql.substring(1);
            }
            // 每一条sql的结尾不是)的情况, 补充)
            if(!sql.endsWith(")") && StrUtil.isNotBlank(sql)){
                sql = sql + ")";
            }
            resultSqlList.add(sql);
        }
        // 将表名和主键提取出来
        List<InsertDataVisitorVo> insertDataVisitorVos = new ArrayList<>();
        for(String sql : resultSqlList){
            InsertDataVisitorVo insertDataVisitorVo = ParseHelper.getInsertInfo(sql);
            insertDataVisitorVos.add(insertDataVisitorVo);
        }

        // 根据表名进行分组
        Map<String, List<InsertDataVisitorVo>> delMap = insertDataVisitorVos.stream().collect(Collectors.groupingBy(InsertDataVisitorVo::getFullTableName));
        // 根据表名拼接根据ID的删除语句
        List<String> delSqls = new ArrayList<>();
        for(String fullTableName : delMap.keySet()){
            List<InsertDataVisitorVo> insertDataVisitorVoList = delMap.get(fullTableName);
            // 若id过多, 防止出现in('ID')中内容过长, 按每条删除语句20个id进行拆分
            if(insertDataVisitorVoList.size() > 20){
                int count = insertDataVisitorVoList.size() / 20;
                for(int i = 0; i < count; i++){
                    List<InsertDataVisitorVo> subList = insertDataVisitorVoList.subList(i * 20, (i + 1) * 20);
                    String delSql = StrUtil.format(DEL_SQL_TEMPLATE, fullTableName, subList.stream().map(InsertDataVisitorVo::getId).collect(Collectors.joining(",")));
                    delSqls.add(delSql);
                }
                // 若有剩余的id, 则单独拼接一条删除语句
                if(insertDataVisitorVoList.size() % 20 != 0){
                    List<InsertDataVisitorVo> subList = insertDataVisitorVoList.subList(count * 20, insertDataVisitorVoList.size());
                    String delSql = StrUtil.format(DEL_SQL_TEMPLATE, fullTableName, subList.stream().map(InsertDataVisitorVo::getId).collect(Collectors.joining(",")));
                    delSqls.add(delSql);
                }
            }else{
                delSqls.add(StrUtil.format(DEL_SQL_TEMPLATE, fullTableName, insertDataVisitorVoList.stream().map(InsertDataVisitorVo::getId).collect(Collectors.joining(","))));
            }
        }

        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, resultSqlList);
        dataMap.put(FILE_DESC, config.getFileName());
        dataMap.put(DEL_SQL, delSqls);
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
    public String createTable(DataConfig config) {
        CreateTableVo createTableVo = ParseHelper.createTable(config.getSql());

        Map<String, Object> dataMap = MapUtil.of(SQL, createTableVo.getSql());
        //dataMap.put(TABLE_NAME, handleList.get(0).getTableName());
        dataMap.put(FILE_DESC, config.getFileName());
        dataMap.put(DATA_SCHEMA, createTableVo.getDatabase());
        dataMap.put(TABLE_NAME, createTableVo.getTableName());
        return generatorBuilder.render("buildTable.vm", dataMap, config);
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
