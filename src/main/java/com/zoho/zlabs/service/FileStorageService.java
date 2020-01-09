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
    private String commands;
    private String commandsIn;
    private String commandsOut;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileDownloadLocation = Paths.get(fileStorageProperties.getDownloadDir())
                .toAbsolutePath().normalize();
        this.commands = fileStorageProperties.getCommands();
        this.commandsIn = fileStorageProperties.getCommandsIn();
        this.commandsOut = fileStorageProperties.getCommandsOut();
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileDownloadLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public String[] storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String s = null;
        String inOut = null;
        String outOut = null;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String outputFile = fileName.split("\\.")[0] +"_out."+fileName.split("\\.")[1];
            String command = this.commands + " -i " + fileStorageLocation.toString() + "/" + fileName +" -y "+fileDownloadLocation.toString()+ "/" + outputFile;
            String command1 = this.commandsIn +" "+ fileStorageLocation.toString() + "/" + fileName;
            String command2 = this.commandsOut + " "+ fileDownloadLocation.toString()+ "/" + outputFile;
            System.out.println(command);
            Process p = Runtime.getRuntime().exec(command);
            Process p1 = Runtime.getRuntime().exec(command1);
            Process p2 = Runtime.getRuntime().exec(command2);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
            BufferedReader stdError1 = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
            BufferedReader stdInput2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            BufferedReader stdError2 = new BufferedReader(new InputStreamReader(p2.getErrorStream()));

            System.out.println("1st command output");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            if ((s = stdError.readLine()) != null) {
                System.out.println("Here is the standard error of the command (if any):\n");
                System.out.println(s);
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
            System.out.println("2nd command output");
            while ((s = stdInput1.readLine()) != null) {
                inOut += s + " ";
            }

            if ((s = stdError1.readLine()) != null) {
                System.out.println("Here is the standard error of the command (if any):\n");
                System.out.println(s);
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
            System.out.println("3rd command output");
            while ((s = stdInput2.readLine()) != null) {
                outOut += s + " ";
            }

            if ((s = stdError2.readLine()) != null) {
                System.out.println("Here is the standard error of the command (if any):\n");
                System.out.println(s);
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
            }
            String[] output = {outputFile,inOut,outOut};
            return output;
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
