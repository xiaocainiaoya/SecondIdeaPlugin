package cn.com.xiaocainiaoya.core.dbTool.core.builder;

import cn.com.xiaocainiaoya.core.dbTool.core.DbException;
import cn.com.xiaocainiaoya.core.dbTool.core.IScriptTpl;
import cn.com.xiaocainiaoya.core.dbTool.core.SqlMeta;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * sql构建器
 *
 * @author wendao76
 * @version 1.0
 * @description 类描述信息
 * @className SqlBuilder
 * @date 2021-6-30 11:12
 */
public class SqlBuilder implements ISqlTplBuilder {
  /** 空格 */
  static final String BLANK_2 = " ", BLANK_4 = "    ", END_TAG = ";", LINE_BREAK = "\t\n";

  private static SqlBuilder instance;

  public static SqlBuilder getInstance() {
    if (instance == null) {
      synchronized (SqlBuilder.class) {
        instance = new SqlBuilder();
      }
    }
    return instance;
  }

  private SqlBuilder() {}

  @Getter String tplContent;
  @Getter Map<String, String> tplParams;
  IScriptTpl scriptTpl;

  @Override
  public SqlBuilder setTplContent(String content) {
    this.tplContent = content;
    return this;
  }

  @Override
  public SqlBuilder setTplParams(Map<String, String> params) {
    this.tplParams = params;
    return this;
  }

  public SqlBuilder setScriptTpl(IScriptTpl scriptTpl) {
    this.scriptTpl = scriptTpl;
    return this;
  }

  @Override
  public String build() {
    if (scriptTpl == null && tplParams == null) {
      throw new DbException("无法生成sql文件，参数无法满足要求");
    }
    tplParams = new HashMap<>(10);
    buildParams(tplParams, scriptTpl);
    return StrUtil.format(tplContent, tplParams);
  }

  /**
   * 构建参数
   *
   * @param params
   * @param script
   */
  private void buildParams(Map<String, String> params, IScriptTpl script) {
    // --------------其他参数构建--------------------
    params.put("sourceVersion", script.getSourceVersion());
    params.put("changeId", script.getChangeId());
    params.put("changeUser", script.getChangeUser());
    params.put("database", script.getDatabase());
    // ----------------构建sql主体-------------------
    StringBuilder sb = new StringBuilder();
    sb.append(BLANK_2).append(script.preposeSql().replaceFirst("SELECT", "IF")).append(" THEN\r\n");

    // sql语句构建
    buildFromMain(script.mainSql(), sb);
    buildFromFile(script.mainSqlPath(), sb);

    sb.append(BLANK_4).append(script.updatePreposeSql()).append(";\r\n");
    sb.append(BLANK_2).append("END IF").append(";\r\n");
    params.put("sqlBody", sb.toString());
  }

  /**
   * 从sql主体构建sql
   *
   * @param sqlMetas
   * @param sb
   */
  public void buildFromMain(List<SqlMeta> sqlMetas, StringBuilder sb) {
    SqlMeta sqlMeta;
    for (int i = 0; i < sqlMetas.size(); i++) {
      boolean hasPrepose = false;
      sqlMeta = sqlMetas.get(i);
      if (StrUtil.isNotBlank(sqlMeta.getPreposeSql())) {
        hasPrepose = true;
        sb.append(BLANK_4)
            .append(sqlMeta.getPreposeSql().replaceFirst("select|SELECT", "IF"))
            .append(" THEN")
            .append(LINE_BREAK);
      }
      if (hasPrepose) {
        sb.append(BLANK_4);
      }
      sqlMeta
          .getSqls()
          .forEach(
              s -> {
                String sNew = s.trim();
                sb.append(BLANK_4).append(s);
                // 无封号补充一下
                if (!sNew.endsWith(END_TAG)) {
                  sb.append(END_TAG).append(LINE_BREAK);
                }
              });
      if (hasPrepose) {
        sb.append(BLANK_4).append("END IF").append(END_TAG).append(LINE_BREAK);
      }
    }
  }

  /**
   * 从sql当中
   *
   * @param sqlPaths
   * @param sb
   */
  public void buildFromFile(List<String> sqlPaths, StringBuilder sb) {
    List<String> sqlLines = new ArrayList<>();
    List<String> sqlLineApart = new ArrayList<>();
    for (String sqlPath : sqlPaths) {
      sqlLineApart =
          FileUtil.readLines(
              scriptTpl.getBasePath() + File.separator + sqlPath, StandardCharsets.UTF_8);
      sqlLines.addAll(
          sqlLineApart.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList()));
    }

    sqlLines.forEach(
        s -> {
          String sNew = s.trim();
          sb.append(BLANK_4).append(s);
          // 无封号补充一下
          if (!sNew.endsWith(END_TAG)) {
            sb.append(END_TAG).append(LINE_BREAK);
          }
        });
  }
}
