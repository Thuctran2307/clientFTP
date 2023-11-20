package com.example.Views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.net.ftp.FTPFile;

import com.example.Client;

public class MainUI extends JFrame {

    public static MainUI instance;
    Client client;
    JPanel menuPanel;
    JPanel remotePanel;
    JPanel headerPanel;
    JPanel statusPanel;

    JLabel lbHost;
    JLabel lbPort;
    JLabel lbUser;
    JLabel lbPass;
    JTextField txtHost;
    JTextField txtPort;
    JTextField txtUser;
    JTextField txtPass;
    JTextField txtPath;
    JButton btnConnect;
    JButton btnGo;
    JButton btnBack;

    JTextArea txtStatus;
    JScrollPane scrollPane;
    
    public static MainUI getInstance() {
        if (instance == null) {
            instance = new MainUI();
        }
        return instance;        
    }

    private MainUI() {
        initUI();
        initEvent();
    }

    void initUI(){
        Dimension headerSize = new Dimension(1080, 25);
        Dimension statusSize = new Dimension(1080, 150);
        Dimension remoteSize = new Dimension(1080, 800 - headerSize.height - statusSize.height);

        this.setTitle("Main UI");
        this.setSize(1080, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout()); // Sử dụng BorderLayout cho frame chính

        // Tạo headerPanel
        headerPanel = new JPanel(new GridLayout(1, 11)); // GridLayout với 2 hàng, 5 cột
        lbHost = new JLabel("Host");
        txtHost = new JTextField(10);

        lbPort = new JLabel("Port");
        txtPort = new JTextField(10);
        lbUser = new JLabel("User");
        txtUser = new JTextField(10);
        lbPass = new JLabel("Pass");
        txtPass = new JTextField(10);
        btnConnect = new JButton("Connect");

        // Thêm các thành phần vào headerPanel
        headerPanel.add(lbHost);
        headerPanel.add(txtHost);
        headerPanel.add(lbPort);
        headerPanel.add(txtPort);
        headerPanel.add(lbUser);
        headerPanel.add(txtUser);
        headerPanel.add(lbPass);
        headerPanel.add(txtPass);
        headerPanel.add(btnConnect);
        headerPanel.setPreferredSize(headerSize);

        // menupanel
        menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtPath = new JTextField(40);
        btnGo = new JButton("Go");
        btnBack = new JButton("Back");
        menuPanel.add(txtPath);
        menuPanel.add(btnBack);
        menuPanel.add(btnGo);

        // Tạo statusPanel và scrollPane
        statusPanel = new JPanel(new BorderLayout()); // Sử dụng BorderLayout cho statusPanel
        txtStatus = new JTextArea(30, 80);
        scrollPane = new JScrollPane(txtStatus);
        statusPanel.add(scrollPane, BorderLayout.CENTER);
        statusPanel.add(menuPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(statusSize);

        // Tạo remotePanel
        
        remotePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        remotePanel.setPreferredSize(remoteSize);

        // Đặt các panel vào vị trí cụ thể trong BorderLayout của frame chính
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(statusPanel, BorderLayout.CENTER);
        this.add(remotePanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    void initEvent(){

        remotePanel.setComponentPopupMenu(new PopUpMenu("", "", 3));

        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = txtHost.getText();
                String port = txtPort.getText();
                String user = txtUser.getText();
                String pass = txtPass.getText();

                client = Client.getInstance(host, Integer.parseInt(port), user, pass);
                try {
                    client.connect();
                    updateStatus("Connected to server...");
                    updateRemotePanel("");
                    updateStatus("Listed files in root directory...");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void updateStatus(String status){
        txtStatus.append(status + "\n");
    }

    public void updateRemotePanel(String path) throws IOException{

        remotePanel.removeAll();
        Folder folderPanel = null;

        for(FTPFile folder : client.listFiles(path)){
            JPopupMenu popupMenu;
            if(folder.isDirectory()){
                folderPanel = new Folder(folder.getName(), false);
                folderPanel.setPreferredSize(new Dimension(120, 80));
                popupMenu = new PopUpMenu(folderPanel.FolderName.getText(), path, 2);
            }else{
                folderPanel = new Folder(folder.getName(), true);
                folderPanel.setPreferredSize(new Dimension(100, 80));
                popupMenu = new PopUpMenu(folderPanel.FolderName.getText(), path, 1);
            }
            folderPanel.setComponentPopupMenu(popupMenu);
            folderPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if(evt.getClickCount() == 2){
                        try {
                            updateRemotePanel(path + folder.getName() + "/");
                            updateStatus("Listed files in " + path + folder.getName() + "/");
                            txtPath.setText(txtPath.getText() + folder.getName() + "/");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            remotePanel.add(folderPanel);
        }
        remotePanel.revalidate();
        remotePanel.repaint();
    }
}
