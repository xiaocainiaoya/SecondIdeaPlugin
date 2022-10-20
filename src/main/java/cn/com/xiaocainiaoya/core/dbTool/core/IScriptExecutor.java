package cn.com.xiaocainiaoya.core.dbTool.core;

import java.sql.SQLException;

/**
 * 脚本执行器接口
 *
 * @author wendao76
 * @version 1.0
 * @description 类描述信息
 * @className IScriptExecutor
 * @date 2021-6-28 17:40
 */
public interface IScriptExecutor {
	/**
	 * 执行脚本
	 *
	 * @param
	 * @return
	 * @description 方法描述
	 * @author lwh
	 * @create_at 2021-6-30 8:37
	 * @updated_at 2021-6-30 8:37
	 */
	String execute() throws SQLException;
}
