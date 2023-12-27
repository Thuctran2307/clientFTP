package com.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.example.Views.Progress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    private FTPClient ftpClient;
    private String server;
    private int port;
    private String user;
    private String pass;
    public boolean isAvailable = true;

    public Client(String server, int port, String user, String pass) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.ftpClient = new FTPClient();

        this.ftpClient.setBufferSize(2048);
    }

    public void connect() throws IOException {
        ftpClient.connect(server, port);
        
    }

    public boolean login() throws IOException {
        if(ftpClient.login(user, pass)) {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(16384);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            return true;
        }
        return false;
    }

    public void disconnect() throws IOException {
        ftpClient.disconnect();
    }

    public ArrayList<FTPFile> listFiles(String path) throws IOException {
        return new ArrayList<FTPFile>(Arrays.asList(ftpClient.listFiles(path)));
    }

    public ArrayList<FTPFile> listFolders(String path) throws IOException {
        return new ArrayList<FTPFile>(Arrays.asList(ftpClient.listDirectories(path)));
    }

    public boolean uploadFile(String localFilePath, String remoteFilePath) throws IOException {
        File localFile = new File(localFilePath);
        FileInputStream inputStream = new FileInputStream(localFile);
        ftpClient.setCopyStreamListener(new Progress((int) localFile.length()));
        isAvailable = false;
        if (ftpClient.storeFile(remoteFilePath, inputStream)) {
            inputStream.close();
            isAvailable = true;
            return true;
        }
        return false;
    }

    public boolean downloadFile(String localFilePath, String remoteFilePath, int SizeFile) throws IOException {

        File localFile = new File(localFilePath);
        ftpClient.setCopyStreamListener(new Progress((int) SizeFile));
        isAvailable = false;
        if (ftpClient.retrieveFile(remoteFilePath, new java.io.FileOutputStream(localFile))) {
            isAvailable = true;
            return true;
        }
        return false;
    }

    public boolean deleteFile(String remoteFilePath) {
        try {
            return ftpClient.deleteFile(remoteFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean createFolder(String remoteDirPath) {
        try {
            return ftpClient.makeDirectory(remoteDirPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteFolder(String remoteDirPath) {
        try {
            FTPFile[] subFiles = ftpClient.listFiles(remoteDirPath); // Lấy danh sách các tệp và thư mục trong thư mục gốc
            if (subFiles != null && subFiles.length > 0) {
                for (FTPFile aFile : subFiles) {
                    String currentFileName = aFile.getName();
                    if (currentFileName.equals(".") || currentFileName.equals("..")) {
                        continue; // Bỏ qua thư mục hiện tại (.) và thư mục cha (..)
                    }
                    String filePath = remoteDirPath + "/" + currentFileName;
                    if (aFile.isDirectory()) {
                        // Nếu là thư mục, gọi đệ quy để xoá thư mục con
                        deleteFolder(filePath);
                    } else {
                        // Nếu là tệp, xoá tệp
                        ftpClient.deleteFile(filePath);
                    }
                }
            }
            // Sau khi xoá tất cả các thư mục con và tệp trong thư mục gốc, xoá thư mục gốc
            return ftpClient.removeDirectory(remoteDirPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean renameFile(String from, String to) {
        try {
            return ftpClient.rename(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean renameFolder(String from, String to) {
        try {
            return ftpClient.rename(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean moveFile(String from, String to) {
        try {
            return ftpClient.rename(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean moveFolder(String from, String to) {
        try {
            return ftpClient.rename(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String getWorkingDirectory() throws IOException {
        return ftpClient.printWorkingDirectory();
    }
}
