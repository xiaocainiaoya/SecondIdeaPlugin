package cn.com.xiaocainiaoya;

import cn.com.xiaocainiaoya.form.MainForm;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author :jiangjiamin
 * @date : 2022/8/31 11:24
 */
public class MyWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MainForm mainForm = new MainForm();
        mainForm.setProject(project);

        //获取内容工厂的实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        //获取用于toolWindow显示的内容
        Content content = contentFactory.createContent(mainForm.getTopPanel(), "", false);

        //给toolWindow设置内容
        toolWindow.getContentManager().addContent(content);
    }
}
