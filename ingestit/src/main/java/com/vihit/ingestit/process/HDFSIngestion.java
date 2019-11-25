package com.vihit.ingestit.process;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.vihit.ingestit.model.*;
import com.vihit.ingestit.util.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class HDFSIngestion implements Runnable {

	private List<Path> fileList;
	private Pipeline pipeline;
	private static String MODULE = "HDFS-INGESTION";
	public static AtomicLong filesIngested = new AtomicLong();
	public static AtomicLong filesErrored = new AtomicLong();
	static FileSystem fs = null;

	public HDFSIngestion(List<Path> fileList, Pipeline pipeline) {
		this.fileList = fileList;
		this.pipeline = pipeline;
	}

	private static FileSystem getHDFSFileSystem(Map<String, String> confs) {
		Configuration configuration = new Configuration();
		confs.forEach((key, value) -> configuration.set(key, value));
		try {
			fs = org.apache.hadoop.fs.FileSystem.get(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fs;
	}

	@Override
	public void run() {
		try {
			ConnectionsModel connectionModel = CommonResourceManager.getInstance().getConnectionModel();
			String outputConnection = pipeline.getOut().get("connection");
			fs = getHDFSFileSystem((connectionModel.connections.stream()
					.filter(conn -> conn.name.equalsIgnoreCase(outputConnection)).findFirst()).get().getProps());
			for (Path file : fileList) {
				ingestFile(file, Paths.get(pipeline.getOut().get("output")));
				archiveFile(file, pipeline.getIn().get("archive"));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void ingestFile(Path file, Path outputPath) {
		try {
			Logger.logInfo(MODULE, "Ingesting file " + file.getFileName().toString() + " to " + outputPath.toString());
			fs.copyFromLocalFile(new org.apache.hadoop.fs.Path(file.toString()),
					new org.apache.hadoop.fs.Path(outputPath.toString() + "/" + file.getFileName().toString()));
			filesIngested.incrementAndGet();
		} catch (Exception ex) {
			filesErrored.incrementAndGet();
			ex.printStackTrace();
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
