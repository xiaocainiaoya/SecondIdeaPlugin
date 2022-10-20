package cn.com.xiaocainiaoya.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ButtonEnum {

    SAVE_CONFIG("saveConfig", "配置保存按钮"),
    EXEC_("exec", "执行按钮"),
    GENERATOR("generator", "生成按钮"),


    ;
    private String code;
    private String value;

}
