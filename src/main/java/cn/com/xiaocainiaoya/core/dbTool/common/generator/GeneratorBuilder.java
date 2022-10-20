package cn.com.xiaocainiaoya.core.dbTool.common.generator;

import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GeneratorBuilder {

    public ResourceComponent resourceComponent;

    private VelocityEngine engine;

    public ResourceComponent getResourceComponent() {
        return resourceComponent;
    }

    public GeneratorBuilder(){
        resourceComponent = new ResourceComponent();
        engine = new VelocityEngine();
        Properties properties = new Properties();
        try{
            properties.load(resourceComponent.getAbstractPathStream("velocity.properties"));
        }catch (Exception e){
            e.printStackTrace();
        }
        //  override path attribute
        properties.put("file.resource.loader.path", "");
        engine.init(properties);
    }

    //String templateContent = FileUtil.readString(templateContentIs, "UTF-8");

    //TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(config.getTemplatePath(), TemplateConfig.ResourceMode.FILE));
    //TemplateConfig templateConfig = new TemplateConfig("/template", TemplateConfig.ResourceMode.FILE);
    //templateConfig.setCustomEngine(VelocityEngine.class);
    //TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
    //Template template = engine.getTemplate(resourceComponent.getResourcePath("template/" + templatePath));

//        org.apache.velocity.app.VelocityEngine vmEngine = new org.apache.velocity.app.VelocityEngine();
//        Properties properties = new Properties();
//        try{
//            properties.load(resourceComponent.getAbstractPathStream("velocity.properties"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        //  override path attribute
//        properties.put("file.resource.loader.path", "");
//        vmEngine.init(properties);

    //Template t = (Template) vmEngine.getTemplate(resourceComponent.getResourcePath("template/" + templatePath), "UTF-8");




    /**
     * 读取控制台单行数据
     *
     * @author jiangjiamin
     * @date 2022/4/24 15:26:45
     * @param
     * @return: java.lang.String
     */
    public static String scannerOneLine() {
        Scanner input = new Scanner(System.in);
        String str = input.nextLine();
        return str.trim();
    }

    /**
     * 读取控制台多行数据
     *
     * @author jiangjiamin
     * @date 2022/4/24 15:27:01
     * @param
     * @return: java.util.List<java.lang.String>
     */
    public static List<String> scannerMoreLine(DataConfig config){
        if(ObjectUtil.isNotEmpty(config)){
            return StrUtil.split(config.getSql(), '\n');
        }
        Scanner input = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        while (input.hasNextLine()){
            String value = input.nextLine();
            if(StringUtils.isBlank(value)){
                break;
            }
            list.add(value.trim());
        }
        return list;
    }


    /**
     * 渲染并写入文件
     *
     * @author jiangjiamin
     * @date 2022/4/24 15:27:26
     * @param templatePath
     * @param dataMap
     * @return: void
     */
    public String render(String templatePath, Map<String, Object> dataMap, DataConfig config){
        return render(templatePath, dataMap, true, config);
    }

    public String render(String templatePath, Map<String, Object> dataMap, boolean generatorFile, DataConfig config){
        //DataConfig config = GeneratorUtils.getConfig();

        InputStream in = resourceComponent.getAbstractPathStream( "template/" + templatePath);
        String templateContent = "";
        try {
            templateContent = IoUtil.read(in, "UTF-8");
        } finally {
            IoUtil.close(in);
        }
        Map<String, Object> map = new HashMap<>(BeanUtil.beanToMap(config));
        map.putAll(dataMap);
        String destPath = null;
        if(generatorFile){
            //map.put("fileName", assembleFileName(config, dataMap.get(TemplateHandler.FILE_DESC).toString()));
            map.put("fileName", dataMap.get(TemplateHandler.FILE_DESC));
            destPath = config.getWritePath() + map.get("fileName") + ".groovy";
        }

        VelocityContext velocityContext = new VelocityContext(map);

        try(StringWriter writer = new StringWriter()){
            engine.evaluate(velocityContext, writer, "", templateContent);
            String content = writer.toString();
            System.out.println(content);
            if(StrUtil.isBlank(destPath)){
                System.out.println("无文件路径不生成文件");
                return null;
            }
            FileUtil.writeString(content, destPath, "UTF-8");
            return destPath;
        }catch (IOException e){
            e.printStackTrace();
        }
        return destPath;
    }
}
