package com.example.HelperFunction;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class SystemFileNode extends DefaultMutableTreeNode {
    File file;

    public SystemFileNode(File file) {
        super(file.getName()); // Đặt tên hiển thị là tên tập tin
        this.file = file;
    }

    public File getFile() {
        return file;
    }
    
}
