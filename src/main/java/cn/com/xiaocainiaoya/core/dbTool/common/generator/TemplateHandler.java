package cn.com.xiaocainiaoya.core.dbTool.common.generator;

import cn.com.xiaocainiaoya.common.MainFileUtil;
import cn.com.xiaocainiaoya.core.dbTool.Application;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.StepInfoVo;
import cn.com.xiaocainiaoya.core.dbTool.core.DbConfig;
import cn.com.xiaocainiaoya.enums.StepCodeEnum;
import cn.hutool.core.util.StrUtil;

import java.util.Scanner;

/**
 * @author :jiangjiamin
 * @date : 2022/4/24 10:02 AM
 */
//@Slf4j
public class TemplateHandler {

    private static String SQL = "sql";

    private static String SQL_LIST = "sqlList";

    public static String FILE_DESC = "fileDesc";

    public static String DEL_SQL = "delSql";

    public static String TABLE_NAME = "tableName";

    public static String COLUMN_NAME = "columnName";

    //private GeneratorBuilder generatorBuilder;

    private GeneratorHandler generatorHandler;

    public TemplateHandler(){
        //generatorHandler = new DruidGeneratorHandler();
    }



    /**
     * 空文件处理
     *
     * @author jiangjiamin
     * @date 2022/4/24 11:04:11
     * @param
     * @return: void
     */
//    public String other(DataConfig config){
//        Map<String, Object> dataMap = MapUtil.of(SQL, StrUtil.EMPTY);
//        dataMap.put(FILE_DESC, config.getFileName());
//        return generatorBuilder.render("insertData.vm", dataMap, config);
//    }

    public static void menu(){
        System.out.println("================================");
        System.out.println("1:插入(修改)单行数据；");
        System.out.println("2:批量插入数据；");
        System.out.println("3:插入字段；");
        System.out.println("3.1:批量插入字段[内测版]；");
        System.out.println("4:修改字段长度、字段类型等；");
        System.out.println("5:添加索引；");
        System.out.println("6:修改索引；");
        System.out.println("7:多行SQL；");
        System.out.println("其他任意键生成空脚本的.groovy文件");
        System.out.println("================================");
    }


    public static void main(String[] args) {
        menu();

        Scanner scanner = new Scanner(System.in);
        System.out.println("输入功能序号：");
        String input = scanner.nextLine();
        System.out.println("输入脚本作用描述：");
        String desc = scanner.nextLine();

        //doMain(args, input, desc);

    }

    //String path = TemplateHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    //System.out.println(TemplateHandler.class.getClassLoader().getResource("").getPath());

//        System.out.println(PathManager.getAbsolutePath("template/insertData.vm"));
//        System.out.println(PathManager.getResourceRoot(TemplateHandler.class, "template/insertData.vm"));
//        System.out.println(PathManager.getConfigPath());
//        System.out.println(TemplateHandler.class.getClassLoader().getResource("/"));

    //ResourceUtil.readWithoutLr("/api-doc/template.vm");
    //ResourceUtil.readAsString(path).replace(StringConstants.CR_LF, StringConstants.LF)

//        TemplateHandler templateHandler = new TemplateHandler();
//        templateHandler.test();

    //System.out.println(LocalFileSystem.getInstance().findFileByIoFile());


