package cn.com.xiaocainiaoya.core.dbTool.common.generator.impl;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorBuilder;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.HandleVo;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author :jiangjiamin
 * @date : 2022/10/20 09:32
 */
public class SplitGeneratorHandler extends AbstractGeneratorHandler{

    /**
     * 插入单条数据
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:00
     * @param
     * @return: void
     */
    @Override
    public String insertData(DataConfig config){
        System.out.println("输入插入(修改)的数据SQL：");
        //String input = GeneratorUtils.scannerOneLine();

        Map<String, Object> dataMap = MapUtil.of(SQL, config.getSql());
        dataMap.put(FILE_DESC, config.getFileName());
        dataMap.put(DEL_SQL, generatorDelSql(Lists.newArrayList(config.getSql())));

        return generatorBuilder.render("insertData.vm", dataMap, config);
    }

    private String generatorDelSql(List<String> sqlList){
        // 默认生成删除语句
        try{
            // 割删除语句前半句
            String insertPreSql = sqlList.get(0).split("\\(")[0];
            String delSql = insertPreSql.toUpperCase().replace("INSERT INTO", "DELETE FROM");
            delSql = delSql + " WHERE id in ( {} );";

            // 捞id值
            StringBuilder sqlBuilder = new StringBuilder();
            for(int i = 0; i < sqlList.size(); i++){
                String id = sqlList.get(i).split("\\(")[2].split("\\,")[0];
                sqlBuilder.append(id);

                if(i != sqlList.size() - 1){
                    sqlBuilder.append(",");
                }
            }

            delSql = StrUtil.format(delSql, sqlBuilder.toString());
            System.out.println("生成删除语句：" + delSql);
            return delSql;
        }catch (Exception e){
            System.out.println("生成删除语句失败：" + e.getMessage());
            e.printStackTrace();
            return StrUtil.EMPTY;
        }


    }



