package com.example.Views;

import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JPanel {

    private JProgressBar progressBar = new JProgressBar();
    public JLabel lbProgress = new JLabel("Progress: ");
    private JLabel lbFileName = new JLabel("File Name: ");
    private JLabel lbType = new JLabel("Type: ");
    private JLabel lbSize = new JLabel("Size: ");

    private JLabel filename = new JLabel();
    private JLabel type = new JLabel();
    private JLabel size = new JLabel();

    public ProgressBar(String fileName, String type, String size) {
        setLayout(new GridLayout(4, 2)); // Sử dụng GridLayout để xếp các thành phần dọc theo cột

        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        this.filename.setText(fileName);
        this.type.setText(type);
        this.size.setText(size);

        add(lbProgress);
        add(progressBar);

        add(lbFileName);
        add(filename);
        
        add(lbType);
        add(this.type);

        add(lbSize);
        add(this.size);
        
    }

    public void updateProgress(int percent) {
        progressBar.setValue(percent);
    }
}

