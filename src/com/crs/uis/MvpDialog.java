package com.crs.uis;

import com.crs.utils.CheckNull;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.*;

public class MvpDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mTextPanel;
    private JButton btnSelectFolder;
    private JLabel mTvContractName;
    private JTextField mEdContractName;
    private Project mProject;
    private AnActionEvent mAnActionEvent;

    public MvpDialog() {
        setContentPane(contentPane);
    }

    public MvpDialog(AnActionEvent e) {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonCancel);

        mAnActionEvent = e;
        mProject = e.getProject();

        setSize(800, 400);
        setLocationRelativeTo(null);
        setModal(false);
        setTitle("创建MVP契约类");

        btnSelectFolder.addActionListener(actionEvent -> {
            if (onSelectFolderListener != null) {
                onSelectFolderListener.onSelect(this, actionEvent);
            }
        });

        buttonOK.addActionListener(this::onOK);

        buttonCancel.addActionListener(this::onCancel);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(ActionEvent e) {
        // add your code here
        if (CheckNull.isNull(getEdContractName())) {
            Messages.showMessageDialog(mProject, "警告", "契约类名不能为空", Messages.getInformationIcon());
            return;
        }
        if (onCreatMvpFileListener != null) {
            onCreatMvpFileListener.onCreat(this, e);
        }
        dispose();
    }

    private void onCancel(ActionEvent e) {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        MvpDialog dialog = new MvpDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

    public void setSelectFolderPathText(String path) {
        if (CheckNull.isNull(path))
            return;
        mTextPanel.setText(path);
    }

    public String getEdContractName() {
        return mEdContractName.getText();
    }

    public interface OnSelectFolderListener {
        void onSelect(JDialog dialog, ActionEvent e);
    }

    public interface OnCreatMvpFileListener {
        void onCreat(JDialog dialog, ActionEvent e);
    }

    public void setOnSelectFolderListener(OnSelectFolderListener listener) {
        this.onSelectFolderListener = listener;
    }

    private OnSelectFolderListener onSelectFolderListener;

    public void setOnCreatMvpFileListener(OnCreatMvpFileListener listener) {
        this.onCreatMvpFileListener = listener;
    }

    private OnCreatMvpFileListener onCreatMvpFileListener;

}
