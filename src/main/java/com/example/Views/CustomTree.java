package com.example.Views;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.example.HelperFunction.FTPFileNode;
import com.example.HelperFunction.SystemFileNode;

import java.awt.*;
import java.io.File;

public class CustomTree extends DefaultTreeCellRenderer {
    private final ImageIcon folderIcon = new ImageIcon("src\\main\\java\\com\\example\\image\\folder.png"); // Đường dẫn ảnh icon thư mục
    private final ImageIcon fileIcon = new ImageIcon("src\\main\\java\\com\\example\\image\\file.png"); // Đường dẫn ảnh icon tệp tin

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                                                    

        
        // try {
        //     SystemFileNode node = (SystemFileNode) value;
        //     File file = node.getFile();
        //     if (file.isDirectory()) {
        //         setIcon(folderIcon);
        //     } else {
        //         setIcon(fileIcon);
        //     }
        // } catch (Exception e) {
        //     setIcon(folderIcon);
        // }
        if(value instanceof SystemFileNode ){
            SystemFileNode node = (SystemFileNode) value;
            File file = node.getFile();
            if (file.isDirectory()) {
                setIcon(folderIcon);
            } else {
                setIcon(fileIcon);
            }
        }
        else if(value instanceof FTPFileNode){
            FTPFileNode node = (FTPFileNode) value;
            if (node.getFTPFile().isDirectory()) {
                setIcon(folderIcon);
            } else {
                setIcon(fileIcon);
            }
        }
        else{
            setIcon(folderIcon);
        }

        return this;
    }
}