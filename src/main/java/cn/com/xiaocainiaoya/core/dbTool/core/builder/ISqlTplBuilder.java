package cn.com.xiaocainiaoya.core.dbTool.core.builder;

import java.io.Serializable;
import java.util.Map;

/**
 * 脚本构建器
 *
 * @author wendao76
 * @version 1.0
 * @description {@link ISqlTplBuilder}
 * @date 2021-6-28 14:59
 */
public interface ISqlTplBuilder extends Serializable {
	/**
	 * 设置模板内容
	 *
	 * @param content
	 * @return
	 */
	ISqlTplBuilder setTplContent(String content);

	/**
	 * 设置模板参数
	 *
	 * @param params
	 * @return
	 */
	ISqlTplBuilder setTplParams(Map<String, String> params);

	/**
	 * 构建sql语句
	 *
	 * @return
	 */
	String build();
}
