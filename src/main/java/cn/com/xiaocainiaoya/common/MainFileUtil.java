package cn.com.xiaocainiaoya.common;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.com.xiaocainiaoya.vo.ConfigFileInfoVo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.application.PathManager;

import java.io.File;

/**
 * @author :jiangjiamin
 * @date : 2022/9/2 10:54
 */
//@Slf4j
public class MainFileUtil {

    public static String CONFIG_PATH = "/configFile.txt";

    public static String DIR = "/generatorConfig";

    public static File getConfigFile(){
        String configFilePath = PathManager.getConfigPath();
//        try {
//            configFilePath = new File("").getCanonicalPath();
//        }catch (Exception e){
//            throw new RuntimeException(e.getMessage());
//        }

        File configFileDir = new File(configFilePath + DIR);
        if(!configFileDir.exists()){
            configFileDir.mkdir();
        }
        File configFile = new File(configFilePath + DIR + CONFIG_PATH);
        if(configFile.exists()){
            return configFile;
        }
        return FileUtil.newFile(configFilePath + DIR + CONFIG_PATH);
    }

    public static String getConfigPath(){
        return PathManager.getConfigPath() + DIR + CONFIG_PATH;
    }

    public static void mkdirConfig(){
        File configFileDir = new File(PathManager.getConfigPath() + DIR);
        if(!configFileDir.exists()){
            configFileDir.mkdir();
        }
    }

    /**
     * 获取配置信息
     *
     * @author jiangjiamin
     * @date 2022/9/2 11:25:30
     * @param
     * @return: cn.com.xiaocainiaoya.vo.ConfigFileInfoVo
     */
    public static ConfigFileInfoVo getConfigInfo(){
        //File configFile = MainFileUtil.getConfigFile();

//        InputStream in = GeneratorUtils.resourceComponent.getAbstractPathStream("configInfo.json");
//        String json = "";
//        try {
//            json = IoUtil.read(in, "UTF-8");
//        } finally {
//            IoUtil.close(in);
//        }
//        JOptionPane.showMessageDialog(null, PathManager.getConfigPath() + "::" + PathManager.getHomePath() + "::"
//        + PathManager.getSystemPath() + "::" + PathManager.getOptionsPath() + "::" + PathManager.getSystemPath() + "::" + PathManager.getLogPath());

        MainFileUtil.mkdirConfig();
        String configFilePath = MainFileUtil.getConfigPath(); //FileUtil.getAbsolutePath(new File("")) + CONFIG_PATH;
        File configFile = new File(configFilePath);
        if(!FileUtil.isFile(configFile)){
            try{
                configFile.createNewFile();
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
        System.out.println(String.format("配置文件路径: %s", configFile.getAbsolutePath()));
        String json = FileUtil.readString(configFilePath, "UTF-8");

        if(StrUtil.isBlank(json)){
            return ConfigFileInfoVo.builder().build();
        }
        return JSONObject.parseObject(json, ConfigFileInfoVo.class);
    }

    /**
     * 保存配置信息
     *
     * @author jiangjiamin
     * @date 2022/9/2 11:25:21
     * @param configFileInfo
     * @return: void
     */
    public static void saveConfigInfo(ConfigFileInfoVo configFileInfo){
        ConfigFileInfoVo oldConfigFileInfo = MainFileUtil.getConfigInfo();

        BeanUtil.copyProperties(configFileInfo, oldConfigFileInfo, CopyOptions.create().setIgnoreNullValue(true));

        String configFilePath = MainFileUtil.getConfigPath(); //FileUtil.getAbsolutePath(new File("")) + CONFIG_PATH;
        if(!FileUtil.isFile(configFilePath)){
            FileUtil.newFile(configFilePath);
        }
        FileUtil.writeString(JSONObject.toJSONString(oldConfigFileInfo), configFilePath, "UTF-8");
    }

    public static void setConfigToSourceConfig(DataConfig dataConfig){
        ConfigFileInfoVo oldConfigFileInfo = MainFileUtil.getConfigInfo();
        System.out.println();

        dataConfig.setWritePath(oldConfigFileInfo.getGeneratorSqlPath());
        dataConfig.setTemplatePath("template");

        // 如果生成tab的信息有不一样，保存一次
        if(StrUtil.equals(oldConfigFileInfo.getSourceVersion(), dataConfig.getSourceVersion())
                && StrUtil.equals(oldConfigFileInfo.getDatabase(), dataConfig.getDatabase())
                && StrUtil.equals(oldConfigFileInfo.getAuthor(), dataConfig.getAuthor())) {
            return;
        }

        oldConfigFileInfo.setSourceVersion(dataConfig.getSourceVersion());
        oldConfigFileInfo.setDatabase(dataConfig.getDatabase());
        oldConfigFileInfo.setAuthor(dataConfig.getAuthor());
        MainFileUtil.saveConfigInfo(oldConfigFileInfo);

    }

}
