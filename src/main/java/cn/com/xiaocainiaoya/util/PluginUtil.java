package cn.com.xiaocainiaoya.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author :jiangjiamin
 * @date : 2023/3/21 10:36
 */
public class PluginUtil {

    /**
     * 添加反引号
     *
     * @author jiangjiamin
     * @date 2023/3/21 10:37:17
     * @param
     * @return: java.lang.String
     */
    public static String addReverseQuotes(String value){
        if(StrUtil.isEmpty(value)){
            return StrUtil.EMPTY;
        }
        if(value.startsWith("`") && value.endsWith("`")){
            return value;
        }
        if(value.startsWith("`")){
            return value + "`";
        }
        if(value.endsWith("`")){
            return "`" + value;
        }
        return "`" + value + "`";
    }

    /**
     * 移除反引号
     *
     * @author jiangjiamin
     * @date 2023/3/21 10:39:29
     * @param value
     * @return: java.lang.String
     */
    public static String removeReverseQuotes(String value){
        if(StrUtil.isEmpty(value)){
            return StrUtil.EMPTY;
        }
        return value.replace("`", "");
    }

    /**
     * 无效sql
     *
     * @author jiangjiamin
     * @date 2023/3/28 11:27:27
     * @param sql
     * @return: boolean
     */
    public static boolean invalidSql(String sql){
        if(StrUtil.isEmpty(sql)){
            return false;
        }
        if(sql.startsWith("\n")){
            sql = sql.replace("\n", "");
        }

        if(StrUtil.isEmpty(sql) || sql.startsWith("--") || sql.startsWith("/*") || sql.startsWith("\n") ){
            return false;
        }
        return true;
    }

}
