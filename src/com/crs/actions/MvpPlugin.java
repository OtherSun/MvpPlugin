package com.crs.actions;

import com.crs.uis.MvpDialog;
import com.crs.utils.CheckNull;
import com.crs.utils.FileUtils;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class MvpPlugin extends AnAction implements MvpDialog.OnSelectFolderListener, MvpDialog.OnCreatMvpFileListener {

    private int count;
    private AnActionEvent mAnActionEvent;
    private MvpDialog mMvpDialog;
    private VirtualFile mVirtualFile;
    private String mSelectPath;
    private Project mProject;

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        mAnActionEvent = e;
        mProject = e.getProject();
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (mProject != null && psiFile != null && editor != null) {
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(mProject);
            PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
            PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            if (psiClass != null && psiClass.getNameIdentifier() != null) {
                String className = psiClass.getNameIdentifier().getText();
                String selectedText = Messages.showInputDialog(mProject, "方法名", "请输入方法名", Messages.getInformationIcon());
                String methodText = buildMethodText("", className, selectedText);
                PsiMethod psiMethod = elementFactory.createMethodFromText(methodText, psiClass);
                /*对文档进行操作部分代码，需要放入Runnable接口中实现,
                由IDEA在内部将其通过一个新线程执行,加入任务，由IDEA调度执行这个任务*/
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    psiClass.add(psiMethod);
                    CodeStyleManager.getInstance(mProject).reformat(psiClass);
                });
            }

        } else {
            mVirtualFile = DataKeys.VIRTUAL_FILE.getData(mAnActionEvent.getDataContext());
            mSelectPath = CheckNull.isNotNull(mVirtualFile) ? mVirtualFile.getPath() : "";
            mMvpDialog = new MvpDialog(mAnActionEvent);
            mMvpDialog.setSelectFolderPathText(mSelectPath);
            mMvpDialog.setOnSelectFolderListener(this);
            mMvpDialog.setOnCreatMvpFileListener(this);
            mMvpDialog.setVisible(true);
        }


    }

    /**
     * 在显示插件菜单前的回调函数，可以用来判断是否要显示或隐藏该插件
     */
    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
//        e.getPresentation().setEnabled(editor != null);
    }

    /**
     * 自动代码拼接
     *
     * @param modifier   修饰符
     * @param valueType  返回值类型
     * @param methodName 方法名
     * @return 生成代码
     */
    private String buildMethodText(String modifier, String valueType, String methodName) {
        return String.format(Locale.getDefault()
                , "%s %s(){%s}"
                , String.format(Locale.getDefault(), "%s%s"
                        , CheckNull.isNotNull(modifier) ? modifier : ""
                        , CheckNull.isNotNull(valueType) ? valueType : "void"
                )
                , CheckNull.isNotNull(methodName) ? methodName : ""
                , "return this;"
        );
    }

    private String parseVirtualFile(VirtualFile[] virtualFiles) {
        return mSelectPath = CheckNull.isNotNull(virtualFiles) && virtualFiles.length != 0 ? virtualFiles[0].getPath() : "";
    }

    @Override
    public void onSelect(JDialog dialog, ActionEvent e) {
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(new FileChooserDescriptor(false
                        , true
                        , false
                        , false
                        , false
                        , false)
                , mAnActionEvent.getProject()
                , mVirtualFile);
        mMvpDialog.setSelectFolderPathText(parseVirtualFile(virtualFiles));

    }

    @Override
    public void onCreat(JDialog dialog, ActionEvent e) {
        String packageName = mSelectPath.substring(mSelectPath.indexOf("com"), mSelectPath.length()).replace("/", ".");
        String viewCode = FileUtils.readFile(this, "ViewCode.txt");
        String contractCode = FileUtils.readFile(this, "ContractCode.txt")
                .replace("$package$", packageName)
                .replace("$View_Code$", viewCode)
                .replace("$Contract_Name$", mMvpDialog.getEdContractName());
        FileUtils.writetoFile(contractCode, mSelectPath, mMvpDialog.getEdContractName() + ".java");
        mProject.getBaseDir().refresh(false,true);
    }


}