    /**
     * 插入多条数据
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    @Override
    public String insertBatchData(DataConfig config){
        System.out.println("输入批量插入的数据SQL：");
        System.out.println("样例：(输入完成后请点击回车键)");
        System.out.println("INSERT INTO `gp-gpfa`.`gpfa_process_config` (`ID`, `GROUP_BIZ_TYPE_CODE`,`GROUP_BIZ_TYPE_NAME`, `GROUP_SEQ`, `BIZ_TYPE_CODE`, `BIZ_TYPE_NAME`, `BIZ_TYPE_SEQ`,`P_BIZ_TYPE_CODE`, `P_BIZ_TYPE_NAME`, `MATH_CONDITION`, `MANAGE_USER_TYPE`, `BUSINESS_PROCESS_NAME`,`BUSINESS_PROCESS_STATUS_VALUE`, `BUSINESS_PROCESS_STATUS_NAME`, `POST_CODE`, `PROCESS_FORM`,`WORKFLOW_KEY`, `NODE_TYPE_CODE`, `ENABLE`, `REMARK`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, `UPDATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`) VALUES ('ff8080817fe529f9017fe51aed8c0033', 'levyFirstProcess', '发布入围结果公告', 200, 'noticeConfirmPublishAuditFlow', '发布入围结果公告', 1, NULL, NULL, '-', NULL, '完结', '899', '如果结果公告已发布', NULL, NULL, NULL, 2, '1', NULL, '111113', '代理机构用户', '2022-04-01 20:30:22', '2022-04-01 20:30:22', '111113', '代理机构用户');");
        System.out.println("INSERT INTO `gp-gpfa`.`gpfa_process_config` (`ID`, `GROUP_BIZ_TYPE_CODE`,`GROUP_BIZ_TYPE_NAME`, `GROUP_SEQ`, `BIZ_TYPE_CODE`, `BIZ_TYPE_NAME`, `BIZ_TYPE_SEQ`,`P_BIZ_TYPE_CODE`, `P_BIZ_TYPE_NAME`, `MATH_CONDITION`, `MANAGE_USER_TYPE`, `BUSINESS_PROCESS_NAME`,`BUSINESS_PROCESS_STATUS_VALUE`, `BUSINESS_PROCESS_STATUS_NAME`, `POST_CODE`, `PROCESS_FORM`,`WORKFLOW_KEY`, `NODE_TYPE_CODE`, `ENABLE`, `REMARK`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, `UPDATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`) VALUES ('ff8080817fe529f9017fe51aed8c0033', 'levyFirstProcess', '发布入围结果公告', 200, 'noticeConfirmPublishAuditFlow', '发布入围结果公告', 1, NULL, NULL, '-', NULL, '完结', '899', '如果结果公告已发布', NULL, NULL, NULL, 2, '1', NULL, '111113', '代理机构用户', '2022-04-01 20:30:22', '2022-04-01 20:30:22', '111113', '代理机构用户');");
        System.out.println("INSERT INTO `gp-gpfa`.`gpfa_process_config` (`ID`, `GROUP_BIZ_TYPE_CODE`,`GROUP_BIZ_TYPE_NAME`, `GROUP_SEQ`, `BIZ_TYPE_CODE`, `BIZ_TYPE_NAME`, `BIZ_TYPE_SEQ`,`P_BIZ_TYPE_CODE`, `P_BIZ_TYPE_NAME`, `MATH_CONDITION`, `MANAGE_USER_TYPE`, `BUSINESS_PROCESS_NAME`,`BUSINESS_PROCESS_STATUS_VALUE`, `BUSINESS_PROCESS_STATUS_NAME`, `POST_CODE`, `PROCESS_FORM`,`WORKFLOW_KEY`, `NODE_TYPE_CODE`, `ENABLE`, `REMARK`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, `UPDATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`) VALUES ('ff8080817fe529f9017fe51aed8c0033', 'levyFirstProcess', '发布入围结果公告', 200, 'noticeConfirmPublishAuditFlow', '发布入围结果公告', 1, NULL, NULL, '-', NULL, '完结', '899', '如果结果公告已发布', NULL, NULL, NULL, 2, '1', NULL, '111113', '代理机构用户', '2022-04-01 20:30:22', '2022-04-01 20:30:22', '111113', '代理机构用户');");

        //List<String> sqls = GeneratorBuilder.scannerMoreLine();
        List<String> sqls = StrUtil.split(config.getSql(), '\n');

        List<String> resultSqlList = new ArrayList<>();
        for(String sql : sqls){
            // 不能确保SQL内容中没有分号
            // sql.replaceAll(";", "");
            int index = sql.lastIndexOf(");");
            if(index == -1){
                resultSqlList.add(sql.trim());
                continue;
            }
            String sqlStr = sql.substring(0, sql.length() - 1);
            resultSqlList.add(sqlStr);
        }

        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, resultSqlList);
        dataMap.put(FILE_DESC, config.getFileName());
        dataMap.put(DEL_SQL, generatorDelSql(resultSqlList));
        return generatorBuilder.render("insertBatchData.vm", dataMap, config);
    }

    /**
     * 插入字段
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    public String insertField(DataConfig config){
        System.out.println("插入字段SQL：(请使用Navicat Premium 标准SQL)");
        System.out.println("示例: ");
        System.out.println("ALTER TABLE `gp-gpfa`.`gpfa_demand_cancel`");
        System.out.println("ADD COLUMN `upda` varchar(255) NULL COMMENT '1' AFTER `UPDATE_USER_NAME`,");
        System.out.println("ADD COLUMN `cc` varchar(255) NULL COMMENT '2' AFTER `upda`;");

        List<String> sqls = GeneratorBuilder.scannerMoreLine(config);

        String tableName = changeQuotes(sqls.get(0).split("\\.")[1]);
        List<HandleVo> handleList = ListUtil.list(false);
        String mainSql = sqls.get(0);
        for(int i = 1; i < sqls.size(); i++){
            String columnName = sqls.get(i).split(" ")[2];
            String subSql = cleanSymbol(sqls.get(i));
            HandleVo handleVo =  HandleVo.builder()
                    .sql(mainSql + subSql)
                    .columnName(changeQuotes(columnName))
                    .build();
            handleList.add(handleVo);
        }

        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, tableName);
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("insertField.vm", dataMap, config);
    }

    /**
     * 批量插入字段，可能不是同一个表
     *
     * @author jiangjiamin
     * @date 2022/7/22 16:51:55
     * @param config
     * @return: java.lang.String
     */
    @Override
    public String insertFieldBatch(DataConfig config){
        System.out.println("批量插入字段SQL，语句之间请不要存在换行！：(请使用Navicat Premium 标准SQL)");
        System.out.println("如下示例SQL，支持部分语句只有一个ADD子句，部分语句多个ADD子句混用，但语句之间请不要存在换行！");
        System.out.println("判断逻辑为以分号结尾则表示是对同一个表的操作，会从这个语句中读取填充模板相关数据");
        System.out.println("示例: ");
        System.out.println("ALTER TABLE `gp-gpfa`.`gpfa_demand_cancel`");
        System.out.println("ADD COLUMN `upda` varchar(255) NULL COMMENT '1' AFTER `UPDATE_USER_NAME`,");
        System.out.println("ADD COLUMN `cc` varchar(255) NULL COMMENT '2' AFTER `upda`;");
        System.out.println("ALTER TABLE `gp-gpfa`.`gpfa_demand_cancel_2`");
        System.out.println("ADD COLUMN `upda` varchar(255) NULL COMMENT '1' AFTER `UPDATE_USER_NAME`;");

        List<String> sqls = GeneratorBuilder.scannerMoreLine(config);

        //String tableName = changeQuotes(sqls.get(0).split("\\.")[1]);
        List<HandleVo> handleList = ListUtil.list(false);
        //String mainSql = sqls.get(0);
        int cursor = 0;
        for(int i = 0; i < sqls.size(); i++){
            // 以分号判断语句的结束
            if(!sqls.get(i).contains(";")){
                continue;
            }
            for(int j = cursor + 1; j <= i; j++){
                String columnName = sqls.get(j).split(" ")[2];
                String subSql = cleanSymbol(sqls.get(j));
                HandleVo handleVo =  HandleVo.builder()
                        .sql(sqls.get(cursor) + subSql)
                        .columnName(changeQuotes(columnName))
                        .tableName(changeQuotes(sqls.get(cursor).split("\\.")[1]))
                        .build();
                handleList.add(handleVo);
            }
            cursor = i + 1;
        }

        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        //dataMap.put(TABLE_NAME, tableName);
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("insertFieldBatch.vm", dataMap, config);
    }

