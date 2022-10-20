package cn.com.xiaocainiaoya.core.dbTool;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.ResourceComponent;
import cn.com.xiaocainiaoya.core.dbTool.core.Dao;
import cn.com.xiaocainiaoya.core.dbTool.core.DbConfig;
import cn.com.xiaocainiaoya.core.dbTool.core.IScriptTpl;
import cn.com.xiaocainiaoya.core.dbTool.core.ScriptExecutor;
import cn.com.xiaocainiaoya.core.dbTool.core.builder.SqlBuilder;
import cn.com.xiaocainiaoya.core.dbTool.util.ConfigUtil;
import cn.com.xiaocainiaoya.core.dbTool.util.FileUtilExt;
import cn.com.xiaocainiaoya.core.dbTool.util.GroovyUtil;
import cn.com.xiaocainiaoya.vo.ConfigFileInfoVo;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author wendao76
 * @version 1.0
 * @description 类描述信息
 * @className Application
 * @date 2021-6-28 11:21
 */
//@Slf4j
public class Application {

  /** sql模板文件内容 */
  static SqlBuilder sqlBuilder;

  /** 数据库配置 */
  static DbConfig dbConfig;

  static {
    sqlBuilder = SqlBuilder.getInstance();
    ResourceComponent resourceComponent = new ResourceComponent();

    String sqlTemplate = resourceComponent.readFile("script.tpl.sql");
    sqlBuilder.setTplContent(sqlTemplate);
  }

  public static void main(String[] args) {
    String currentDir = getAppPath();
    // 创建sql 构造器
    sqlBuilder = SqlBuilder.getInstance();
    String sqlTemplate = FileUtilExt.readFromJar(Application.class, "/script.tpl.sql");
    sqlBuilder.setTplContent(sqlTemplate);
    if (args.length == 0) {
      //log.error("请输入配置文件地址");
      System.out.println("请输入配置文件地址");
    }
    // 配置文件读取
    if (FileUtil.isAbsolutePath(args[0])) {
      if (FileUtil.isDirectory(args[0])) {
        args[0] = args[0] + "/config.json";
      }
      ConfigUtil.readConfig("", args[0]);
    } else {
      ConfigUtil.readConfig(currentDir, File.separator + args[0]);
    }

    int func = showConfirmDialog();
    if (func == 99) {
      return;
    }
    if (func == 100) {
      version();
      return;
    }
    try {
      // 通用数据库名脚本
      List<File> commonFiles = FileUtil.loopFiles(ConfigUtil.getScriptPath());
      commonFiles = arrangeFiles(commonFiles);
      // 私有定制化脚本
      List<File> customFiles = FileUtil.loopFiles(ConfigUtil.getCustomScriptPath());
      customFiles = arrangeFiles(customFiles);

      dbConfig = ConfigUtil.getDbConfig();
      runScripts(func, commonFiles, null, ConfigUtil.getScriptPath());
      runScripts(func, customFiles, null, ConfigUtil.getCustomScriptPath());
    } catch (Exception e) {
      //log.error("发生未知异常", e);
      e.printStackTrace();
    } finally {
      try {
        Dao.close();
      } catch (SQLException e) {
        e.printStackTrace();
        //log.error("JDBC连接关闭失败", e);
      }
    }
  }

  /**
   * 执行单个脚本
   *
   * @author jiangjiamin
   * @date 2022/7/22 14:13:32
   * @param
   * @return: void
   */
  public static void runSingleScriptByFilePath(String scriptFilePath, String dbToolConfigPath){
//    if(StrUtil.isBlank(dbToolConfigPath)){
//      //log.info("执行脚本配置文件路径为空，故不进行脚本执行。");
//      System.out.println("执行脚本配置文件路径为空，故不进行脚本执行。");
//      return;
//    }
    // 创建sql 构造器
//    sqlBuilder = SqlBuilder.getInstance();
//    String sqlTemplate = FileUtilExt.readFromJar(Application.class, "/script.tpl.sql");
//    sqlBuilder.setTplContent(sqlTemplate);
    // 读取配置信息
    //DbConfig dbConfig = ConfigUtil.readConfigByPath(dbToolConfigPath);

    // 执行脚本
    runSingleScripts(scriptFilePath, ConfigUtil.getDbConfig(), ConfigUtil.getScriptPath());
  }

  public static List<File> arrangeFiles(List<File> files) {
    if (files.isEmpty()) {
      return new ArrayList<>();
    }
    // 根据文件名过滤
    files =
        files.stream()
            .filter(
                f -> {
                  return f.getName().endsWith(".groovy");
                })
            .collect(Collectors.toList());
    if (files.isEmpty()) {
      //log.warn("找不到可执行脚本,请确认一下脚本目录是否准确");
      System.out.println("找不到可执行脚本,请确认一下脚本目录是否准确");
      return new ArrayList<>();
    }

    // 根据文件名排序
    files.sort(
        (f1, f2) -> {
          return f1.getName().compareToIgnoreCase(f2.getName());
        });
    return files;
  }

