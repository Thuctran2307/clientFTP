package com.example.HelperFunction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileHandler {
    private static FileHandler instance;

    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    private FileHandler() {
        // Initialize if needed
    }

    public File createFile(String filePath) {
        File file = new File(filePath);

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
                return file;
            } else {
                System.out.println("File already exists.");
                return file;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
            return null;
        }
    }

    public boolean renameFile(String filePath, String newName, String oldName) {
        File oldFile = new File(filePath);
        File newFile = new File(filePath.replace(oldName, "") + newName);

        System.out.println(oldFile.getAbsolutePath());
        System.out.println(newFile.getAbsolutePath());

        if (oldFile.exists()) {
            if (oldFile.renameTo(newFile)) {
                System.out.println("File renamed successfully.");
                return true;
            } else {
                System.out.println("Failed to rename file.");
                return false;
            }
        } else {
            System.out.println("File not found.");
            return false;
        }
    }

    public boolean createFolder(String folderPath) {
        System.out.println(folderPath);
        File folder = new File(folderPath);

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Folder created: " + folder.getAbsolutePath());
                return true;
            } else {
                System.out.println("Failed to create folder.");
                return false;
            }
        } else {
            System.out.println("Folder already exists.");
            return false;
        }
    }

    public boolean deleteFolder(String folderPath) {
        Path path = Paths.get(folderPath);

        try {
            Files.walk(path)
                 .sorted(Comparator.reverseOrder())
                 .map(Path::toFile)
                 .forEach(File::delete);
            System.out.println("Folder deleted: " + folderPath);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to delete folder.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFile(String filePath) {
        Path path = Paths.get(filePath);
    
        try {
            Files.delete(path);
            System.out.println("File deleted: " + filePath);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to delete file.");
            e.printStackTrace();
            return false;
        }
    }
    
}

