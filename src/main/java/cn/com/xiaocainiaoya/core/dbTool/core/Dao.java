package cn.com.xiaocainiaoya.core.dbTool.core;

import cn.com.xiaocainiaoya.core.dbTool.util.ConfigUtil;
import cn.hutool.core.util.StrUtil;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author wendao76
 * @version 1.0
 * @description 类描述信息
 * @className Dao
 * @date 2021-6-28 11:25
 */
//@Slf4j
public class Dao {
  static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
  // static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  // static final String DB_URL =
  // "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

  static Connection conn = null;

  private Dao() {}

  /**
   * 执行sql脚本
   *
   * @param sql
   * @throws SQLException
   */
  public static void execute(String sql) throws SQLException {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      //log.error("数据库脚本执行出错", e);
      System.out.println("数据库脚本执行出错");
      throw e;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * @param version 数据库版本
   * @param changeId 脚本ID
   * @param changeUser 脚本开发者
   * @param database 数据库
   * @param sqls 执行的sql
   * @throws SQLException
   */
  public static void callProcedure(
      String version, String changeId, String changeUser, String database, String... sqls)
      throws SQLException {
    callProcedure(version, changeId, changeUser, database, Arrays.asList(sqls));
  }

  /**
   * @param version 数据库版本
   * @param changeId 脚本ID
   * @param changeUser 脚本开发者
   * @param database 数据库
   * @param sqls 执行的sql
   * @throws SQLException
   */
  public static void callProcedure(
      String version, String changeId, String changeUser, String database, List<String> sqls)
      throws SQLException {
    // 存储过程名称
    final String PROCEDURE_NAME = "gpx_database_procedure";
    CallableStatement stmt = null;

    try {
      String prevSql = StrUtil.format("DROP PROCEDURE IF EXISTS {}", PROCEDURE_NAME);
      StringBuilder procedureBody = new StringBuilder();
      procedureBody.append(
          StrUtil.format(
              "CREATE PROCEDURE {}(in gpx_source_version VARCHAR(255), in gpx_change_id VARCHAR(255), in gpx_change_user VARCHAR(255), in gpx_database_name VARCHAR(255))",
              PROCEDURE_NAME));
      procedureBody.append("\r\nBEGIN\r\n");
      procedureBody
          .append("\t")
          .append(
              "IF NOT EXISTS(SELECT 1 FROM `{changeLogDbName}`.`sys_database_change_log` WHERE ID = gpx_change_id) THEN\r\n");
      sqls.forEach(
          s -> {
            procedureBody.append("\t\t").append(s);
          });

      procedureBody.append(
          "\r\n\t\tINSERT INTO `{changeLogDbName}`.`sys_database_change_log` (`ID`,`AUTHOR`,`FILENAME`,`DATEEXECUTED`,`ORDEREXECUTED`,`EXECTYPE`,`MD5SUM`,`DESCRIPTION`,`COMMENTS`,`LIQUIBASE`,`DEPLOYMENT_ID`,`TAG`,`LABELS`) VALUES (gpx_change_id,gpx_change_user,gpx_change_id,now(),'0','EXECUTED','','sql','','3.10.1','001',gpx_source_version,gpx_database_name);");
      procedureBody.append("\r\n\tEND IF;\r\n");
      procedureBody.append("END;\r\n");

      String callSql = StrUtil.format("CALL {}(?, ?, ?, ?)", PROCEDURE_NAME);
      String postSql = StrUtil.format("DROP PROCEDURE {}", PROCEDURE_NAME);
      // 如果存在存储过程， 则先删除
      execute(prevSql);

      // 执行存储过程创建语句
      HashMap<String, String> param = new HashMap<>(1);
      param.put("changeLogDbName", Dao.getChangeLogDbName());
      String procedureBodyTpl = procedureBody.toString();
      execute(StrUtil.format(procedureBodyTpl,param));

      // 执行存储过程调用
      stmt = conn.prepareCall(callSql);
      stmt.setString(1, version);
      stmt.setString(2, changeId);
      stmt.setString(3, changeUser);
      stmt.setString(4, database);
      stmt.execute();

      // 存储过程清理
      execute(postSql);
    } catch (SQLException e) {
      //log.error("存储过程执行出错", e);
      throw e;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * 获取changeLog数据库名
   * @return changeLog数据库名
   */
  public static String getChangeLogDbName(){
    return StrUtil.blankToDefault(ConfigUtil.getChangeLogDbMame(), "gpx_basic");
  }

  /**
   * 是否存在
   *
   * @param sql
   * @throws SQLException
   */
  public static boolean isPass(String sql) throws SQLException {
    boolean result;
    Statement stmt = null;
    ResultSet resultSet = null;
    try {
      stmt = conn.createStatement();
      resultSet = stmt.executeQuery(sql);
      resultSet.next();
      result = resultSet.getInt(1) == 1;
    } catch (SQLException e) {
      //log.error("数据库查询出错", e);
      throw e;
    } finally {
      if (resultSet != null) {
        resultSet.close();
      }
      if (stmt != null) {
        stmt.close();
      }
    }
    return result;
  }

  public static void commit() throws SQLException {
    if (conn != null) {
      //log.info("数据库操作提交");
      System.out.println("数据库操作提交");
      conn.commit();
    }
  }

  public static void rollback() throws SQLException {
    if (conn != null) {
      //log.info("数据库操作回滚");
      System.out.println("数据库操作回滚");
      conn.rollback();
    }
  }

  public static void close() throws SQLException {
    if (conn != null) {
      //log.info("数据库连接关闭");
      System.out.println("数据库连接关闭");
      conn.close();
    }
  }

  /**
   * 数据库连接初始化
   *
   * @param connUrl
   * @param username
   * @param password
   * @return
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public static void openConn(String connUrl, String username, String password)
      throws ClassNotFoundException, SQLException {
    try {
      // 先关闭连接
      if (conn != null && !conn.isClosed()) {
        conn.close();
      }
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(connUrl, username, password);
      // 关闭自动提交(整个文件统一成功失败)
      conn.setAutoCommit(false);
    } catch (Exception e) {
      throw e;
    }
    //log.info("数据库连接初始化");
    System.out.println("数据库连接初始化");
  }
}
