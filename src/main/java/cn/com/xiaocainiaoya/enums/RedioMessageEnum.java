package cn.com.xiaocainiaoya.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedioMessageEnum {

    ADD_MESSAGE("1","insertLineRadio","示例: \nINSERT INTO `gpx_bidconfirm`.`bid_confirm_process_file` (`ID`, `PROJECT_ID`, `PACKAGE_ID`, `NOTICE_ID`, `NOTICE_TYPE`, `BUSINESS_ID`, `OWNER_ID`, `OWNER_NAME`, `EFFECTIVE_DATE`, `STAMP_STATUS`, `FILE_STATUS`, `FILE_STATUS_NAME`, `FILE_CLASS`, `FILE_TYPE_CODE`, `FILE_TYPE_NAME`, `FILE_NAME`, `ATTACHMENT_ID`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`, `UPDATE_TIME`) VALUES ('2c90842b82f84515018339a8e31300e5', '2c908b9482abb4000182afa6eae50eb6', '2c908b9482abb4000182afa6ec1f0ebf', NULL, NULL, '2c908b9482abb4000182afa6ec1f0ebf', '2c908dfc821b46af01821b48df260008', '测试供应商003', NULL, 1, 1, '有效', 1, 1001, '中标通知书', '测试供应商003-包(1)中标通知书.pdf', '2c90842b82f84515018339a8e31300e4', 'B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:53', 'B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:53');"),

    BATCH_ADD_MESSAGE("2","batchRadio","示例: \nINSERT INTO `gpx_bidconfirm`.`bid_confirm_process_file` (`ID`,`PROJECT_ID`, `PACKAGE_ID`, `NOTICE_ID`, `NOTICE_TYPE`, `BUSINESS_ID`, `OWNER_ID`, `OWNER_NAME`, `EFFECTIVE_DATE`, `STAMP_STATUS`, `FILE_STATUS`, `FILE_STATUS_NAME`, `FILE_CLASS`, `FILE_TYPE_CODE`, `FILE_TYPE_NAME`, `FILE_NAME`, `ATTACHMENT_ID`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`, `UPDATE_TIME`) VALUES ('2c90842b82f84515018339a8e31300e5','2c908b9482abb4000182afa6eae50eb6', '2c908b9482abb4000182afa6ec1f0ebf', NULL, NULL,'2c908b9482abb4000182afa6ec1f0ebf', '2c908dfc821b46af01821b48df260008', '测试供应商003', NULL, 1, 1, '有效', 1,1001, '中标通知书', '测试供应商003-包(1)中标通知书.pdf', '2c90842b82f84515018339a8e31300e4','B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:53','B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:53');\n" +
            "INSERT INTO `gpx_bidconfirm`.`bid_confirm_process_file` (`ID`, `PROJECT_ID`, `PACKAGE_ID`, `NOTICE_ID`, `NOTICE_TYPE`, `BUSINESS_ID`, `OWNER_ID`, `OWNER_NAME`, `EFFECTIVE_DATE`, `STAMP_STATUS`, `FILE_STATUS`,`FILE_STATUS_NAME`, `FILE_CLASS`, `FILE_TYPE_CODE`, `FILE_TYPE_NAME`, `FILE_NAME`, `ATTACHMENT_ID`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`, `UPDATE_TIME`) VALUES ('2c90842b82f84515018339a8e32500e7', '2c908b9482abb4000182afa6eae50eb6','2c908b9482abb4000182afa6ec1f0ebf', NULL, NULL, '2c908b9482abb4000182afa6ec1f0ebf', '2c908dfc821b46af01821b48df260008', '测试供应商003', NULL, 0, 1, '有效', 1, 1005, '中标通知书（原始版本）', '测试供应商003-包(1) 中标通知书.pdf', '2c90842b82f84515018339a8e31300e4', 'B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:53', 'B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:53');\n" +
            "INSERT INTO `gpx_bidconfirm`.`bid_confirm_process_file` (`ID`, `PROJECT_ID`, `PACKAGE_ID`, `NOTICE_ID`, `NOTICE_TYPE`, `BUSINESS_ID`, `OWNER_ID`, `OWNER_NAME`, `EFFECTIVE_DATE`, `STAMP_STATUS`, `FILE_STATUS`,`FILE_STATUS_NAME`, `FILE_CLASS`, `FILE_TYPE_CODE`, `FILE_TYPE_NAME`, `FILE_NAME`, `ATTACHMENT_ID`, `CREATE_USER_ID`, `CREATE_USER_NAME`, `CREATE_TIME`, `UPDATE_USER_ID`, `UPDATE_USER_NAME`, `UPDATE_TIME`) VALUES ('2c90842b82f84515018339a8e69400ea', '2c908b9482abb4000182afa6eae50eb6', '2c908b9482abb4000182afa6ec1f0ebf', NULL, NULL, '2c908b9482abb4000182afa6ec1f0ebf', '2c908dfc821b46af01821b46afee0000', '测试供应商001', NULL, 1, 1, '有效', 1, 1001, '中标通知书', '测试供应商001-包(1)中标通知书 .pdf', '2c90842b82f84515018339a8e69400e9', 'B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:54', 'B1CA2544A9A6495CB0BCCBEDF04156F0', '四川省集采机构', '2022-09-14 09:41:54');"),

    ADD_INDEX("5","addIndexRedio","示例: \n" +
            "ALTER TABLE `gp-gpfa`.`gpfa_demand_catalog_config`  \n" +
            "ADD INDEX `index`(`SEQUENCE`, `CATALOG_GROUP_NAME`) USING BTREE,\n" +
            "ADD INDEX `index2`(`CATALOG_GROUP_NAME`, `CATALOG_GROUP_CODE`) USING BTREE;"),

    UPDATE_INDEX("6","updateIndexRedio","示例: \n" +
            "ALTER TABLE `gp-gpfa`.`gpfa_demand_catalog_config`  \n" +
            "ADD INDEX `index`(`SEQUENCE`, `CATALOG_GROUP_NAME`) USING BTREE,\n" +
            "ADD INDEX `index2`(`CATALOG_GROUP_NAME`, `CATALOG_GROUP_CODE`) USING BTREE;"),


    MUL_SQL("7","mulSqlRedio","示例: \n" + "生成的脚本是以''' xxxx '''' 三引号包裹"),

    ADD_FIELD("3.1","insertFieldRadio","示例: \n" + "批量插入字段SQL，语句之间请不要存在换行！：(请使用Navicat Premium 标准SQL)\n" +
            "如下示例SQL，支持部分语句只有一个ADD子句，部分语句多个ADD子句混用，但语句之间请不要存在换行！\n" +
            "判断逻辑为以分号结尾则表示是对同一个表的操作，会从这个语句中读取填充模板相关数据\n" +
            "示例: \n" +
            "ALTER TABLE `gp-gpfa`.`gpfa_demand_cancel`\n" +
            "ADD COLUMN `upda` varchar(255) NULL COMMENT '1' AFTER `UPDATE_USER_NAME`,\n" +
            "ADD COLUMN `cc` varchar(255) NULL COMMENT '2' AFTER `upda`;\n" +
            "ALTER TABLE `gp-gpfa`.`gpfa_demand_cancel_2`\n" +
            "ADD COLUMN `upda` varchar(255) NULL COMMENT '1' AFTER `UPDATE_USER_NAME`;"),


    UPDATE_FIELD("4","updateFieldRadio","示例：\n" +
            "ALTER TABLE `gp-gpfa`.`gpfa_demand_catalog_config` \n" +
            "MODIFY COLUMN `CATALOG_NAME` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT " +
            "NULL COMMENT '目录名称，如公告标题等 ' AFTER `CATALOG_CODE`,\n" +
            "MODIFY COLUMN `CATALOG_REF` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT " +
            "NULL COMMENT '目录关系，包、项目等' AFTER `CATALOG_NAME`;"),


    ;
    private String radio;
    private String code;
    private String value;

    public static String getValueByCode(String code){
        for(RedioMessageEnum redioMessageEnum : RedioMessageEnum.values()){
            if(redioMessageEnum.getCode().equals(code)){
                return redioMessageEnum.getValue();
            }
        }
        return StrUtil.EMPTY;
    }

    public static String getValueByRadio(String radio){
        for(RedioMessageEnum redioMessageEnum : RedioMessageEnum.values()){
            if(redioMessageEnum.getRadio().equals(radio)){
                return redioMessageEnum.getValue();
            }
        }
        return StrUtil.EMPTY;
    }
}
