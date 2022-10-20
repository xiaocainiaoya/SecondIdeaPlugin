package cn.com.xiaocainiaoya.listener;

import cn.com.xiaocainiaoya.enums.RedioMessageEnum;
import cn.hutool.core.util.StrUtil;

import javax.swing.*;
import javax.swing.plaf.TextUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 按钮单选监听控制
 *
 * @author :jiangjiamin
 * @date : 2022/8/31 17:26
 */
public class RadioListener {


    public static void setRadioListener(List<JRadioButton> radioButtons, JTextArea sqlTextArea){

        // 遍历按钮列表
        for(JRadioButton jRadioButton : radioButtons){
            jRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for(JRadioButton innerRadioButton : radioButtons){
                        if(jRadioButton == innerRadioButton){
                            continue;
                        }
                        innerRadioButton.setSelected(false);
                    }

                    String message = RedioMessageEnum.getValueByCode(jRadioButton.getName());
                    if(StrUtil.isBlank(message)){
                        return;
                    }
                    String sql = sqlTextArea.getText();
                    if(StrUtil.isBlank(sql) || sql.startsWith("示例")){
                        sqlTextArea.setText(message);
                        sqlTextArea.setForeground(new Color(142,142,142));
                    }
                }
            });
        }


    }
}
