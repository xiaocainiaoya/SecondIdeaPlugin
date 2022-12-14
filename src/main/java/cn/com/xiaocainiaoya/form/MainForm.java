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

    private Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    public MainForm() {

        init();

        // ????????????????????????
        radioListenerHandle();

        // tab???????????????
        tabListenerHandle();

        // ?????? ??????????????????
        dynamicFileNameListenerHandle();

        // ??????????????????
        submitButtonListener();

        // sql???????????????
        sqlTextAreaListener();

        // ????????????????????????
        choosePathListener();

        createUIComponents();
    }

    private void init(){
        ConfigFileInfoVo configFileInfo = MainFileUtil.getConfigInfo();
        if(ObjectUtil.isEmpty(configFileInfo)){
            return ;
        }
        // ????????????tab?????????
        setGenerateFieldByConfigInfo(configFileInfo);

        // ????????????????????????
        setConfigFieldByConfigInfo(configFileInfo);

        // ????????????????????????
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
     * ??????????????????????????????
     *
     * @author jiangjiamin
     * @date 2022/8/31 17:39:00
     * @param
     * @return: void
     */
    private void radioListenerHandle(){
        List<JRadioButton> radioButtons = Lists.newArrayList(batchRadio, insertFieldRadio, updateFieldRadio, addIndexRedio, updateIndexRedio, mulSqlRedio);
        RadioListener.setRadioListener(radioButtons, sqlTextArea);
    }

    /**
     * ????????????????????????
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

        VirtualFile virtualFile = component.showFolderSelectionDialog("????????????????????????", null);
        if(ObjectUtil.isEmpty(virtualFile)){
            return ;
        }
        System.out.println(virtualFile.getPath());
        textField.setText(virtualFile.getPath());
    }


    /**
     * ????????????????????????
     *
     * @author jiangjiamin
     * @date 2022/8/31 17:39:52
     * @param
     * @return: void
     */
    private void submitButtonListener(){
        // ???????????????????????????
        generatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatorSqlFile(true);
            }
        });

        // ?????????????????????
        generatorNoExecButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatorSqlFile(false);
            }
        });

        // ??????????????????
        saveConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readInputInfo(ButtonEnum.SAVE_CONFIG.getCode());
            }
        });

        // ????????????
        execButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //generatorButton.setEnabled(false);
                //execButton.setEnabled(false);
                //saveConfigButton.setEnabled(false);

                try{
                    ConfigFileInfoVo configFileInfoVo = readInputInfo(ButtonEnum.EXEC_.getCode());

                    if(StrUtil.isBlank(configFileInfoVo.getUsername()) || StrUtil.isBlank(configFileInfoVo.getPassword()) || StrUtil.isBlank(configFileInfoVo.getDataSourceUrl())){
                        JOptionPane.showMessageDialog(null, "?????????????????????????????????");
                        return ;
                    }

                    if(StrUtil.isBlank(configFileInfoVo.getScriptPath())){
                        JOptionPane.showMessageDialog(null, "[??????????????????]??????????????????");
                        return ;
                    }
                    List<File> commonFiles = FileUtil.loopFiles(configFileInfoVo.getScriptPath());
                    commonFiles = Application.arrangeFiles(commonFiles);

                    String execMsg = Application.runScripts(1, commonFiles, configFileInfoVo, ConfigUtil.getScriptPath());
                    execMsg = StrUtil.format(OUT_PUT_TEMPLATE, configFileInfoVo.getScriptPath(), execMsg);
                    System.out.println(execMsg);

                    if(StrUtil.isNotBlank(configFileInfoVo.getCustomScriptPath())){
                        // ???????????????
                        List<File> customFiles = FileUtil.loopFiles(ConfigUtil.getCustomScriptPath());
                        customFiles = Application.arrangeFiles(customFiles);

                        String customMsg = Application.runScripts(1, customFiles, configFileInfoVo, ConfigUtil.getCustomScriptPath());
                        customMsg = StrUtil.format(OUT_PUT_TEMPLATE, configFileInfoVo.getCustomScriptPath(), customMsg);

                        //JOptionPane.showMessageDialog(null, "????????????\n" + execMsg + customMsg);
                        //Messages.showDialog("????????????\n" + execMsg + customMsg, "????????????", new String[]{"???"}, 0, null);
                        notices(execMsg + "\n???????????????????????????\n" +customMsg, ButtonEnum.EXEC_.getCode());
                    }else{
                        //JOptionPane.showMessageDialog(topPanel, "????????????\n" + execMsg);
                        //Messages.showDialog("????????????\n" + execMsg, "????????????", new String[]{"???"}, 2, null);
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
        System.out.println(String.format("??????????????????%s", versionTextField.getText()));
        System.out.println(String.format("????????????%s", databaseTextField.getText()));
        System.out.println(String.format("????????????ID???%s", fileNameTextField.getText()));
        System.out.println(String.format("????????????%s", authorTextField.getText()));
        System.out.println(String.format("SQL:%s", sqlTextArea.getText()));
        System.out.println(String.format("???????????????%s", getRadioCodeValue()));
        //System.out.println("???????????????");
        if(StrUtil.isEmpty(generatorSqlPathTextField.getText())){
            JOptionPane.showMessageDialog(null, "?????????????????????????????????");
            return ;
        }
        if(StrUtil.isEmpty(getRadioCodeValue())){
            JOptionPane.showMessageDialog(null, "??????????????????????????????");
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
        System.out.println("bin????????????" + PathManager.getBinPath());
        StepInfoVo stepInfoVo = TemplateHandler.doMain(getRadioCodeValue(), dataConfig, dbConfig, generatorHandler, runStatus);
        //Messages.showDialog(stepInfoVo.getMessage(), stepInfoVo.getTitle(), new String[]{"???"}, 0, null);
        notices(stepInfoVo.getMessage(), ButtonEnum.GENERATOR.getCode());
    }

    public static String OUT_PUT_TEMPLATE = "??????????????????: {}  \n{}";

    /**
     * ???????????????
     */
    public static void notice(String content){
        NotificationGroup notificationGroup = new NotificationGroup("testid", NotificationDisplayType.TOOL_WINDOW, false);
        /**
         * content :  ????????????
         * type  ?????????????????????warning,info,error
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

            JOptionPane.showMessageDialog(null, jsp, "????????????", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "?????????????????????" + e.getMessage());
        }
    }

    private ConfigFileInfoVo readInputInfo(String buttonId){
        System.out.println(String.format("?????????????????????%s", generatorSqlPathTextField.getText()));
        System.out.println(String.format("??????????????????%s", dbUrlTextField.getText()));
        System.out.println(String.format("????????????%s", userNameField.getText()));
        System.out.println(String.format("?????????%s", passwordField.getText()));
        System.out.println(String.format("????????????:%s", scriptPathTextField.getText()));
        System.out.println(String.format("?????????????????????%s", customScriptTextField.getText()));
        System.out.println(String.format("?????????????????????%s", errorTextField.getText()));
        System.out.println(String.format("????????????:%s", sqlOutputTextField.getText()));

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
            JOptionPane.showMessageDialog(null, "?????????????????????");
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
                    // ????????????????????????
                    setConfigFieldByConfigInfo(configFileInfo);
                    System.out.println("????????????????????????");
                }else if(0 == mainTabPanel.getSelectedIndex()){
                    // ??????????????????
                    setGenerateFieldByConfigInfo(configFileInfo);
                }else{
                    System.out.println("?????????");
                }

            }
        });
    }

    private void dynamicFileNameListenerHandle(){
        // ???????????????????????????
        versionTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                setFileNameTextField();
            }
        });
        // ??????????????????????????????
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
        System.out.println("????????????");
    }

    private void setFileNameTextField(){
        String last = fileNameTextField.getText();
        if(StrUtil.isBlank(last)){
            last = StrUtil.EMPTY;
        }else{
            String[] strs = fileNameTextField.getText().split("_");
            last = strs[strs.length - 1];
            // ??????????????????????????????????????????
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
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if(sqlTextArea.getText().startsWith("??????")){
                    sqlTextArea.setText("");
                    sqlTextArea.setForeground(Color.white);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                //????????????????????????????????????????????????????????????????????????????????????????????????

                String message = RedioMessageEnum.getValueByRadio(getRadioCodeValue());
                if(StrUtil.isBlank(sqlTextArea.getText())){
                    sqlTextArea.setText(message);
                    sqlTextArea.setForeground(new Color(142,142,142));
                }
            }
        });
    }

    /**
     * ????????????????????????
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
     * ????????????tab?????????
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
