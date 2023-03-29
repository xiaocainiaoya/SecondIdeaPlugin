package cn.com.xiaocainiaoya.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {

    COMMENT_IS_EMPTY("", "你干嘛不写表注释啊！！！给我把表注释写上！！！"),


    ;
    private String code;
    private String value;

}
