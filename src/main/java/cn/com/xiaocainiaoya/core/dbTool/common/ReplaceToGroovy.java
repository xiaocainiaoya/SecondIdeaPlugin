package cn.com.xiaocainiaoya.core.dbTool.common;

import cn.com.xiaocainiaoya.core.dbTool.Application;
import cn.com.xiaocainiaoya.core.dbTool.util.FileUtilExt;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.List;

/**
 * @author :jiangjiamin
 * @date : 2022/4/17 8:56 PM
 */
public class ReplaceToGroovy {

    public static void main(String[] args) throws FileNotFoundException {
        String sqlTemplate = FileUtilExt.readFromJar(Application.class, "/temp/temp.sql");

        File file = new File("/Users/jiangjiamin/IdeaProjects/db-script-tool/src/main/resources/temp/temp.sql");
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        String line = FileUtil.readLine(raf, CharsetUtil.defaultCharset());
        while(line != null){
            line = line.replace(");"," )\"))");
            line = line.replace("INSERT","add(SqlMeta.build(\"\", \"INSERT");

            System.out.println(line);
            line = FileUtil.readLine(raf, CharsetUtil.defaultCharset());
        }


    }

}
