package com.example.Views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.net.ftp.FTPFile;
import java.awt.event.*;

import com.example.Client;
import com.example.HelperFunction.FTPFileNode;
import com.example.HelperFunction.SystemFileNode;

public class MainUI extends JFrame {

    public static MainUI instance;
    public Client client;

    JPanel remotePanel;
    JPanel localPanel;
    JPanel mainPanel;
    JPanel headerPanel;
    JPanel statusPanel;
    JPanel detailsPanel;

    public JTree localTree;
    public JTree remoteTree;

    JLabel lbHost;
    JLabel lbPort;
    JLabel lbUser;
    JLabel lbPass;
    JTextField txtHost;
    JTextField txtPort;
    JTextField txtUser;
    JTextField txtPass;
    JPasswordField txtPath;
    JButton btnConnect;
    JButton btnGo;
    JButton btnBack;

    JTextArea txtStatus;
    JScrollPane scrollPaneStatus;

    public ProgressBar progressBar = null;

    Dimension headerSize = new Dimension(1080, 40);
    Dimension statusSize = new Dimension(1050, 120);
    Dimension remoteSize = new Dimension(540, 800 - headerSize.height - statusSize.height - 100);
    Dimension detailsSize = new Dimension(1050, 900 - headerSize.height - statusSize.height - remoteSize.height - 10);

    public static MainUI getInstance() {
        if (instance == null) {
            instance = new MainUI();
        }
        return instance;
    }

    private MainUI() {
        initUI();
        initClient();
    }

    void initUI() {

        this.setTitle("Main UI");
        this.setSize(1080, 950);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout()); // Sử dụng BorderLayout cho frame chính

        // Tạo headerPanel
        headerPanel = new JPanel(new FlowLayout());
        lbHost = new JLabel("Host");
        txtHost = new JTextField(10);

        lbPort = new JLabel("Port");
        txtPort = new JTextField(10);
        lbUser = new JLabel("User");
        txtUser = new JTextField(10);
        lbPass = new JLabel("Pass");
        txtPass = new JPasswordField(10);
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

        // Tạo statusPanel và scrollPane
        statusPanel = new JPanel(new BorderLayout()); // Sử dụng BorderLayout cho statusPanel
        txtStatus = new JTextArea(10, 80);
        txtStatus.setEditable(false);
        scrollPaneStatus = new JScrollPane(txtStatus);
        statusPanel.setPreferredSize(statusSize);
        scrollPaneStatus.setPreferredSize(statusSize);
        statusPanel.add(scrollPaneStatus, BorderLayout.CENTER);

        // Tạo remotePanel
        mainPanel = new JPanel(new GridLayout(1, 2));

        remotePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        remotePanel.setPreferredSize(remoteSize);

        localPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        localPanel.setPreferredSize(remoteSize);

        mainPanel.add(remotePanel);
        mainPanel.add(localPanel);

        // Đặt các panel vào vị trí cụ thể trong BorderLayout của frame chính
        this.add(headerPanel);
        this.add(statusPanel);
        this.add(mainPanel);

        updateLocalPanel();

        // detile pannel
        detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        detailsPanel.setPreferredSize(detailsSize);
        this.add(detailsPanel);

