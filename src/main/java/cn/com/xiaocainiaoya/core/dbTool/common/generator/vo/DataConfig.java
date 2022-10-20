package cn.com.xiaocainiaoya.core.dbTool.common.generator.vo;

import cn.hutool.core.util.StrUtil;
import lombok.*;

/**
 * @author :jiangjiamin
 * @date : 2022/4/20 5:41 PM
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataConfig {

    private String sourceVersion;

    private String database;

    private String fileName;

    private String author;

    private String sql;

    private String templatePath;

    private String writePath;

    public String emptyMessage(){
        if(StrUtil.isBlank(sourceVersion)){
            return "请输入[数据库版本]！";
        }
        if(StrUtil.isBlank(database)){
            return "请输入[数据库]！";
        }
        if(StrUtil.isBlank(fileName)){
            return "请输入[版本修改ID]！";
        }
        if(StrUtil.isBlank(author)){
            return "请输入[负责人]！";
        }
        if(StrUtil.isBlank(sql)){
            return "请输入[SQL]！";
        }
        return StrUtil.EMPTY;
    }
}
