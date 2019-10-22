package com.vihit;

import java.io.Serializable;

public class IngestionModel implements Serializable {

    private String inputPath;
    private String outputPath;
    private String fileNameRule;
    private String archivePath;

    public String getArchivePath() {
        return archivePath;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }

    public String getFileNameRule() {
        return fileNameRule;
    }

    public void setFileNameRule(String fileNameRule) {
        this.fileNameRule = fileNameRule;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public String toString() {
        return "IngestionModel{" +
                "inputPath='" + inputPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", fileNameRule='" + fileNameRule + '\'' +
                ", archivePath='" + archivePath + '\'' +
                '}';
    }
}
