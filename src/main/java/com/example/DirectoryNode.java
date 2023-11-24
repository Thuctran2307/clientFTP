package com.example;

import javax.swing.tree.DefaultMutableTreeNode;

public class DirectoryNode extends DefaultMutableTreeNode {
    private String fileType;
    private String dateCreated;

    public DirectoryNode(Object userObject, String fileType, String dateCreated) {
        super(userObject);
        this.fileType = fileType;
        this.dateCreated = dateCreated;
    }

    public String getFileType() {
        return fileType;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        String a = getUserObject().toString();
        String cleanedPath = a.replaceAll("[\\[\\],]", "");
        return cleanedPath;
    }
}