    /**
     * 0: 仅生成脚本
     * 1: 生成了脚本并且执行了脚本
     *
     * @author jiangjiamin
     * @date 2022/9/14 16:25:01
     * @param input
     * @param input
     * @param dataConfig
     * @return: int
     */
    public static StepInfoVo doMain(String input, DataConfig dataConfig, DbConfig dbConfig, GeneratorHandler generatorHandler, boolean runStatus){
        String destPath = null;

        MainFileUtil.setConfigToSourceConfig(dataConfig);

        //TemplateHandler templateHandler = new TemplateHandler();

        try{
            switch (input){
                case "1":
                    destPath = generatorHandler.insertData(dataConfig);
                    break;
                case "2":
                    destPath = generatorHandler.insertBatchData(dataConfig);
                    break;
                case "3":
                    //destPath = generatorHandler.insertField(dataConfig);
                    break;
                case "3.1":
                    destPath = generatorHandler.insertFieldBatch(dataConfig);
                    break;
                case "4":
                    destPath = generatorHandler.updateFieldLength(dataConfig);
                    break;
                case "5":
                    destPath = generatorHandler.insertIndex(dataConfig);
                    break;
                case "6":
                    destPath = generatorHandler.updateIndex(dataConfig);
                    break;
                case "7":
                    destPath = generatorHandler.multilineSql(dataConfig);
                    break;
                case "8":
                    //destPath = generatorHandler.createTableSqlByMd();
                    break;
                default:
                    destPath = generatorHandler.other(dataConfig);
            }
        }catch (Exception e){
            e.printStackTrace();
            return StepInfoVo.builder()
                    .message(StrUtil.format(StepCodeEnum.GENERATOR_ERROR.getValue(), e.getMessage()))
                    .title(StepCodeEnum.GENERATOR_ERROR.getTitle())
                    .build();
        }
        System.out.println("生成目标文件: " + destPath);

        // 不执行脚本
        if(!runStatus){
            return StepInfoVo.builder()
                    .message(StrUtil.format(StepCodeEnum.GENERATOR_NO_EXEC.getValue(), destPath))
                    .title(StepCodeEnum.GENERATOR_NO_EXEC.getTitle())
                    .build();
        }

        if(StrUtil.isBlank(destPath)){
            System.out.println("生成脚本目标地址为空，故不执行脚本");
            return StepInfoVo.builder()
                    .message(StrUtil.format(StepCodeEnum.GENERATOR_NO_EXEC.getValue(), dataConfig.getWritePath()))
                    .title(StepCodeEnum.GENERATOR_NO_EXEC.getTitle())
                    .build();
        }
        if(dbConfig.isEmptyInfo()){
            System.out.println("未配置执行脚本相关配置信息，故不执行脚本");
            return StepInfoVo.builder()
                    .message(StrUtil.format(StepCodeEnum.GENERATOR_NO_EXEC.getValue(), dataConfig.getWritePath()))
                    .title(StepCodeEnum.GENERATOR_NO_EXEC.getTitle())
                    .build();
        }
        String result = runScript(destPath);
        if(!StrUtil.isBlank(result)){
            return StepInfoVo.builder()
                    .message(StrUtil.format(StepCodeEnum.GENERATOR_AND_FIRST_EXEC_ERROR.getValue(), dataConfig.getWritePath(), result))
                    .title(StepCodeEnum.GENERATOR_AND_FIRST_EXEC_ERROR.getTitle())
                    .build();
        }

        result = runScript(destPath);
        if(!StrUtil.isBlank(result)){
            return StepInfoVo.builder()
                    .message(StrUtil.format(StepCodeEnum.GENERATOR_AND_SECOND_EXEC_ERROR.getValue(), dataConfig.getWritePath(), result))
                    .title(StepCodeEnum.GENERATOR_AND_SECOND_EXEC_ERROR.getTitle())
                    .build();
        }

        return StepInfoVo.builder()
                .message(StrUtil.format(StepCodeEnum.GENERATOR_AND_EXEC.getValue(), dataConfig.getWritePath()))
                .title(StepCodeEnum.GENERATOR_AND_EXEC.getTitle())
                .build();
    }

    public static String runScript(String destPath){
        try{
            Application.runSingleScriptByFilePath(destPath, null);
            return StrUtil.EMPTY;
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void test(){
//        ResourceComponent component = new ResourceComponent();
//        org.apache.velocity.app.VelocityEngine vmEngine = new org.apache.velocity.app.VelocityEngine();
//        Properties properties = new Properties();
//        try{
//            properties.load(component.getAbstractPathStream("velocity.properties"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        //  override path attribute
//        properties.put("file.resource.loader.path", "");
//        vmEngine.init(properties);
//        String str = component.getResourcePath("template/insertData.vm");
//        Template t = (Template) vmEngine.getTemplate(str, "UTF-8");

        AutoGenerator autoGenerator = new AutoGenerator();
        try{
            ResourceComponent component = new ResourceComponent();
            autoGenerator.vmToFile(null, "insertData.vm", null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
