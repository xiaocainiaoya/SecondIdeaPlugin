package cn.com.xiaocainiaoya.core.dbTool.common.generator.vo;

import lombok.*;

/**
 * @author :jiangjiamin
 * @date : 2022/4/20 5:41 PM
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteFieldHandleVo {

    private String fullTableName;

    private String tableName;

    private String columnName;

    private String field;

}
