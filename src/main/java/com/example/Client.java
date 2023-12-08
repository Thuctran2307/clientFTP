package com.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

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

    public Client(String server, int port, String user, String pass) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.ftpClient = new FTPClient();
    }

    public void  connect() throws IOException {
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
    }

    public void disconnect() throws IOException {
        ftpClient.disconnect();
    }

    public ArrayList<FTPFile> listFiles(String path) throws IOException {
        return  new ArrayList<FTPFile>(Arrays.asList(ftpClient.listFiles(path)));
    }

    public ArrayList<FTPFile> listFolders(String path) throws IOException {
        return  new ArrayList<FTPFile>(Arrays.asList(ftpClient.listDirectories(path)));
    }

    public void uploadFile(String localFilePath, String remoteFilePath) throws IOException {
        File localFile = new File(localFilePath);
        FileInputStream inputStream = new FileInputStream(localFile);
        ftpClient.storeFile(remoteFilePath, inputStream);
        inputStream.close();
    }

    public void downloadFile(String localFilePath, String remoteFilePath) throws IOException {
        File localFile = new File(localFilePath);
        ftpClient.retrieveFile(remoteFilePath, new java.io.FileOutputStream(localFile));
    }

    public void deleteFile(String remoteFilePath) throws IOException {
        ftpClient.deleteFile(remoteFilePath);
    }

    public void createFolder(String remoteDirPath) throws IOException {
        ftpClient.makeDirectory(remoteDirPath);
    }

    public void deleteFolder(String remoteDirPath) throws IOException {
        ftpClient.removeDirectory(remoteDirPath);
    }

    public void renameFile(String from, String to) throws IOException {
        ftpClient.rename(from, to);
    }

    public void renameFolder(String from, String to) throws IOException {
        ftpClient.rename(from, to);
    }

    public void moveFile(String from, String to) throws IOException {
        ftpClient.rename(from, to);
    }

    public void moveFolder(String from, String to) throws IOException {
        ftpClient.rename(from, to);
    }

    // get directory of current working directory
    public String getWorkingDirectory() throws IOException {
        return ftpClient.printWorkingDirectory();
    }
}
