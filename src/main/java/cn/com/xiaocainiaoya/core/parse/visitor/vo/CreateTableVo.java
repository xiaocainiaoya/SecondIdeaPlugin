package cn.com.xiaocainiaoya.core.parse.visitor.vo;

import lombok.*;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 16:07
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTableVo {

    private String fullTableName;

    private String tableName;

    private String database;

    private String sql;
}
