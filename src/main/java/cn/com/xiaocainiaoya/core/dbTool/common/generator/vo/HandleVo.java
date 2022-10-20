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
public class HandleVo {

    private String sql;

    private String columnName;

    /**
     * 目前仅批量插入字段时使用
     */
    private String tableName;

}
