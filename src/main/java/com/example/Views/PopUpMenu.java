package com.example.Views;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import com.example.FileHandler;

public class PopUpMenu extends JPopupMenu {
    JMenuItem download, rename, delete, upload, createFolder, detail;

    public PopUpMenu(DefaultMutableTreeNode FolderNode, String pathCurrently, String type, int check) {
        super();
        upload = new JMenuItem("Upload");
        download = new JMenuItem("Download");
        rename = new JMenuItem("Rename");
        delete = new JMenuItem("Delete");
        createFolder = new JMenuItem("Create New Folder");
        detail = new JMenuItem("Detail");

        if (check == 0) {
            if (type == "REMOTE") {
                this.add(download);
                this.add(rename);
                this.add(delete);
                download.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            System.out.println(pathCurrently);
                            MainUI.getInstance().client.downloadFile("D:\\" + FolderNode.getUserObject().toString(),
                                    pathCurrently);
                            MainUI.getInstance().updateStatus("Download successfully, file saved in: " + "D:\\"
                                    + FolderNode.getUserObject().toString());
                            MainUI.getInstance().updateLocalPanel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                rename.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        try {
                            MainUI.getInstance().client.renameFile(pathCurrently, newName);
                            MainUI.getInstance().updateStatus("Rename successfully");
                            FolderNode.setUserObject(newName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                delete.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            MainUI.getInstance().client.deleteFile(pathCurrently);
                            MainUI.getInstance().updateStatus("Delete successfully");
                            MainUI.getInstance().updateRemotePanel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else{
                this.add(upload);
                this.add(rename);
                this.add(delete);
                upload.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String pathToSave = JOptionPane.showInputDialog("Enter path to upload: ");
                        try {
                            
                            MainUI.getInstance().client.uploadFile(pathCurrently , pathToSave + "\\" + FolderNode.getUserObject().toString());
                            MainUI.getInstance().updateStatus("Upload successfully to " + pathToSave + "\\" + FolderNode.getUserObject().toString());
                            MainUI.getInstance().updateRemotePanel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                rename.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        try {
                            System.out.println(pathCurrently);
                            FileHandler.getInstance().renameFile(pathCurrently, newName, FolderNode.getUserObject().toString());
                            MainUI.getInstance().updateLocalPanel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                delete.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            // FileHandler.getInstance().deleteFolder(MainUI.getInstance().client pathCurrently);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else if (check == 1) {
            this.add(rename);
            this.add(delete);
            this.add(createFolder);

            rename.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        MainUI.getInstance().client.renameFolder(pathCurrently, newName);
                        MainUI.getInstance().updateStatus("Rename successfully");
                        MainUI.getInstance().updateRemotePanel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            delete.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        MainUI.getInstance().client.deleteFile(pathCurrently);
                        MainUI.getInstance().updateStatus("Delete successfully");
                        MainUI.getInstance().updateRemotePanel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            createFolder.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        MainUI.getInstance().client.createFolder(pathCurrently + "/" + newName);
                        MainUI.getInstance().updateStatus("Create successfully");
                        MainUI.getInstance().updateRemotePanel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            this.add(createFolder);
            this.add(upload);

            createFolder.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        MainUI.getInstance().client.createFolder(pathCurrently + "/" + newName);
                        MainUI.getInstance().updateStatus("Create successfully");
                        MainUI.getInstance().updateRemotePanel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            upload.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        MainUI.getInstance().client.uploadFile("D:\\" + newName, pathCurrently + "/" + newName);
                        MainUI.getInstance().updateStatus("Upload successfully");
                        MainUI.getInstance().updateRemotePanel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        this.add(detail);
        detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (type == "REMOTE") {
                        FTPFileNode node = (FTPFileNode) FolderNode;
                        
                        String nameFile = node.getFTPFile().getName();
                        String sizeFile = node.getFTPFile().getSize() + "";
                        String pathFile = pathCurrently;
                        String typeFile = node.getFTPFile().getType() == 0 ? "File" : "Folder";
                        String lastModified = node.getFTPFile().getTimestamp().getTime() + "";

                        MainUI.getInstance().updateDetailsPanel(nameFile, pathFile, lastModified, typeFile, sizeFile);
                        
                    }
                    else{
                        SystemFileNode node = (SystemFileNode) FolderNode;
                        
                        String nameFile = node.getFile().getName();
                        String sizeFile = node.getFile().length() + "";
                        String pathFile = pathCurrently;
                        String typeFile = node.getFile().isFile() ? "File" : "Folder";
                        String lastModified = node.getFile().lastModified() + "";

                        MainUI.getInstance().updateDetailsPanel(nameFile, pathFile, lastModified, typeFile, sizeFile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
