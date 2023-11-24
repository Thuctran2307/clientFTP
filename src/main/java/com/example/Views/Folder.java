package com.example.Views;

import javax.swing.*;
import java.awt.*;
public class Folder extends JPanel {
    public JLabel pic;
    public JLabel FolderName;

    public Folder(String FolderName, boolean isFile) {
        this.setSize(80, 150);
        this.setLayout(new FlowLayout());
        this.FolderName = new JLabel(FolderName);
        this.pic = new JLabel();
        ImageIcon originalIcon;
        if (!isFile) {
            originalIcon = new ImageIcon("src\\main\\java\\com\\example\\image\\folder.png");
        } else {
            originalIcon = new ImageIcon("src\\main\\java\\com\\example\\image\\file.png");
        }

        // Resize hình ảnh
        Image img = originalIcon.getImage();
        Image resizedImage = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); 
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Đặt biểu tượng đã thay đổi kích thước vào JLabel
        this.pic.setIcon(resizedIcon);

        this.add(this.pic);
        this.add(this.FolderName);
    }
}
