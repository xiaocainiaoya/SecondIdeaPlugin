package cn.com.xiaocainiaoya.core.parse.visitor.vo;

import lombok.*;

import java.util.List;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 16:07
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCreateTableVo {

    private String renderFullTableName;

    private String fullTableName;

    private String tableName;

    private String database;

    private List<TableKey> primaryKeys;

    private List<TableKey> otherKeys;

    private List<String> fields;

    private String tableComment;
}
