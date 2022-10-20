package cn.com.xiaocainiaoya.core.dbTool.tpl

import cn.com.xiaocainiaoya.core.dbTool.core.AbstractScriptTpl
import cn.com.xiaocainiaoya.core.dbTool.core.SqlMeta


/**
 * 脚本示例
 * @author liwenhu
 */
class ScriptExample extends AbstractScriptTpl {
    /**
     * 数据库版本
     * @return
     */
    @Override
    String getSourceVersion() {
        return "V5.3.12.3"
    }

    /**
     * 数据库
     * @return
     */
    @Override
    String getDatabase() {
        return "gpx_basic"
    }

    /**
     * 版本修改ID
     * @return
     */
    @Override
    String getChangeId() {
        return "DB_V5.3.12.3_GPX_BASIC_03_20210625_基础服务_新增供应商信用服务校验开关"
    }

    /**
     * 负责人
     * @return
     */
    @Override
    String getChangeUser() {
        return "Liwenhu"
    }

    @Override
    List<String> mainSqlPath() {
        return super.mainSqlPath()
    }

    @Override
    List<SqlMeta> mainSql() {
        return new ArrayList<SqlMeta>() {
            {
                add(SqlMeta.build("DELETE FROM `gpx_basic`.`basic_system_definition` WHERE `CODE` = 'gpx.archive.gpcms.enable'"))
                add(SqlMeta.build("INSERT INTO `gpx_basic`.`basic_system_definition`(`ID`, `CODE`, `NAME`, `REQUIRED_STATUS`, `VALUE_TYPE`, `DEFAULT_VALUE`, `REMARK`, `DESCRIPTION`, `VALUE_RANGE`, `ENABLE`, `CREATE_TIME`, `BIZ_TYPE_CODE`, `BIZ_TYPE_NAME`) VALUES (MD5(UUID()), 'gpx.archive.gpcms.enable', '诚信系统中标候选人校验开关', '1', 'boolean', '0', '是为开启，否为关闭', '是为开启，否为关闭', NULL, 1, '2020-03-04 19:57:33', 'archive', '档案')"))
                add(SqlMeta.build("DELETE FROM `gpx_basic`.`basic_system_definition` WHERE `CODE` = 'gpx.archive.gpcms.config';"))
                add(SqlMeta.build("INSERT INTO `gpx_basic`.`basic_system_definition`(`ID`, `CODE`, `NAME`, `REQUIRED_STATUS`, `VALUE_TYPE`, `DEFAULT_VALUE`, `REMARK`, `DESCRIPTION`, `VALUE_RANGE`, `ENABLE`, `CREATE_TIME`, `BIZ_TYPE_CODE`, `BIZ_TYPE_NAME`) VALUES (MD5(UUID()), 'gpx.archive.gpcms.config', '诚信系统中标候选人校验配置', '1', 'String', 'ShanXi', '对应诚信系统配置，如ShanXi为陕西诚信系统', '对应诚信系统配置，如ShanXi为陕西诚信系统', NULL, 1, '2020-03-04 19:57:33', 'archive', '档案')"))
            }
        }
    }
}
