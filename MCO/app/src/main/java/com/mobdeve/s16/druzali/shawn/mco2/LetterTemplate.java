package com.mobdeve.s16.druzali.shawn.mco2;

public class LetterTemplate {
    private String name;
    private String fileName;
    private String fileUrl;
    private String description;
    private String fileType;
    private long fileSize;

    public LetterTemplate() {}

    public LetterTemplate(String name, String fileName, String fileUrl, String description, String fileType, long fileSize) {
        this.name = name;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.description = description;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getName() { return name; }
    public String getFileName() { return fileName; }
    public String getFileUrl() { return fileUrl; }
    public String getDescription() { return description; }
    public String getFileType() { return fileType; }
    public long getFileSize() { return fileSize; }

    public void setName(String name) { this.name = name; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFormattedSize() {
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
    }
}