package cn.com.xiaocainiaoya.core.parse.vo;

import lombok.*;

import java.util.List;

/**
 * @author :jiangjiamin
 * @date : 2022/10/18 15:05
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableDetailInfo {

    private String table;

    private List<String> columnNames;

}
