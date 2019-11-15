package com.vihit.ingestIT;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;

import com.vihit.ingestIT.model.IngestionModel;
import com.vihit.ingestIT.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class HDFSIngestionProcess implements Runnable {

	private List<Path> fileList;
	private IngestionModel configModel;
	private static String MODULE = "HDFS-INGESTION";
	public static AtomicLong filesIngested = new AtomicLong();
	public static AtomicLong filesErrored = new AtomicLong();

	HDFSIngestionProcess(List<Path> fileList, IngestionModel configModel) {
		this.fileList = fileList;
		this.configModel = configModel;
	}

	@Override
	public void run() {
		try {
			for (Path file : fileList) {
				ingestFile(file, Paths.get(configModel.getOutputPath()));
				archiveFile(file, configModel.getArchivePath());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void ingestFile(Path file, Path outputPath) {
		try {
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://localhost:9000");

			org.apache.hadoop.fs.FileSystem fs = org.apache.hadoop.fs.FileSystem.get(conf);
			Logger.logInfo(MODULE, "Ingesting file " + file.getFileName().toString() + " to " + outputPath.toString());
			fs.copyFromLocalFile(new org.apache.hadoop.fs.Path(file.toString()),
					new org.apache.hadoop.fs.Path(outputPath.toString() + "/" + file.getFileName().toString()));
			filesIngested.incrementAndGet();
		} catch (Exception ex) {
			filesErrored.incrementAndGet();
			Logger.logError(MODULE,
					"Error while ingesting file " + file.getFileName().toString() + " : " + ex.getMessage());
		}
	}

	public static void archiveFile(Path file, String archivePath) {
		try {
			Logger.logInfo(MODULE, "Archiving file " + file.getFileName().toString() + " to " + archivePath);
			FileUtils.moveFile(file.toFile(), Paths.get(archivePath + "/" + file.getFileName().toString()).toFile());
		} catch (Exception ex) {
			Logger.logError(MODULE,
					"Error while archiving file " + file.getFileName().toString() + " : " + ex.getMessage());
		}
	}
}
