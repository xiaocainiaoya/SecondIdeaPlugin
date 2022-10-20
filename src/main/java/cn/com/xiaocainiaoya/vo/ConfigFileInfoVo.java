package cn.com.xiaocainiaoya.vo;

import lombok.*;

/**
 * @author :jiangjiamin
 * @date : 2022/9/2 10:55
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigFileInfoVo {

    private String generatorSqlPath;

    private String dataSourceUrl;

    private String username;

    private String password;

    private String scriptPath;

    private String customScriptPath;

    private String errorScriptPath;

    private String sqlOutputPath;

    // -----生成脚本信息---------

    private String sourceVersion;

    private String database;

    private String author;

}
