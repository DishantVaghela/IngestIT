package com.vihit;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HDFSIngestionProcess implements Runnable {

    private List<Path> fileList;
    private IngestionModel configModel;

    HDFSIngestionProcess(List<Path> fileList, IngestionModel configModel)
    {
        this.fileList = fileList;
        this.configModel = configModel;
    }

    @Override
    public void run() {
        try {
            for (Path file : fileList) {
                System.out.println("Processing file "+file.getFileName());
                ingestFile(file, Paths.get(configModel.getOutputPath()));
                archiveFile(file, configModel.getArchivePath());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void ingestFile(Path file,Path outputPath)
    {
        try {
            Configuration conf = new Configuration();
            conf.set("fs.default.name","localhost:9000");
            //conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            //conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
            org.apache.hadoop.fs.FileSystem fs = org.apache.hadoop.fs.FileSystem.get(conf);
            System.out.println(fs.exists(new org.apache.hadoop.fs.Path("/user/vihit")));
            fs.copyFromLocalFile(new org.apache.hadoop.fs.Path(file.toString()),new org.apache.hadoop.fs.Path(outputPath.toString()+"/"+file.getFileName().toString()));

        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void archiveFile(Path file, String archivePath)
    {
        try {
            FileUtils.moveFile(file.toFile(), Paths.get(archivePath+"/"+file.getFileName().toString()).toFile());
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