    /**
     * 清除反引号
     *
     * @author jiangjiamin
     * @date 2022/5/7 16:45:10
     * @param str
     * @return: java.lang.String
     */
    public static String changeQuotes(String str){
        return str.trim().replace("`", "");
    }

    /**
     * 清除逗号和分号
     *
     * @author jiangjiamin
     * @date 2022/5/7 16:45:10
     * @param str
     * @return: java.lang.String
     */
    public static String cleanSymbol(String str){
        return str.replace(",", "").replace(";", "");
    }

    static List<String> LAST_SYMBOL = ListUtil.list(false, ";", ",");

    public static String cleanLastSymbol(String str){
        str = str.trim();
        String symbol = str.substring(str.length() - 1, str.length());
        if(!LAST_SYMBOL.contains(symbol)){
            return str;
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 修改字段长度、类型等
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    @Override
    public String updateFieldLength(DataConfig config){
        System.out.println("修改字段长度、类型等SQL：");
        System.out.println("示例: ");
        System.out.println("ALTER TABLE `gp-gpfa`.`gpfa_demand_catalog_config` ");
        System.out.println("MODIFY COLUMN `CATALOG_NAME` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录名称，如公告标题等 ' AFTER `CATALOG_CODE`,");
        System.out.println("MODIFY COLUMN `CATALOG_REF` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录关系，包、项目等' AFTER `CATALOG_NAME`;");

        List<String> sqls = GeneratorBuilder.scannerMoreLine(config);

        String tableName = changeQuotes(sqls.get(0).split("\\.")[1]);
        List<HandleVo> handleList = ListUtil.list(false);
        String mainSql = sqls.get(0);
        for(int i = 1; i < sqls.size(); i++){
            String columnName = sqls.get(i).split(" ")[2];
            String subSql = cleanSymbol(sqls.get(i));
            HandleVo handleVo =  HandleVo.builder()
                    .sql(mainSql + subSql)
                    .columnName(changeQuotes(columnName))
                    .build();
            handleList.add(handleVo);
        }
        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, tableName);
        dataMap.put(FILE_DESC, config.getFileName());

        return generatorBuilder.render("updateFieldLength.vm", dataMap, config);
    }

    /**
     * 添加索引
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    @Override
    public String insertIndex(DataConfig config){
        System.out.println("添加索引SQL：");
        System.out.println("示例: ");
        System.out.println("ALTER TABLE `gp-gpfa`.`gpfa_demand_catalog_config`  ");
        System.out.println("ADD INDEX `index`(`SEQUENCE`, `CATALOG_GROUP_NAME`) USING BTREE,");
        System.out.println("ADD INDEX `index2`(`CATALOG_GROUP_NAME`, `CATALOG_GROUP_CODE`) USING BTREE;");

        List<String> sqls = GeneratorBuilder.scannerMoreLine(config);

        String tableName = changeQuotes(sqls.get(0).split("\\.")[1]);
        List<HandleVo> handleList = ListUtil.list(false);
        String mainSql = sqls.get(0);
        for(int i = 1; i < sqls.size(); i++){
            String indexName = getIndexName(sqls.get(i));
            String subSql = cleanLastSymbol(sqls.get(i));
            HandleVo handleVo =  HandleVo.builder()
                    .sql(mainSql + subSql)
                    .columnName(changeQuotes(indexName))
                    .build();
            handleList.add(handleVo);
        }
        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, tableName);
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("insertIndex.vm", dataMap, config);
    }

    /**
     * 获取索引名称
     *
     * @author jiangjiamin
     * @date 2022/5/7 17:05:59
     * @param sql
     * @return: java.lang.String
     */
    public static String getIndexName(String sql){
        int startIndex = sql.indexOf("`");
        int endIndex = sql.indexOf("`", startIndex + 1);
        return sql.substring(startIndex + 1, endIndex);
    }

    /**
     * 修改索引
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    @Override
    public String updateIndex(DataConfig config){
        System.out.println("修改索引SQL：");
        System.out.println("示例: ");
        System.out.println("ALTER TABLE `gp-gpfa`.`gpfa_demand_catalog_config`  ");
        System.out.println("ADD INDEX `index`(`SEQUENCE`, `CATALOG_GROUP_NAME`) USING BTREE,");
        System.out.println("ADD INDEX `index2`(`CATALOG_GROUP_NAME`, `CATALOG_GROUP_CODE`) USING BTREE;");

        List<String> sqls = GeneratorBuilder.scannerMoreLine(config);

        String tableName = changeQuotes(sqls.get(0).split("\\.")[1]);
        List<HandleVo> handleList = ListUtil.list(false);
        String mainSql = sqls.get(0);
        for(int i = 1; i < sqls.size(); i++){
            String indexName = getIndexName(sqls.get(i));
            String subSql = cleanSymbol(sqls.get(i));
            HandleVo handleVo =  HandleVo.builder()
                    .sql(mainSql + subSql)
                    .columnName(changeQuotes(indexName))
                    .build();
            handleList.add(handleVo);
        }
        Map<String, Object> dataMap = MapUtil.of(SQL_LIST, handleList);
        dataMap.put(TABLE_NAME, tableName);
        dataMap.put(FILE_DESC, config.getFileName());
        return generatorBuilder.render("updateIndex.vm", dataMap, config);
    }

    /**
     * 多行SQL
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
    @Override
    public String multilineSql(DataConfig config){
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

    public String createTableSqlByMd(){
        System.out.println("请输入表名：");
        String tableName = GeneratorBuilder.scannerOneLine();
        System.out.println("请输入表描述：");
        String tableComment = GeneratorBuilder.scannerOneLine();

        List<String> inputs = GeneratorBuilder.scannerMoreLine(null);

        List<String> results = new ArrayList<>();
        for(String input : inputs){
            String[] values = input.trim().split("\\|");
            StringBuilder sb = new StringBuilder(values[1].trim() + " " + values[2].trim() + " DEFAULT NULL COMMENT'" + values[5].trim() + "',");
            results.add(sb.toString());
            System.out.println(input);
        }

        Map<String, Object> dataMap = MapUtil.of("sqlList", results);
        dataMap.put("tableName", tableName);
        dataMap.put("tableComment", tableComment);
        return generatorBuilder.render("createTable.vm", dataMap, false, null);

    }
}
