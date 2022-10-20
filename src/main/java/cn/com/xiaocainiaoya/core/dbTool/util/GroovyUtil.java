package cn.com.xiaocainiaoya.core.dbTool.util;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.ResourceComponent;
import cn.com.xiaocainiaoya.core.dbTool.core.DbException;
import cn.com.xiaocainiaoya.core.dbTool.core.IScriptTpl;
import cn.hutool.core.io.FileUtil;
import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wendao76
 * @version 1.0
 * @description 类描述信息
 * @className GroovyUtil
 * @date 2021-6-28 15:14
 */
//@Slf4j
public class GroovyUtil {
  static GroovyClassLoader classLoader;

  static {
    classLoader = new GroovyClassLoader(new ResourceComponent().getClassLoader());
  }

  private GroovyUtil() {}

  /**
   * 从文件当中加载脚本
   *
   * @param file
   * @return
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public static IScriptTpl loadScript(File file)
      throws IllegalAccessException, InstantiationException {
    List<String> scriptLines = FileUtil.readLines(file, StandardCharsets.UTF_8);
    StringBuilder sb = new StringBuilder();
    sb.append("import cn.com.xiaocainiaoya.core.dbTool.core.IScriptTpl").append("\r\n");
    sb.append("import cn.com.xiaocainiaoya.core.dbTool.core.AbstractScriptTpl").append("\r\n");
    sb.append("import cn.com.xiaocainiaoya.core.dbTool.core.SqlMeta").append("\r\n");
    scriptLines.forEach(
        s -> {
          s = s.replaceAll("\\\\", "\\\\\\\\\\\\");
          sb.append(s).append("\r\n");
        });

    Class<?> gClazz = classLoader.parseClass(sb.toString());
    if (gClazz == null) {
      throw new DbException("类加载失败");
    }
    Object obj = gClazz.newInstance();
    if (obj instanceof IScriptTpl) {
      return (IScriptTpl) obj;
    }
    return null;
  }

  /** 清空脚本缓存 */
  public static void clearCache() {
    classLoader.clearCache();
  }
}
