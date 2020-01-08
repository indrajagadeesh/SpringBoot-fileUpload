package com.zoho.zlabs.service;

import com.zoho.zlabs.exception.FileStorageException;
import com.zoho.zlabs.exception.MyFileNotFoundException;
import com.zoho.zlabs.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final Path fileDownloadLocation;
    private String args;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileDownloadLocation = Paths.get(fileStorageProperties.getDownloadDir())
                .toAbsolutePath().normalize();
        this.args = fileStorageProperties.getCommands();
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileDownloadLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String s = null;
        File fout = null;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String outputFile = fileName + ".txt";
            fout = new File(fileDownloadLocation.toString() + "/" + outputFile);
            String command = args + " " + fileStorageLocation.toString() + "/" + fileName;
            System.out.println(command);
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            while ((s = stdInput.readLine()) != null) {
                bw.write(s);
                bw.newLine();
            }
            bw.close();
            if ((s = stdError.readLine()) != null) {
                System.out.println("Here is the standard error of the command (if any):\n");
                System.out.println(s);
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
            return outputFile;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileDownloadLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
