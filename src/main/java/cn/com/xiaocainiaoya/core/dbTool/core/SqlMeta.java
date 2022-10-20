package cn.com.xiaocainiaoya.core.dbTool.core;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * sql元数据
 *
 * @author wendao76
 * @version 1.0
 * @description sql构建元对象
 * @className SqlMeta
 * @date 2021-6-29 11:34
 */
public class SqlMeta {
  public static SqlMeta build(String... sqls) {
    if (sqls.length < 2) {
      throw new DbException("数据脚本参数错误");
    }
    return new SqlMeta(sqls);
  }

  /**
   * 构建存储过程
   *
   * @param sqls
   * @return
   */
  public static SqlMeta buildProcedure(String... sqls) {
    return new SqlMeta(1, sqls);
  }

  /**
   * @param isMulti 是否多行
   * @param condition 条件
   * @param sql 要执行的SQL
   * @return
   */
  public static SqlMeta build(boolean isMulti, String condition, String sql) {
    return new SqlMeta(isMulti, condition, sql);
  }

  private SqlMeta(String... sqls) {
    this.preposeSql = sqls[0];
    this.sqls = Arrays.asList(Arrays.copyOfRange(sqls, 1, sqls.length));
  }

  /**
   * 单语句构建
   *
   * @param sqlType
   * @param sqls
   */
  private SqlMeta(int sqlType, String... sqls) {
    this.preposeSql = "";
    this.sqlType = sqlType;
    this.name = sqls[0];
    this.sqls = Arrays.asList(sqls[1]);
  }

  private SqlMeta(boolean isMulti, String condition, String sql) {
    this.preposeSql = condition;
    if (!isMulti) {
      this.sqls = Arrays.asList(sql);
    } else {
      this.sqls =
          Arrays.stream(sql.split("\\r?\\n"))
              .filter(StrUtil::isNotBlank)
              .collect(Collectors.toList());
    }
  }

  public String getPreposeSql() {
    return preposeSql;
  }

  public List<String> getSqls() {
    return sqls;
  }

  /** 前置sql */
  private String preposeSql;

  public String getName() {
    return name;
  }
  /** 名称 */
  private String name;

  /** 待执行sql */
  private List<String> sqls;

  public int getSqlType() {
    return sqlType;
  }

  /** sql类型（0普通1存储过程） */
  private int sqlType = 0;
}
