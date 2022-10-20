package cn.com.xiaocainiaoya.core.dbTool.common.generator;

import cn.hutool.core.io.IoUtil;

import java.io.InputStream;

/**
 * @author :jiangjiamin
 * @date : 2022/9/13 23:27
 */
public class ResourceComponent {

    public String getResourcePath(String path){
        return getClass().getClassLoader().getResource(path).getPath();
        //return getClass().getResource(path).getPath();
    }

    public InputStream getAbstractPathStream(String relatedPath) {
        //noinspection ConstantConditions
        return getClass().getClassLoader().getResourceAsStream(relatedPath);
    }

    public ClassLoader getClassLoader(){
        return getClass().getClassLoader();
    }

    public String readFile(String filePath){
        InputStream in = getAbstractPathStream(filePath);
        try {
            return IoUtil.read(in, "UTF-8");
        } finally {
            IoUtil.close(in);
        }
    }
}
