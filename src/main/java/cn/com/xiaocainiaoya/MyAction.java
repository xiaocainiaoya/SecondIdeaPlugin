package cn.com.xiaocainiaoya;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;

/**
 * @author :jiangjiamin
 * @date : 2022/9/20 09:50
 */
public class MyAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        String classPath = psiFile.getVirtualFile().getPath();

        System.out.println(psiFile.getVirtualFile());

        //Library library = LibraryUtil.findLibraryByClass(psiClass.getQualifiedName(), project);
        //VirtualFile YmlFile = library.getFiles(OrderRootType.CLASSES)[0].findChild("script.tpl.sql");

        Messages.showMessageDialog(project, "idea-plugin: " + classPath, "Hi IDEA Plugin", Messages.getInformationIcon());
    }
}