        this.setVisible(true);
    }

    void initClient() {
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = txtHost.getText();
                String port = txtPort.getText();
                String user = txtUser.getText();
                String pass = txtPass.getText();

                if (host.isEmpty() || port.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                    return;
                }

                try {
                    client = new Client(host, Integer.parseInt(port), user, pass);
                    client.connect();
                    updateStatus("Connected to server...");
                    if (client.login()) {
                        updateRemotePanel();
                        updateStatus("Login Success! Listed files in root directory...");
                        localTree.updateUI();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Login failed");
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Can't connect to server");
                }

            }
        });
    }

    public void updateStatus(String status) {
        txtStatus.append(status + "\n");
    }

    private void addFilesToNode(File directory, DefaultMutableTreeNode parentNode) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                SystemFileNode childNode = new SystemFileNode(file);
                parentNode.add(childNode);
            }
        }
    }

    private void addFilesToNode(DefaultMutableTreeNode root, String filePath) {

        String path = filePath;
        try {
            for (FTPFile file : client.listFiles(path)) {

                FTPFileNode child = new FTPFileNode(file);

                root.add(child);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRemotePanel() throws IOException {

        remotePanel.removeAll();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        addFilesToNode(root, "");

        remoteTree = new JTree(root);
        remoteTree.setCellRenderer(new CustomTree());
        remoteTree.addMouseListener(new MouseAdapter() {
            // create right click event show popup menu
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath treepath = remoteTree.getPathForLocation(e.getX(), e.getY());
                    remoteTree.setSelectionPath(treepath);
                    PopUpMenu popupMenu = null;
                    if (treepath != null) {

                        FTPFileNode node = (FTPFileNode) treepath.getLastPathComponent();

                        String a = getPathFromTreePath(treepath);
                        if (node.getFTPFile().isFile()) {
                            popupMenu = new PopUpMenu(node, a, "REMOTE", 0);
                        } else if (node.getFTPFile().isDirectory()) {
                            popupMenu = new PopUpMenu(node, a, "REMOTE", 1);
                        }
                    } else {
                        popupMenu = new PopUpMenu(null, "", "REMOTE", 3);
                    }
                    remoteTree.setComponentPopupMenu(popupMenu);
                }
                else if(e.getClickCount() == 2){
                    TreePath path = remoteTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        node.removeAllChildren();
                        String pathString = getPathFromTreePath(path);
                        addFilesToNode(node, pathString);
                        remoteTree.updateUI();
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(remoteTree);
        scrollPane.setPreferredSize(remoteSize);
        remotePanel.add(scrollPane);

        remotePanel.revalidate();
        remotePanel.repaint();
    }

    public void updateLocalPanel() {

        DefaultMutableTreeNode rootLocal = new DefaultMutableTreeNode();
        File[] roots = File.listRoots();

        // Duyệt và in ra tên của mỗi ổ đĩa
        for (File root : roots) {
            rootLocal.add(new DefaultMutableTreeNode(root.getAbsolutePath()));
        }

        localTree = new JTree(rootLocal);

        // Thêm JTree vào JScrollPane để hỗ trợ cuộn
        JScrollPane localScrollPane = new JScrollPane(localTree);
        localTree.setCellRenderer(new CustomTree());
        localScrollPane.setPreferredSize(remoteSize);
        // Thêm JScrollPane vào localPanel
        localPanel.add(localScrollPane, BorderLayout.CENTER);
        localTree.addMouseListener(new MouseAdapter() {
            // create right click event show popup menu
            public void mousePressed(MouseEvent e) {
                try {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        TreePath treepath = localTree.getPathForLocation(e.getX(), e.getY());
                        localTree.setSelectionPath(treepath);
                        PopUpMenu popupMenu = null;

                        if (treepath != null) {

                            SystemFileNode node = (SystemFileNode) treepath.getLastPathComponent();
                            if (node.getFile().isFile()) {
                                popupMenu = new PopUpMenu(node, node.getFile().getAbsolutePath(), "LOCAL", 0);
                            } else if (node.getFile().isDirectory()) {
                                popupMenu = new PopUpMenu(node, node.getFile().getAbsolutePath(), "LOCAL", 1);
                            }
                        }
                        localTree.setComponentPopupMenu(popupMenu);
                    } else if (e.getClickCount() == 2) {
                        TreePath path = localTree.getPathForLocation(e.getX(), e.getY());
                        if (path != null) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                            node.removeAllChildren();
                            File rootFile = new File(getPathFromTreePath(path));
                            addFilesToNode(rootFile, node);
                            localTree.updateUI();
                        }
                    }
                } catch (Exception e1) {
                }

            }
        });
    }

    public String getPathFromTreePath(TreePath TreePath) {
        StringBuilder sb = new StringBuilder();
        Object[] nodes = TreePath.getPath();
        for (int i = 0; i < nodes.length; i++) {
            sb.append(File.separatorChar).append(nodes[i].toString());
        }
        return sb.toString();
    }

    public void updateDetailsPanel(String Name, String path, String lastModified, String type, String size)
            throws IOException {
        detailsPanel.removeAll();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.X_AXIS));

        addLabelToPanel("<html><font color='blue'>Name:</font> " + Name + "</html>", detailsPanel);
        addLabelToPanel("<html><font color='green'>Path:</font> " + path + "</html>", detailsPanel);
        addLabelToPanel("<html><font color='red'>Last Modified:</font> " + lastModified + "</html>", detailsPanel);
        addLabelToPanel("<html><font color='orange'>Type:</font> " + type + "</html>", detailsPanel);
        addLabelToPanel("<html><font color='purple'>Size:</font> " + size + "KB" + "</html>", detailsPanel);

        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    public void addProgressBar() {
        detailsPanel.removeAll();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.add(progressBar, BorderLayout.CENTER);
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    private void addLabelToPanel(String labelText, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(30, 0))); // Khoảng cách giữa các nhãn
    }

}
