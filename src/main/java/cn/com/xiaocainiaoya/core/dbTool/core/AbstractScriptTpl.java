package cn.com.xiaocainiaoya.core.dbTool.core;

import cn.com.xiaocainiaoya.core.dbTool.util.ConfigUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 脚本模板抽象类
 *
 * @author wendao76
 * @sourceVersion 1.0
 * @description 脚本模板抽象类
 * @className AbstractScriptTpl
 * @date 2021-6-29 9:10
 */
public abstract class AbstractScriptTpl implements IScriptTpl {
  @Setter String sqlTemplate;

  String basePath;

  @Override
  public void test() {
    this.mainSql();
  }

  @Override
  public String preposeSql() {
    String tplSql =
        "SELECT NOT EXISTS(SELECT 1 FROM `{}`.`sys_database_change_log` WHERE ID = '{}')";
    return StrUtil.format(tplSql, this.getChangeLogDbName(),getChangeId());
  }

  @Override
  public String updatePreposeSql() {
    String tplSql =
        "INSERT INTO `{changeLogDbName}`.`sys_database_change_log` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `LIQUIBASE`, `DEPLOYMENT_ID`, `TAG`, `LABELS`) VALUES ('{changeId}', '{changeUser}', '{changeId}', now(), '0', 'EXECUTED', '', 'sql', '', '3.10.1', '001', '{sourceVersion}', '{database}')";
    Map<String, String> params = new HashMap<>(4);
    params.put("sourceVersion", getSourceVersion());
    params.put("changeId", getChangeId());
    params.put("changeUser", getChangeUser());
    params.put("database", getDatabase());
    params.put("changeLogDbName", this.getChangeLogDbName());
    return StrUtil.format(tplSql, params);
  }

  @Override
  public void setBasePath(String path) {
    this.basePath = path;
  }

  @Override
  public String getBasePath() {
    return basePath;
  }

  public String getChangeLogDbName(){
    return StrUtil.blankToDefault(ConfigUtil.getChangeLogDbMame(), "gpx_basic");
  }
}
