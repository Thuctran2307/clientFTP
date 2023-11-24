package com.example.Views;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.example.Client;

public class PopUpMenu extends JPopupMenu {
    JMenuItem download, rename, delete, upload, create;

    public PopUpMenu(String FolderName, String pathCurrently, int check) {
        super();
        upload = new JMenuItem("Upload");
        download = new JMenuItem("Download");
        rename = new JMenuItem("Rename");
        delete = new JMenuItem("Delete");
        create = new JMenuItem("Create");

        if (check == 0) {
            this.add(download);
            this.add(rename);
            this.add(delete);
            download.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        System.out.println(pathCurrently);
                        Client.getInstance().downloadFile("D:\\" + FolderName, pathCurrently);
                        MainUI.getInstance().updateStatus("Download successfully, file saved in: " + "D:\\" + FolderName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            rename.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        Client.getInstance().renameFile(pathCurrently, newName);
                        MainUI.getInstance().updateStatus("Rename successfully");
                        MainUI.getInstance().updateRemotePanel("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            delete.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        Client.getInstance().deleteFile(pathCurrently);
                        MainUI.getInstance().updateStatus("Delete successfully");
                        MainUI.getInstance().updateRemotePanel("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (check == 1) {
            this.add(rename);
            this.add(delete);

            rename.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        Client.getInstance().renameFolder(pathCurrently, newName);
                        MainUI.getInstance().updateStatus("Rename successfully");
                        MainUI.getInstance().updateRemotePanel("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            delete.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        Client.getInstance().deleteFile(pathCurrently);
                        MainUI.getInstance().updateStatus("Delete successfully");
                        MainUI.getInstance().updateRemotePanel("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            this.add(create);
            this.add(upload);

            create.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        Client.getInstance().createFolder(pathCurrently + "/" + newName);
                        MainUI.getInstance().updateStatus("Create successfully");
                        MainUI.getInstance().updateRemotePanel("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            upload.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        Client.getInstance().uploadFile("D:\\" + newName, pathCurrently + "/" + newName);
                        MainUI.getInstance().updateStatus("Upload successfully");
                        MainUI.getInstance().updateRemotePanel("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
