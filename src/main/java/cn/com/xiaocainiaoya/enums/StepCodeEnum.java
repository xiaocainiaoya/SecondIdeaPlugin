package cn.com.xiaocainiaoya.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StepCodeEnum {

    GENERATOR_NO_EXEC(0,"生成.groovy文件到目录[{}]成功,但未配置执行脚本相关信息故不执行脚本。\n", "脚本生成成功"),
    GENERATOR_AND_EXEC(1,"生成.groovy文件到目录[{}]成功,且重复执行两次成功\n", "脚本生成成功"),

    GENERATOR_AND_FIRST_EXEC_ERROR(2,"生成.groovy文件到目录[{}]成功,首次执行脚本失败: \n[{}]", "脚本生成成功，首次执行失败"),
    GENERATOR_AND_SECOND_EXEC_ERROR(3,"生成.groovy文件到目录[{}]成功,重复执行脚本失败: \n[{}]", "脚本生成成功，首次执行成功，重复执行失败"),
    GENERATOR_ERROR(9,"生成脚本失败：\n[{}]", "脚本生成成功，首次执行成功，重复执行失败"),

    //DATA_BASE_IS_EMPTY(10,"数据库为空", "数据库为空"),




    ;
    private Integer code;
    private String value;
    private String title;

    public static String getValueByCode(String code){
        for(StepCodeEnum serviceNameEnum : StepCodeEnum.values()){
            if(serviceNameEnum.getCode().equals(code)){
                return serviceNameEnum.getValue();
            }
        }
        return StrUtil.EMPTY;
        //throw new RuntimeException("未找到对应的服务名称");
    }

    public static boolean matchValue(String value){
        for(StepCodeEnum serviceNameEnum : StepCodeEnum.values()){
            if(serviceNameEnum.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }
}
