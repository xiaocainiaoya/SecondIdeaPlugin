package cn.com.xiaocainiaoya.core.dbTool.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 脚本执行器接口
 *
 * @author wendao76
 * @version 1.0
 * @description {@link IScriptTpl}
 * @date 2021-6-28 14:59
 */
public interface IScriptTpl extends Serializable {
  /** 存储过程 */
  Integer TYPE_PROCEDURE = 1;
  /** 默认类型 */
  Integer TYPE_DEFAULT = 0;
  /**
   * 获取版本号
   *
   * @return
   */
  String getSourceVersion();

  /**
   * 数据库名称
   *
   * @return
   */
  String getDatabase();

  /**
   * 获取变更ID
   *
   * @return
   */
  String getChangeId();

  /**
   * 作者
   *
   * @return
   */
  String getChangeUser();

  /**
   * 返回需要执行的sql数据
   *
   * @return
   */
  default List<SqlMeta> mainSql() {
    return new ArrayList<>();
  }

  /**
   * sql文件路径（与groovy脚本通目录）
   *
   * @return
   */
  default List<String> mainSqlPath() {
    return new ArrayList<>();
  }

  /**
   * 设置基础路径
   *
   * @param path
   */
  void setBasePath(String path);

  /**
   * 获取基础路径
   *
   * @return
   */
  String getBasePath();

  /**
   * 返回是否可执行判断语句<br>
   * select not exists(select 1 from <tableName> where <CONDITION>); <br>
   * select exists(select 1 from <tableName> where <CONDITION>);
   *
   * @return
   */
  String preposeSql();

  /**
   * 判断条件更新语句
   *
   * @return
   */
  String updatePreposeSql();

  /**
   * 脚本类型（0 普通脚本 1存储过程）
   *
   * @return
   */
  default Integer type() {
    return TYPE_DEFAULT;
  }

  /** 脚本测试 */
  void test();
}
