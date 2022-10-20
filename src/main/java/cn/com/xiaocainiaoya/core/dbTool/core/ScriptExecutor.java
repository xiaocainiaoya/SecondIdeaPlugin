package cn.com.xiaocainiaoya.core.dbTool.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 脚本执行器
 *
 * @author wendao76
 * @version 1.0
 * @description 脚本执行器
 * @className AbstractScriptExecutor
 * @date 2021-6-29 9:58
 */
//@Slf4j
public class ScriptExecutor implements IScriptExecutor {
  IScriptTpl scriptTpl;

  /** 是否纯净执行， 不包含存储过程前后判断 */
  Boolean isPureExecute = false;

  public ScriptExecutor(IScriptTpl scriptTpl) {
    this.scriptTpl = scriptTpl;
  }

  @Override
  public String execute() throws SQLException {
    if (scriptTpl.type().equals(IScriptTpl.TYPE_PROCEDURE)) {
      isPureExecute = true;
    }
    this.beforeExecute();
    if (!this.preposePassed()) {
      //log.warn("当前脚本已经执行过：{}", scriptTpl.getChangeId());
      System.out.println("当前脚本已经执行过：" + scriptTpl.getChangeId());
      return "当前脚本已经执行过：" + scriptTpl.getChangeId();
    }
    this.batchExecute(scriptTpl.mainSql());
    this.batchExecuteFile(scriptTpl.mainSqlPath());
    this.afterExecute();
    return StrUtil.EMPTY;
  }

  /**
   * 执行sql文件
   *
   * @param sqlPaths
   * @return
   */
  private boolean batchExecuteFile(List<String> sqlPaths) {
    if (sqlPaths.isEmpty()) {
      return true;
    }
    try {
      for (int i = 0; i < sqlPaths.size(); i++) {
        List<String> sqlLines =
            FileUtil.readLines(
                scriptTpl.getBasePath() + File.separator + sqlPaths.get(i), StandardCharsets.UTF_8);
        sqlLines = sqlLines.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        if (CollUtil.isEmpty(sqlLines)) {
          return false;
        }
        if (scriptTpl.type().equals(IScriptTpl.TYPE_DEFAULT)) {
          for (int j = 0; j < sqlLines.size(); j++) {
            Dao.execute(sqlLines.get(j));
          }
          continue;
        }

        // 执行存储过程(把一个文件的内容放到存储过程执行)
        Dao.callProcedure(
            scriptTpl.getSourceVersion(),
            scriptTpl.getChangeId(),
            scriptTpl.getChangeUser(),
            scriptTpl.getDatabase(),
            sqlLines);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DbException("脚本执行出错：" + e.getMessage());
    }

    sqlPaths.forEach(path -> {});
    return true;
  }
  /**
   * 批量执行sql脚本
   *
   * @param sqlMetas
   * @return
   */
  private boolean batchExecute(List<SqlMeta> sqlMetas) {
    if (sqlMetas.isEmpty()) {
      return true;
    }
    SqlMeta sqlMeta;
    for (int i = 0; i < sqlMetas.size(); i++) {
      sqlMeta = sqlMetas.get(i);
      try {
        if (StrUtil.isBlank(sqlMeta.getPreposeSql()) || Dao.isPass(sqlMeta.getPreposeSql())) {
          if (sqlMeta.getSqlType() == 1) {
            // 执行存储过程
            Dao.callProcedure(
                scriptTpl.getSourceVersion(),
                scriptTpl.getChangeId(),
                scriptTpl.getChangeUser(),
                scriptTpl.getDatabase(),
                sqlMeta.getSqls());
            continue;
          }
          for (String sql : sqlMeta.getSqls()) {
            Dao.execute(sql);
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
        throw new DbException("脚本执行出错：" + e.getMessage());
      }
    }
    //log.info("Sql脚本执行成功：{}", scriptTpl.getChangeId());
    System.out.println("Sql脚本执行成功：" + scriptTpl.getChangeId());
    return true;
  }

  /**
   * 是否通过前置sql判断
   *
   * @return
   */
  public boolean preposePassed() throws SQLException {
    return isPureExecute || Dao.isPass(scriptTpl.preposeSql());
  }

  /**
   * 结束执行
   *
   * @return
   */
  public void beforeExecute() {}
  /**
   * 结束执行
   *
   * @return
   */
  public boolean afterExecute() throws SQLException {
    if (!isPureExecute) {
      Dao.execute(scriptTpl.updatePreposeSql());
    }
    return true;
  }
}
