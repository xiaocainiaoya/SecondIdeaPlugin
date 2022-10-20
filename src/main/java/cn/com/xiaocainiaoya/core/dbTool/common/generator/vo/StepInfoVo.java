package cn.com.xiaocainiaoya.core.dbTool.common.generator.vo;

import cn.com.xiaocainiaoya.enums.StepCodeEnum;
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
public class StepInfoVo {

    private String message;

    private String title;

    private StepCodeEnum stepCodeEnum;

}