  /**
   * 确认提示框展示
   *
   * @return
   */
  public static int showConfirmDialog() {
    System.out.println("通用文件输入目录   : " + ConfigUtil.getScriptPath());
    System.out.println("个性化文件输入目录 : " + ConfigUtil.getCustomScriptPath());
    System.out.println("错误脚本输出目录   : " + ConfigUtil.getErrorScriptPath());
    System.out.println("SQL脚本输出目录   : " + ConfigUtil.getSqlOutputPath());
    System.out.println("请输入  : 1，确认执行 2. 仅生成sql脚本 99.取消执行, 100.版本 并按下确认键");
    Scanner input = new Scanner(System.in);
    String wd = input.nextLine();
    return Integer.parseInt(wd);
  }

  /**
   * 获取jar文件路径
   *
   * @return
   */
  public static String getAppPath() {
    String path = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    if (System.getProperty("os.name").contains("dows")) {
      path = path.substring(1, path.length());
    }
    if (path.contains("jar")) {
      path = path.substring(0, path.lastIndexOf("."));
      return path.substring(0, path.lastIndexOf("/"));
    }
    return path;
  }

  /**
   * 运行脚本
   *
   * @param func 功能
   * @param scripts 脚本列表
   * @param dbConfig 数据库配置
   * @param dirPath 最外层目录
   * @throws SQLException
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public static String runScripts(int func, List<File> scripts, ConfigFileInfoVo dbConfig, String dirPath) {
    GroovyUtil.clearCache();
    IScriptTpl scriptTpl = null;
    ScriptExecutor scriptExecutor;
    Map<String, List<String>> failedFiles = new HashMap<>(128);
    List<IScriptTpl> sqlTplList = new ArrayList<>();
    List<String> errorInfo = new ArrayList<>();
    for (int i = 0; i < scripts.size(); i++) {
      //log.info("groovy脚本开始执行：{}", scripts.get(i).getAbsolutePath());
      System.out.println(String.format("groovy脚本开始执行：%s", scripts.get(i).getAbsolutePath()));
      try {
        scriptTpl = GroovyUtil.loadScript(scripts.get(i));
        // 与执行脚本同目录
        scriptTpl.setBasePath(FileUtil.getParent(scripts.get(i).getAbsolutePath(), 1));
        sqlTplList.add(scriptTpl);
        if (func == 1) {
          // groovy脚本执行主体
          Dao.openConn(dbConfig.getDataSourceUrl(), dbConfig.getUsername(), dbConfig.getPassword());
          scriptTpl.test();
          scriptExecutor = new ScriptExecutor(scriptTpl);
          scriptExecutor.execute();
          Dao.commit();
        }
      } catch (Exception e) {
        if (scriptTpl != null) {
          if (!failedFiles.containsKey(scriptTpl.getDatabase())) {
            failedFiles.put(scriptTpl.getDatabase(), new ArrayList<>());
          }
          failedFiles.get(scriptTpl.getDatabase()).add(scripts.get(i).getAbsolutePath());
        }

        //log.error("脚本执行失败：{}", scripts.get(i).getAbsolutePath());
        //log.error("{}", e);
        System.out.println(String.format("脚本执行失败：{}", scripts.get(i).getAbsolutePath()));
        errorInfo.add(StrUtil.format("脚本执行失败：{} -> {}", scripts.get(i).getAbsolutePath(), e.getMessage()));
        e.printStackTrace();
        try {
          Dao.rollback();
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      } finally {
        try {
          Dao.commit();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    System.out.println("sql输出目录：" + dbConfig.getSqlOutputPath());
    System.out.println("文件总数：" + sqlTplList.size());
    try{
      outputSqls(sqlTplList, dbConfig.getSqlOutputPath());
    }catch (Exception e){
      dbConfig.setSqlOutputPath("生成[.sql]文件失败！" + e.getMessage());
    }


    System.out.println("异常文件输出目录：" + dbConfig.getErrorScriptPath());
    System.out.println("异常文件总数：" + failedFiles.size());
    try{
      outputErrorScripts(failedFiles, dbConfig.getErrorScriptPath());
    }catch (Exception e){
      dbConfig.setErrorScriptPath("输出错误信息文件失败！" + e.getMessage());
    }

    String templateStr = StrUtil.format(OUT_PUT_TEMPLATE, dbConfig.getSqlOutputPath(), sqlTplList.size(), dbConfig.getErrorScriptPath(), failedFiles.size());
    return templateStr + StrUtil.join("\n", errorInfo);
  }

  public static String OUT_PUT_TEMPLATE = "sql输出目录：{}\n文件总数：{}\n异常文件输出目录：{}\n异常文件总数：{}\n";

  /**
   * 执行单个脚本
   *
   * @author jiangjiamin
   * @date 2022/7/22 14:26:10
   * @param
   * @return: void
   */
  public static void runSingleScripts(String scriptFilePath, DbConfig dbConfig, String dirPath){
    GroovyUtil.clearCache();
    IScriptTpl scriptTpl = null;
    File scriptFile = new File(scriptFilePath);
    if(!scriptFile.exists()){
      //log.error("脚本文件不存在：{}", scriptFilePath);
      System.out.println("脚本文件不存在：" + scriptFilePath);
      throw new RuntimeException("脚本文件不存在"+ scriptFilePath);
    }

    //log.info("groovy脚本开始执行：{}", scriptFilePath);
    System.out.println("groovy脚本开始执行：" + scriptFilePath);
    try {
      scriptTpl = GroovyUtil.loadScript(scriptFile);
      // 与执行脚本同目录
      scriptTpl.setBasePath(FileUtil.getParent(scriptFilePath, 1));

      // groovy脚本执行主体
      Dao.openConn(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
      scriptTpl.test();
      ScriptExecutor scriptExecutor = new ScriptExecutor(scriptTpl);
      String execResult = scriptExecutor.execute();
      // todo 暂不处理，重复执行抛不抛提示问题
//      if(StrUtil.isNotEmpty(execResult)){
//        throw new RuntimeException(execResult);
//      }
      Dao.commit();

    } catch (Exception e) {
      //log.error("脚本执行失败：{}", scriptFilePath);
      System.out.println("脚本执行失败：" + scriptFilePath);
      e.printStackTrace();
      //log.error(e.getMessage(), e);
      try {
        Dao.rollback();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
      throw new RuntimeException(e.getMessage());
    } finally {
      try {
        Dao.commit();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    String sqlOutputDir = ConfigUtil.getSqlOutputPath() + File.separator + FileUtil.getName(dirPath);
    //log.info("sql输出目录：" + sqlOutputDir);
    System.out.println("sql输出目录：" + sqlOutputDir);
    outputSqls(ListUtil.of(scriptTpl), sqlOutputDir);

    System.out.println("脚本执行结束");
    //log.info("脚本执行结束");
  }

  /**
   * 输出sql脚本
   *
   * @param scriptTpls
   * @param baseDir
   */
  public static void outputSqls(List<IScriptTpl> scriptTpls, final String baseDir) {
    if (CollUtil.isEmpty(scriptTpls)) {
      return;
    }
    Set<String> fileSet = new HashSet<>();
    FileUtil.clean(baseDir);
    scriptTpls.forEach(
        s -> {
          String sql = sqlBuilder.setScriptTpl(s).build();
          String sqlFilePath =
              baseDir
                  + File.separator
                  + s.getDatabase()
                  + File.separator
                  + s.getChangeId()
                  + ".sql";
          if (fileSet.contains(sqlFilePath)) {
            //log.error("sql文件名重复");
            //log.error("change_id: " + s.getChangeId());
            System.out.println("sql文件名重复");
          } else {
            fileSet.add(sqlFilePath);
            FileUtil.writeUtf8String(sql, sqlFilePath);
          }
        });
    //log.info("生成实际sql文件数量：" + fileSet.size());
    System.out.println("生成实际sql文件数量：" + fileSet.size());
  }

  /**
   * 错误脚本输出
   *
   * @param errorScripts
   * @param baseDir
   */
  public static void outputErrorScripts(
      Map<String, List<String>> errorScripts, final String baseDir) {
    FileUtil.clean(baseDir);
    if (errorScripts.isEmpty()) {
      return;
    }
    FileUtil.clean(baseDir);

    errorScripts.forEach(
        (k, v) -> {
          v.forEach(
              s -> {
                FileUtil.copy(
                    s, baseDir + File.separator + k + File.separator + FileUtil.getName(s), true);
              });
        });
  }

  /**
   * 版本信息（git提交与构建相关）暴露给前端控制台
   */
  public static void version(){
    ClassLoader classLoader = Application.class.getClassLoader();
    URL url = classLoader.getResource("git.properties");
    List<String> list = FileUtil.readLines(url, UTF_8);
    list=list.stream().filter(
    item ->
            item.startsWith("git.branch") ||
            item.startsWith("git.commit.id=") ||
            item.startsWith("git.commit.message.full") ||
            item.startsWith("git.commit.time") ||
            item.startsWith("git.build.user.email") ||
            item.startsWith("git.build.time")
    ).collect(Collectors.toList());
    //log.info("\n"+JSONUtil.toJsonPrettyStr(list));
    System.out.println("\n"+JSONUtil.toJsonPrettyStr(list));
  }
}
