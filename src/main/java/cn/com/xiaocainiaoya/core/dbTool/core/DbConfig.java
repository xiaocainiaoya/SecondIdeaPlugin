package cn.com.xiaocainiaoya.core.dbTool.core;

import cn.hutool.core.util.StrUtil;
import lombok.*;

import java.io.Serializable;

/**
 * @author wendao76
 * @version 1.0
 * @description 类描述信息
 * @className DbConfig
 * @date 2021-6-28 16:17
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class DbConfig implements Serializable {
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String url;
	private String username;
	private String password;

	public boolean isEmptyInfo(){
		if(StrUtil.isBlank(url)){
			return true;
		}
		if(StrUtil.isBlank(username)){
			return true;
		}
		if(StrUtil.isBlank(password)){
			return true;
		}
		return false;
	}

}
