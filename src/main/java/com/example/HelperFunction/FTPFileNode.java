package com.example.HelperFunction;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.net.ftp.FTPFile;

public class FTPFileNode extends DefaultMutableTreeNode {
    private FTPFile ftpFile;

    public FTPFileNode(FTPFile ftpFile) {
        super(ftpFile.getName()); // Đặt tên hiển thị là tên tập tin
        this.ftpFile = ftpFile;
    }

    public FTPFile getFTPFile() {
        return ftpFile;
    }
}
