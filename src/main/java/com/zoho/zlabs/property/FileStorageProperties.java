package com.zoho.zlabs.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String downloadDir;
    private String commands;
    private String commandsIn;
    private String commandsOut;

    public String getCommandsIn() {
        return commandsIn;
    }

    public void setCommandsIn(String commandsIn) {
        this.commandsIn = commandsIn;
    }

    public String getCommandsOut() {
        return commandsOut;
    }

    public void setCommandsOut(String commandsOut) {
        this.commandsOut = commandsOut;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public String getDownloadDir() {
        return downloadDir;
    }
    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }
    public String getUploadDir() {
        return uploadDir;
    }
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
