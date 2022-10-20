package cn.com.xiaocainiaoya.core.dbTool.common.generator;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceNameEnum {

    GP_GPFA("gp-gpfa","框采服务"),
    GPX_BIDCONFIRM("gpx_bidconfirm","定标服务"),
    GPX_BASIC("gpx_basic","基础服务"),
    GPX_AUCTION("gpx_auction","网拍服务"),
    GPX_ARCHIVE("gpx_archive","档案服务"),
    GPX_CONTRACT("gpx_contract","合同服务"),

    GPE_EVALUATION("gpe_evaluation","评标服务"),

    ;
    private String code;
    private String value;

    public static String getValueByCode(String code){
        for(ServiceNameEnum serviceNameEnum : ServiceNameEnum.values()){
            if(serviceNameEnum.getCode().equals(code)){
                return serviceNameEnum.getValue();
            }
        }
        return StrUtil.EMPTY;
        //throw new RuntimeException("未找到对应的服务名称");
    }

    public static boolean matchValue(String value){
        for(ServiceNameEnum serviceNameEnum : ServiceNameEnum.values()){
            if(serviceNameEnum.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }
}
