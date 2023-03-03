package cn.com.xiaocainiaoya.form;

import cn.com.xiaocainiaoya.common.FileChooserComponent;
import cn.com.xiaocainiaoya.common.MainFileUtil;
import cn.com.xiaocainiaoya.core.dbTool.Application;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorBuilder;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.GeneratorHandler;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.ServiceNameEnum;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.TemplateHandler;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.impl.DruidGeneratorHandler;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.DataConfig;
import cn.com.xiaocainiaoya.core.dbTool.common.generator.vo.StepInfoVo;
import cn.com.xiaocainiaoya.core.dbTool.core.DbConfig;
import cn.com.xiaocainiaoya.core.dbTool.util.ConfigUtil;
import cn.com.xiaocainiaoya.enums.ButtonEnum;
import cn.com.xiaocainiaoya.enums.RedioMessageEnum;
import cn.com.xiaocainiaoya.listener.RadioListener;
import cn.com.xiaocainiaoya.vo.ConfigFileInfoVo;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author :jiangjiamin
 * @date : 2022/8/31 14:37
 */
public class MainForm {
    private JPanel topPanel;
    private JPanel versionPanel;
    private JLabel versionLabel;
    private JTextField versionTextField;
    private JLabel databaseLabel;
    private JPanel databasePanel;
    private JTextField databaseTextField;
    private JTextField fileNameTextField;
    private JPanel fileNamePanel;
    private JLabel fileNameLabel;
    private JTextField authorTextField;
    private JPanel authorPanel;
    private JLabel authorLabel;
    private JPanel sqlPanel;
    private JLabel sqlLabel;
    private JTextArea sqlTextArea;

    private JPanel buttonPanel;
    private JRadioButton insertLineRadio;
    private JRadioButton batchRadio;
    private JRadioButton insertFieldRadio;
    private JRadioButton updateFieldRadio;

    private JPanel subButtonPanel;
    private JButton generatorButton;
    private JButton testButton;
    private JTabbedPane mainTabPanel;
    private JPanel generatorSqlPanel;
    private JPanel configPanel;
    private JPanel generatorSqlPathPanel;
    private JLabel generatorSqlPathLabel;
    private JTextField generatorSqlPathTextField;
    private JPanel datasourcePanel;
    private JPanel userNamePanel;
    private JPanel passowrdPanel;
    private JTextField passwordField;
    private JTextField dbUrlTextField;
    private JLabel userNameLabel;
    private JLabel passowrdLabel;
    private JLabel dbUrlLabel;
    private JPanel dbPanel;
    private JPanel scriptPathPanel;
    private JTextField scriptPathTextField;
    private JTextField customScriptTextField;
    private JTextField errorTextField;
    private JTextField sqlOutputTextField;
    private JPanel scrpytPathPanel;
    private JPanel customScriptPathPanel;
    private JLabel customScriptLabel;
    private JPanel errorPanel;
    private JLabel errorLabel;
    private JPanel sqlOutputPanel;
    private JLabel sqlOutputLabel;
    private JLabel scriptPathLabel;
    private JButton saveConfigButton;
    private JButton execButton;
    private JPanel configButtonPanel;
    private JTextField userNameField;
    private JRadioButton mulSqlRedio;
    private JRadioButton addIndexRedio;
    private JRadioButton updateIndexRedio;
    private JPanel buttonSecondPanel;
    private JPanel tipPanel;
    private JLabel tipLabel;
    private JButton generatorPathButton;
    private JButton scriptPathButton;
    private JButton customScriptButton;
    private JButton errorPathButton;
    private JButton sqlOutputButton;
    private JButton generatorNoExecButton;
    private JPanel buttonThreePanel;
    private JRadioButton createTableButton;

    private Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    public MainForm() {

        init();

        // 操作方式监听处理
        radioListenerHandle();

        // tab页切换监听
        tabListenerHandle();

        // 动态 文件名称监听
        dynamicFileNameListenerHandle();

        // 结果按钮监听
        submitButtonListener();

        // sql脚本域监听
        sqlTextAreaListener();

        // 路径选择按钮监听
        choosePathListener();

        createUIComponents();
    }

