package cn.com.xiaocainiaoya.core.dbTool.util;

import cn.com.xiaocainiaoya.core.dbTool.Application;
import cn.com.xiaocainiaoya.core.dbTool.core.DbConfig;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 配置辅助类
 *
 * @author wendao76
 * @version 1.0
 * @description 配置辅助类
 * @className DbConfigUtil
 * @date 2021-6-28 16:15
 */
//@Slf4j
public class ConfigUtil {
  private static String appDir;
  public static final String DATASOURCE = "dataSource";
  public static final String SCRIPT_PATH = "scriptPath";
  public static final String CUSTOM_SCRIPT_PATH = "customScriptPath";
  public static final String ERROR_SCRIPT_PATH = "errorScriptPath";
  public static final String SQL_OUTPUT_PATH = "sqlOutputPath";
  public static final String CHANGE_LOG_DB_NAME = "changeLogDbName";

  public static DbConfig getDbConfig() {
    return dbConfig;
  }

  public static void setDbConfig(DbConfig dbConfig) {
    ConfigUtil.dbConfig = dbConfig;
  }

  public static String getScriptPath() {
    return scriptPath;
  }

  public static void setScriptPath(String scriptPath) {
    ConfigUtil.scriptPath = scriptPath;
  }

  public static String getCustomScriptPath() {
    return customScriptPath;
  }

  public static void setCustomScriptPath(String customScriptPath) {
    ConfigUtil.customScriptPath = customScriptPath;
  }

  private static DbConfig dbConfig;

  private static String scriptPath;

  private static String customScriptPath;

  public static String getErrorScriptPath() {
    return errorScriptPath;
  }

  private static String errorScriptPath;

  public static String getSqlOutputPath() {
    return sqlOutputPath;
  }

  public static void setSqlOutputPath(String sqlOutputPath) {
    ConfigUtil.sqlOutputPath = sqlOutputPath;
  }

  private static String sqlOutputPath;

  private static String changeLogDbMame;

  public static String getChangeLogDbMame() {
    return changeLogDbMame;
  }

  public static void readConfig(String appDir, String path) {
    try {
      ConfigUtil.appDir = appDir;
      String fileContent = FileUtil.readString(appDir + path, Charset.forName("UTF8"));
      JSONObject jsonObject = JSONUtil.parseObj(fileContent);
      dbConfig = jsonObject.getBean(DATASOURCE, DbConfig.class);
      scriptPath = jsonObject.getStr(SCRIPT_PATH);
      customScriptPath = jsonObject.getStr(CUSTOM_SCRIPT_PATH);
      sqlOutputPath = jsonObject.getStr(SQL_OUTPUT_PATH);
      errorScriptPath = jsonObject.getStr(ERROR_SCRIPT_PATH);
      changeLogDbMame = jsonObject.getStr(CHANGE_LOG_DB_NAME);
      buildPath();
    } catch (Exception e) {
      //log.error("配置读取失败", e);
      System.out.println("配置读取失败");
    }
  }

  /**
   * 根据路径读取配置相关信息，并返回配置实体
   *
   * @author jiangjiamin
   * @date 2022/7/22 14:24:47
   * @param dbToolConfigPath
   * @return: com.bosssoft.gpmscloud.dbTool.core.DbConfig
   */
  public static DbConfig readConfigByPath(String dbToolConfigPath){
    String currentDir = Application.getAppPath();
    // 配置文件读取
    if (FileUtil.isAbsolutePath(dbToolConfigPath)) {
      if (FileUtil.isDirectory(dbToolConfigPath)) {
        dbToolConfigPath = dbToolConfigPath + "/config.json";
      }
      ConfigUtil.readConfig("", dbToolConfigPath);
    } else {
      ConfigUtil.readConfig(currentDir, File.separator + dbToolConfigPath);
    }
    return ConfigUtil.getDbConfig();
  }

  public static void mockReadConfig(DbConfig dbConfigParam, String scriptPathParam, String customScriptPathParam, String sqlOutputPathParam, String errorScriptPathParam) {
    try {
      dbConfig = dbConfigParam;
      scriptPath = scriptPathParam;
      customScriptPath = customScriptPathParam;
      sqlOutputPath = sqlOutputPathParam;
      errorScriptPath = errorScriptPathParam;
      //changeLogDbMame = changeLogDbMame;
      //buildPath();
    } catch (Exception e) {
      //log.error("配置读取失败", e);
      System.out.println("配置读取失败");
    }
  }

  public static void buildPath() {
    if (StrUtil.isNotBlank(scriptPath) && !FileUtil.isAbsolutePath(scriptPath)) {
      scriptPath = appDir + File.separator + scriptPath;
    }

    if (StrUtil.isNotBlank(customScriptPath) && !FileUtil.isAbsolutePath(customScriptPath)) {
      customScriptPath = appDir + File.separator + customScriptPath;
    }

    if (StrUtil.isNotBlank(sqlOutputPath) && !FileUtil.isAbsolutePath(sqlOutputPath)) {
      sqlOutputPath = appDir + File.separator + sqlOutputPath;
    }

    if (StrUtil.isNotBlank(errorScriptPath) && !FileUtil.isAbsolutePath(errorScriptPath)) {
      errorScriptPath = appDir + File.separator + errorScriptPath;
    }
  }

}
