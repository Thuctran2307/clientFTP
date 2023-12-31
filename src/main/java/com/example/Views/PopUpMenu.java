package com.example.Views;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import com.example.HelperFunction.FTPFileNode;
import com.example.HelperFunction.FileHandler;
import com.example.HelperFunction.SystemFileNode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import java.util.Date;
public class PopUpMenu extends JPopupMenu {
    private final ExecutorService executor = Executors.newFixedThreadPool(1); // Số lượng luồng
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
                            executor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (MainUI.getInstance().client.isAvailable) {
                                            String size = ((FTPFileNode) FolderNode).getFTPFile().getSize() + "";
                                            String message = null;
                                            String pathToSave = JOptionPane.showInputDialog("Enter path to download: ");
                                            if (pathToSave == null || pathToSave.equals("")) {
                                                return;
                                            }
                                            MainUI.getInstance().progressBar = new ProgressBar(
                                                    FolderNode.getUserObject().toString(), "Download",  Long.parseLong(size)/1024  + " KB");
                                            if (MainUI.getInstance().client.downloadFile(
                                                    pathToSave + "\\" + FolderNode.getUserObject().toString(),
                                                    pathCurrently, Integer.parseInt(size))) {
                                                message = "Download successfully, file saved in: " + pathToSave + "\\"
                                                        + FolderNode.getUserObject().toString();
                                            } else {
                                                message = "Download failed";
                                                MainUI.getInstance().client.isAvailable = true;
                                            }
                                            MainUI.getInstance().updateStatus(message);

                                        } else {
                                            MainUI.getInstance().updateStatus(
                                                    "The action cannot be performed because there is an ongoing process.");
                                        }

                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                        MainUI.getInstance().client.isAvailable = true;
                                        MainUI.getInstance().updateStatus("Something went wrong!");
                                    }
                                }
                            });
                            executor.shutdown();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                rename.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        try {
                            if (MainUI.getInstance().client.renameFile(pathCurrently, newName)) {
                                MainUI.getInstance().updateStatus("Rename successfully");
                                MainUI.getInstance().updateRemotePanel();
                            } else {
                                MainUI.getInstance().updateStatus("Rename failed");
                            }
                            FolderNode.setUserObject(newName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                delete.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            String message = null;
                            if (MainUI.getInstance().client.deleteFile(pathCurrently)) {
                                message = "Delete successfully";
                            } else {
                                message = "Delete failed";
                            }
                            MainUI.getInstance().updateStatus(message);
                            MainUI.getInstance().updateRemotePanel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if(type == "LOCAL"){
                this.add(upload);
                this.add(rename);
                this.add(delete);
                upload.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String pathToSave = JOptionPane.showInputDialog("Enter path to upload: ");
                        if (pathToSave == null || pathToSave.equals("")) {
                            return;
                        }
                        try {
                            executor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (MainUI.getInstance().client.isAvailable) {
                                            String size = ((SystemFileNode) FolderNode).getFile().length() + "";
                                            String message = null;
                                            MainUI.getInstance().progressBar = new ProgressBar(
                                                    FolderNode.getUserObject().toString(), "Upload", Long.parseLong(size)/1024  + " KB");

                                            if (MainUI.getInstance().client.uploadFile(pathCurrently, pathToSave + "\\" + FolderNode.getUserObject().toString())) {
                                                    message = "Upload successfully, file saved in: " + pathToSave + "\\" + FolderNode.getUserObject().toString();
                                            } else {
                                                message = "Upload failed";
                                                MainUI.getInstance().client.isAvailable = true;
                                            }
                                            MainUI.getInstance().updateStatus(message);
                                        } else {
                                            MainUI.getInstance().updateStatus(
                                                    "The action cannot be performed because there is an ongoing process.");
                                        }
                                    } catch (Exception e) {
                                        MainUI.getInstance().client.isAvailable = true;
                                        System.out.println(e.getMessage());
                                        MainUI.getInstance().updateStatus("Something went wrong!");
                                    }
                                }
                            });
                            executor.shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                rename.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        if (newName == null || newName.equals("")) {
                            return;
                        }
                        try {
                            System.out.println(pathCurrently);
                            FileHandler.getInstance().renameFile(pathCurrently, newName,
                                    FolderNode.getUserObject().toString());
                            MainUI.getInstance().updateLocalPanel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                delete.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            SystemFileNode node = (SystemFileNode) FolderNode;
                            if(node.getFile().isFile()){
                                FileHandler.getInstance().deleteFile(pathCurrently);
                            }
                            else if(node.getFile().isDirectory()){
                                FileHandler.getInstance().deleteFolder(pathCurrently);
                            }
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
                            String sizeFile = node.getFTPFile().getSize()/1024 + "";
                            String pathFile = pathCurrently;
                            String typeFile = node.getFTPFile().getType() == 0 ? "File" : "Folder";
                            String lastModified = node.getFTPFile().getTimestamp().getTime() + "";

                            MainUI.getInstance().updateDetailsPanel(nameFile, pathFile, lastModified, typeFile,
                                    sizeFile);

                        } else {
                            SystemFileNode node = (SystemFileNode) FolderNode;

                            String nameFile = node.getFile().getName();
                            String sizeFile = node.getFile().length()/1024 + "";
                            String pathFile = pathCurrently;
                            String typeFile = node.getFile().isFile() ? "File" : "Folder";

                            Date lastModifiedDate = new Date(node.getFile().lastModified());

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String formattedDate = dateFormat.format(lastModifiedDate);
                            String lastModified = formattedDate;

                            MainUI.getInstance().updateDetailsPanel(nameFile, pathFile, lastModified, typeFile,
                                    sizeFile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (check == 1) {
            this.add(rename);
            this.add(delete);
            this.add(createFolder);

            if (type == "REMOTE") {
                rename.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        if(newName == null || newName.equals("")){
                            return;
                        }
                        try {
                            if (MainUI.getInstance().client.renameFolder(pathCurrently, newName)) {
                                MainUI.getInstance().updateStatus("Rename successfully");
                                MainUI.getInstance().updateRemotePanel();
                            } else {
                                MainUI.getInstance().updateStatus("Rename failed");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                delete.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            if (MainUI.getInstance().client.deleteFolder(pathCurrently)) {
                                MainUI.getInstance().updateStatus("Delete successfully");
                                MainUI.getInstance().updateRemotePanel();
                            } else {
                                MainUI.getInstance().updateStatus("Delete failed");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                createFolder.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        try {
                            if (MainUI.getInstance().client.createFolder(pathCurrently + "/" + newName)) {
                                MainUI.getInstance().updateStatus("Create successfully");
                                MainUI.getInstance().updateRemotePanel();
                            } else {
                                MainUI.getInstance().updateStatus("Create failed");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if(type == "LOCAL") {
                rename.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        if (newName == null || newName.equals("")) {
                            return;
                        }
                        try {
                            FileHandler.getInstance().renameFile(pathCurrently, newName,
                                    FolderNode.getUserObject().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                delete.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            SystemFileNode node = (SystemFileNode) FolderNode;
                            if(node.getFile().isFile()){
                                FileHandler.getInstance().deleteFile(pathCurrently);
                            }
                            else if(node.getFile().isDirectory()){
                                FileHandler.getInstance().deleteFolder(pathCurrently);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                createFolder.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String newName = JOptionPane.showInputDialog("Enter new name: ");
                        try {
                            FileHandler.getInstance().createFolder(pathCurrently + "\\" + newName);
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
                            String sizeFile = node.getFTPFile().getSize()/1024 + "";
                            String pathFile = pathCurrently;
                            String typeFile = node.getFTPFile().getType() == 0 ? "File" : "Folder";
                            String lastModified = node.getFTPFile().getTimestamp().getTime() + "";

                            MainUI.getInstance().updateDetailsPanel(nameFile, pathFile, lastModified, typeFile,
                                    sizeFile);

                        } else if(type == "LOCAL") {
                            SystemFileNode node = (SystemFileNode) FolderNode;

                            String nameFile = node.getFile().getName();
                            String sizeFile = node.getFile().length()/1024 + "";
                            String pathFile = pathCurrently;
                            String typeFile = node.getFile().isFile() ? "File" : "Folder";
                            String lastModified = node.getFile().lastModified() + "";

                            MainUI.getInstance().updateDetailsPanel(nameFile, pathFile, lastModified, typeFile,
                                    sizeFile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            this.add(createFolder);

            createFolder.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String newName = JOptionPane.showInputDialog("Enter new name: ");
                    try {
                        if (type == "REMOTE") {
                            if (MainUI.getInstance().client.createFolder(pathCurrently + "/" + newName)) {
                                MainUI.getInstance().updateStatus("Create successfully");
                                MainUI.getInstance().updateRemotePanel();
                            } else {
                                MainUI.getInstance().updateStatus("Create failed");
                            }
                        } else {
                            if (pathCurrently.equals("")) {
                                MainUI.getInstance().updateStatus("Create folder in local failed because path is null");
                                return;
                            }
                            FileHandler.getInstance().createFolder(pathCurrently + "\\" + newName);
                            MainUI.getInstance().updateLocalPanel();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