    private void init(){
        ConfigFileInfoVo configFileInfo = MainFileUtil.getConfigInfo();
        if(ObjectUtil.isEmpty(configFileInfo)){
            return ;
        }
        // 回填生成tab信息项
        setGenerateFieldByConfigInfo(configFileInfo);

        // 回填配置页信息项
        setConfigFieldByConfigInfo(configFileInfo);

        // 文件名称动态修改
        setFileNameTextField();
        insertFieldRadio.isSelected();
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    public JPanel getVersionPanel() {
        return versionPanel;
    }

    public void setVersionPanel(JPanel versionPanel) {
        this.versionPanel = versionPanel;
    }

    /**
     * 单选按钮设置互斥监听
     *
     * @author jiangjiamin
     * @date 2022/8/31 17:39:00
     * @param
     * @return: void
     */
    private void radioListenerHandle(){
        List<JRadioButton> radioButtons = Lists.newArrayList(batchRadio, insertFieldRadio, updateFieldRadio, addIndexRedio, updateIndexRedio, mulSqlRedio, createTableButton);
        RadioListener.setRadioListener(radioButtons, sqlTextArea);
    }

    /**
     * 获取执行方式编码
     *
     * @author jiangjiamin
     * @date 2022/9/2 14:36:43
     * @param
     * @return: java.lang.String
     */
    private String getRadioCodeValue(){
//        if(insertLineRadio.isSelected()){
//            return "1";
//        }
        if(batchRadio.isSelected()){
            return "2";
        }
        if(insertFieldRadio.isSelected()){
            return "3.1";
        }
        if(updateFieldRadio.isSelected()){
            return "4";
        }
        if(addIndexRedio.isSelected()){
            return "5";
        }
        if(updateIndexRedio.isSelected()){
            return "6";
        }
        if(mulSqlRedio.isSelected()){
            return "7";
        }
        if(createTableButton.isSelected()){
            return "9";
        }
        return null;
    }

    private void choosePathListener(){
        this.generatorPathButton.addActionListener(e -> {
            this.fileChoose(project, generatorSqlPathTextField);
        });

        this.scriptPathButton.addActionListener(e -> {
            this.fileChoose(project, scriptPathTextField);
        });

        this.customScriptButton.addActionListener(e -> {
            this.fileChoose(project, customScriptTextField);
        });

        this.errorPathButton.addActionListener(e -> {
            this.fileChoose(project, errorTextField);
        });

        this.sqlOutputButton.addActionListener(e -> {
            this.fileChoose(project, sqlOutputTextField);
        });
    }

    private void fileChoose(Project myProject, JTextField textField){
        FileChooserComponent component = FileChooserComponent.getInstance(myProject);
        VirtualFile baseDir = myProject.getBaseDir();

        VirtualFile virtualFile = component.showFolderSelectionDialog("选择脚本生成目录", null);
        if(ObjectUtil.isEmpty(virtualFile)){
            return ;
        }
        System.out.println(virtualFile.getPath());
        textField.setText(virtualFile.getPath());
    }


    /**
     * 结果按钮组的监听
     *
     * @author jiangjiamin
     * @date 2022/8/31 17:39:52
     * @param
     * @return: void
     */
    private void submitButtonListener(){
        // 生成并执行脚本按钮
        generatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatorSqlFile(true);
            }
        });

        // 生成不执行按钮
        generatorNoExecButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatorSqlFile(false);
            }
        });

        // 保存配置按钮
        saveConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readInputInfo(ButtonEnum.SAVE_CONFIG.getCode());
            }
        });

        // 执行脚本
        execButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //generatorButton.setEnabled(false);
                //execButton.setEnabled(false);
                //saveConfigButton.setEnabled(false);

                try{
                    ConfigFileInfoVo configFileInfoVo = readInputInfo(ButtonEnum.EXEC_.getCode());

                    if(StrUtil.isBlank(configFileInfoVo.getUsername()) || StrUtil.isBlank(configFileInfoVo.getPassword()) || StrUtil.isBlank(configFileInfoVo.getDataSourceUrl())){
                        JOptionPane.showMessageDialog(null, "数据库连接模块未填写！");
                        return ;
                    }

                    if(StrUtil.isBlank(configFileInfoVo.getScriptPath())){
                        JOptionPane.showMessageDialog(null, "[执行脚本路径]未填写路径！");
                        return ;
                    }
                    List<File> commonFiles = FileUtil.loopFiles(configFileInfoVo.getScriptPath());
                    commonFiles = Application.arrangeFiles(commonFiles);

                    String execMsg = Application.runScripts(1, commonFiles, configFileInfoVo, ConfigUtil.getScriptPath());
                    execMsg = StrUtil.format(OUT_PUT_TEMPLATE, configFileInfoVo.getScriptPath(), execMsg);
                    System.out.println(execMsg);

                    if(StrUtil.isNotBlank(configFileInfoVo.getCustomScriptPath())){
                        // 定制化脚本
                        List<File> customFiles = FileUtil.loopFiles(ConfigUtil.getCustomScriptPath());
                        customFiles = Application.arrangeFiles(customFiles);

                        String customMsg = Application.runScripts(1, customFiles, configFileInfoVo, ConfigUtil.getCustomScriptPath());
                        customMsg = StrUtil.format(OUT_PUT_TEMPLATE, configFileInfoVo.getCustomScriptPath(), customMsg);

                        //JOptionPane.showMessageDialog(null, "执行结果\n" + execMsg + customMsg);
                        //Messages.showDialog("执行结果\n" + execMsg + customMsg, "执行结果", new String[]{"好"}, 0, null);
                        notices(execMsg + "\n定制脚本执行结果：\n" +customMsg, ButtonEnum.EXEC_.getCode());
                    }else{
                        //JOptionPane.showMessageDialog(topPanel, "执行结果\n" + execMsg);
                        //Messages.showDialog("执行结果\n" + execMsg, "执行结果", new String[]{"好"}, 2, null);
                        notices(execMsg, ButtonEnum.EXEC_.getCode());
                    }
                }catch (Exception ex){
                }
            }
        });

        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String binPath = "PathManager.getBinPath() :" + PathManager.getBinPath() + "\n";
                String logPath = "PathManager.getLogPath() :" + PathManager.getLogPath() + "\n";
                String configPath = "PathManager.getConfigPath() :" + PathManager.getConfigPath() + "\n";
                String homePath = "PathManager.getHomePath() :" + PathManager.getHomePath() + "\n";
                String systemPath = "PathManager.getSystemPath() :" + PathManager.getSystemPath() + "\n";
                String optionsPath = "PathManager.getOptionsPath() :" + PathManager.getOptionsPath() + "\n";
                notices(binPath + logPath + configPath + homePath + systemPath + optionsPath, ButtonEnum.EXEC_.getCode());
            }
        });
    }

    private void generatorSqlFile(boolean runStatus){
        System.out.println(String.format("数据库版本：%s", versionTextField.getText()));
        System.out.println(String.format("数据库：%s", databaseTextField.getText()));
        System.out.println(String.format("版本修改ID：%s", fileNameTextField.getText()));
        System.out.println(String.format("负责人：%s", authorTextField.getText()));
        System.out.println(String.format("SQL:%s", sqlTextArea.getText()));
        System.out.println(String.format("选择方式：%s", getRadioCodeValue()));
        //System.out.println("触发按钮：");
        if(StrUtil.isEmpty(generatorSqlPathTextField.getText())){
            JOptionPane.showMessageDialog(null, "脚本生成路径不能为空！");
            return ;
        }
        if(StrUtil.isEmpty(getRadioCodeValue())){
            JOptionPane.showMessageDialog(null, "请选择一个执行方式！");
            return ;
        }

        DataConfig dataConfig = DataConfig.builder()
                .sourceVersion(versionTextField.getText())
                .database(databaseTextField.getText())
                .fileName(fileNameTextField.getText())
                .author(authorTextField.getText())
                .sql(sqlTextArea.getText())
                .build();
        String message = dataConfig.emptyMessage();
        if(StrUtil.isNotBlank(message)){
            //JOptionPane.showMessageDialog(null, message);
            notices(message, ButtonEnum.GENERATOR.getCode());
            return ;
        }

        DbConfig dbConfig = DbConfig.builder()
                .username(userNameField.getText())
                .password(passwordField.getText())
                .url(dbUrlTextField.getText())
                .build();

        ConfigUtil.mockReadConfig(dbConfig, scriptPathTextField.getText(), customScriptTextField.getText(), errorTextField.getText(), sqlOutputTextField.getText());

        if(ObjectUtil.isEmpty(generatorHandler)){
            GeneratorBuilder generatorBuilder = new GeneratorBuilder();
            generatorHandler = new DruidGeneratorHandler(generatorBuilder);
        }
        System.out.println("bin路径为：" + PathManager.getBinPath());
        StepInfoVo stepInfoVo = TemplateHandler.doMain(getRadioCodeValue(), dataConfig, dbConfig, generatorHandler, runStatus);
        //Messages.showDialog(stepInfoVo.getMessage(), stepInfoVo.getTitle(), new String[]{"好"}, 0, null);
        notices(stepInfoVo.getMessage(), ButtonEnum.GENERATOR.getCode());
    }

    public static String OUT_PUT_TEMPLATE = "执行脚本路径: {}  \n{}";

    /**
     * 右下角通知
     */
    public static void notice(String content){
        NotificationGroup notificationGroup = new NotificationGroup("testid", NotificationDisplayType.TOOL_WINDOW, false);
        /**
         * content :  通知内容
         * type  ：通知的类型，warning,info,error
         */
        Notification notification = notificationGroup.createNotification(content, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }

    public static void notices(String content, String buttonId){
        try{
            JTextPane jtp = new JTextPane();
            Document doc = jtp.getDocument();
            doc.insertString(doc.getLength(), content, new SimpleAttributeSet());
            JScrollPane jsp = new JScrollPane(jtp);
            if(ButtonEnum.GENERATOR.getCode().equals(buttonId)){
                jsp.setPreferredSize(new Dimension(510, 100));
            }else{
                jsp.setPreferredSize(new Dimension(490, 200));
            }

            jsp.setBorder(null);

            JOptionPane.showMessageDialog(null, jsp, "执行结果", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "弹窗执行报错：" + e.getMessage());
        }
    }

    private ConfigFileInfoVo readInputInfo(String buttonId){
        System.out.println(String.format("生成脚本路径：%s", generatorSqlPathTextField.getText()));
        System.out.println(String.format("数据库路径：%s", dbUrlTextField.getText()));
        System.out.println(String.format("用户名：%s", userNameField.getText()));
        System.out.println(String.format("密码：%s", passwordField.getText()));
        System.out.println(String.format("脚本路径:%s", scriptPathTextField.getText()));
        System.out.println(String.format("定制脚本路径：%s", customScriptTextField.getText()));
        System.out.println(String.format("错误输出路径：%s", errorTextField.getText()));
        System.out.println(String.format("输出路径:%s", sqlOutputTextField.getText()));

        ConfigFileInfoVo configFileInfo = ConfigFileInfoVo.builder()
                .generatorSqlPath(generatorSqlPathTextField.getText())
                .dataSourceUrl(dbUrlTextField.getText())
                .username(userNameField.getText())
                .password(passwordField.getText())
                .scriptPath(scriptPathTextField.getText())
                .customScriptPath(customScriptTextField.getText())
                .errorScriptPath(errorTextField.getText())
                .sqlOutputPath(sqlOutputTextField.getText())
                .build();
        if(!configFileInfo.getGeneratorSqlPath().endsWith("/")){
            configFileInfo.setGeneratorSqlPath(configFileInfo.getGeneratorSqlPath() + "/");
        }
        try{
            MainFileUtil.saveConfigInfo(configFileInfo);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        if(ButtonEnum.SAVE_CONFIG.getCode().equals(buttonId)) {
            JOptionPane.showMessageDialog(null, "配置保存成功！");
        }
        return configFileInfo;
    }

    private TemplateHandler templateHandler;

    private GeneratorHandler generatorHandler;

    private void tabListenerHandle(){
        mainTabPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ConfigFileInfoVo configFileInfo = MainFileUtil.getConfigInfo();
                if(ObjectUtil.isEmpty(configFileInfo)){
                    return ;
                }
                if(1 == mainTabPanel.getSelectedIndex()){
                    // 加载配置信息回显
                    setConfigFieldByConfigInfo(configFileInfo);
                    System.out.println("配置信息回显成功");
                }else if(0 == mainTabPanel.getSelectedIndex()){
                    // 生成信息回显
                    setGenerateFieldByConfigInfo(configFileInfo);
                }else{
                    System.out.println("无处理");
                }

            }
        });
    }

    private void dynamicFileNameListenerHandle(){
        // 版本失去焦点后触发
        versionTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                setFileNameTextField();
            }
        });
        // 数据库失去焦点后触发
        databaseTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                setFileNameTextField();
            }
        });
    }

    private void createUIComponents() {
        System.out.println("创建完成");
    }

    private void setFileNameTextField(){
        String last = fileNameTextField.getText();
        if(StrUtil.isBlank(last)){
            last = StrUtil.EMPTY;
        }else{
            String[] strs = fileNameTextField.getText().split("_");
            last = strs[strs.length - 1];
            // 描述少于四个字，我当成非描述
            if(last.length() < 3 || ServiceNameEnum.matchValue(last)){
                last = StrUtil.EMPTY;
            }
        }
        if(StrUtil.isBlank(databaseTextField.getText()) || StrUtil.isBlank(versionTextField.getText())){
            return ;
        }

        String fileName = String.format("DB_V%s_%s_%s_01_%s_%s", versionTextField.getText(),
                databaseTextField.getText().toUpperCase(), DateUtil.format(new Date(), "yyyyMMdd"),
                ServiceNameEnum.getValueByCode(databaseTextField.getText()), last);
        fileNameTextField.setText(fileName);
    }

    private void sqlTextAreaListener(){

        sqlTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //当点击输入框时，里面的内容为提示信息时，清空内容，将其字体颜色设置为正常黑色。
                if(sqlTextArea.getText().startsWith("示例")){
                    sqlTextArea.setText("");
                    sqlTextArea.setForeground(Color.white);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                //当失去焦点时，判断是否为空，若为空时，直接显示提示信息，设置颜色

                String message = RedioMessageEnum.getValueByRadio(getRadioCodeValue());
                if(StrUtil.isBlank(sqlTextArea.getText())){
                    sqlTextArea.setText(message);
                    sqlTextArea.setForeground(new Color(142,142,142));
                }
            }
        });
    }

    /**
     * 回填配置页信息项
     *
     * @author jiangjiamin
     * @date 2022/9/14 21:21:32
     * @param configFileInfo
     * @return: void
     */
    private void setConfigFieldByConfigInfo(ConfigFileInfoVo configFileInfo){
        generatorSqlPathTextField.setText(configFileInfo.getGeneratorSqlPath());
        dbUrlTextField.setText(configFileInfo.getDataSourceUrl());
        userNameField.setText(configFileInfo.getUsername());
        passwordField.setText(configFileInfo.getPassword());
        scriptPathTextField.setText(configFileInfo.getScriptPath());
        customScriptTextField.setText(configFileInfo.getCustomScriptPath());
        errorTextField.setText(configFileInfo.getErrorScriptPath());
        sqlOutputTextField.setText(configFileInfo.getSqlOutputPath());
    }

    /**
     * 回填生成tab信息项
     *
     * @author jiangjiamin
     * @date 2022/9/14 21:21:32
     * @param configFileInfo
     * @return: void
     */
    private void setGenerateFieldByConfigInfo(ConfigFileInfoVo configFileInfo){
        versionTextField.setText(configFileInfo.getSourceVersion());
        databaseTextField.setText(configFileInfo.getDatabase());
        authorTextField.setText(configFileInfo.getAuthor());
    }
}
