package com.example.Views;

import javax.swing.JMenuItem;
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

        if (check == 1) {
            this.add(download);
            download.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        Client.getInstance().downloadFile("D:\\" + FolderName, pathCurrently + FolderName);
                        MainUI.getInstance().updateStatus("Download successfully, file saved in: " + "D:\\" + FolderName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.add(rename);
            this.add(delete);
        } else if (check == 2) {
            this.add(rename);
            this.add(delete);
        } else {
            this.add(create);
            this.add(upload);
        }

    }
}
