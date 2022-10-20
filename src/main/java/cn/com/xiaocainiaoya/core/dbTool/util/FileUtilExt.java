package cn.com.xiaocainiaoya.core.dbTool.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author wendao76
 * @version 1.0
 * @description 类描述信息
 * @className FileUtilExt
 * @date 2021-6-30 10:49
 */
//@Slf4j
public class FileUtilExt {
  /**
   * 读取jar内部文件
   *
   * @param mainClazz
   * @param relatePath
   * @return
   */
  public static String readFromJar(Class<?> mainClazz, String relatePath) {
    InputStream is = mainClazz.getClass().getResourceAsStream(relatePath);
    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
    BufferedReader bufferedReader = new BufferedReader(isr);
    StringBuilder stringBuilder = new StringBuilder();
    bufferedReader.lines().forEach(line -> stringBuilder.append(line).append("\r\n"));
    return stringBuilder.toString();
  }
}
