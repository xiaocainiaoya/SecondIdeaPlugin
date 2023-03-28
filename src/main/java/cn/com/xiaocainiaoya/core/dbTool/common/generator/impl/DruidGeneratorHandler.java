package cn.com.xiaocainiaoya.core.dbTool.common.generator.impl;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorBuilder;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DeleteFieldHandleVo;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.HandleVo;
import cn.com.xiaocainiaoya.core.parse.ParseHelper;
import cn.com.xiaocainiaoya.core.parse.visitor.vo.*;
import cn.com.xiaocainiaoya.util.PluginUtil;
import cn.hutool.core.bean.BeanUtil;
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
            if(!PluginUtil.invalidSql(sql)){
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
            String sql = sqls[i];
            System.out.println("当前处理的sql为: " + sql);
            if(!PluginUtil.invalidSql(sql)){
                continue;
            }
            List<FieldVisitorVo> fieldVisitors = ParseHelper.getField(sql);

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
        //CreateTableVo createTableVo = ParseHelper.createTable(config.getSql(), config.getDatabase());
        NewCreateTableVo createTableVo = ParseHelper.newCreateTable(config.getSql(), config.getDatabase());
        // 如果建表语句中没有写数据库名，则中config中获取数据库名
        if(StrUtil.isEmpty(createTableVo.getDatabase())){
            createTableVo.setDatabase(config.getDatabase());
        }

        Map<String, Object> dataMap = BeanUtil.beanToMap(createTableVo);
        //dataMap.put(TABLE_NAME, handleList.get(0).getTableName());
        dataMap.put(FILE_DESC, config.getFileName());
        dataMap.put(DATA_SCHEMA, createTableVo.getDatabase());
        dataMap.put(TABLE_NAME, createTableVo.getTableName());
        return generatorBuilder.render("newBuildTable.vm", dataMap, config);
    }

    @Override
    public String updateFieldLength(DataConfig config) {
        String[] sqls = config.getSql().split(";");
        List<HandleVo> handleList = ListUtil.list(false);
        for(int i = 0; i < sqls.length; i++){
            if(!PluginUtil.invalidSql(sqls[i])){
                continue;
            }
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
    public String deleteField(DataConfig config) {
        String[] sqls = config.getSql().split(";");
        List<DeleteFieldHandleVo> handleList = ListUtil.list(false);
        for(int i = 0; i < sqls.length; i++){
            if(!PluginUtil.invalidSql(sqls[i])){
                continue;
            }
            List<DeleteFieldVo> fieldVisitors = ParseHelper.getDeleteFiled(sqls[i]);
            for(DeleteFieldVo deleteFieldVo : fieldVisitors){
                if(!deleteFieldVo.getFullTableName().contains(".")){
                    deleteFieldVo.setFullTableName(config.getDatabase() + "." + deleteFieldVo.getFullTableName());
                }

                DeleteFieldHandleVo handleVo =  DeleteFieldHandleVo.builder()
                        .field(deleteFieldVo.getField())
                        .fullTableName(deleteFieldVo.getFullTableName())
                        .tableName(deleteFieldVo.getTableName().toString().replace("`", ""))
                        .columnName(deleteFieldVo.getField().toString().replace("`", ""))
                        .build();
                handleList.add(handleVo);
            }
        }
        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, handleList.get(0).getTableName());
        dataMap.put(FILE_DESC, config.getFileName());

        return generatorBuilder.render("deleteField.vm", dataMap, config);
    }

    @Override
    public String insertIndex(DataConfig config) {
        String[] sqls = config.getSql().split(";");
        List<HandleVo> handleList = ListUtil.list(false);

        for(int i = 0; i < sqls.length; i++){
            if(!PluginUtil.invalidSql(sqls[i])){
                continue;
            }
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
            if(!PluginUtil.invalidSql(sqls[i])){
                continue;
            }
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
