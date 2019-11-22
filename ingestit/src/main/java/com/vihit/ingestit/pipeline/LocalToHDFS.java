package com.vihit.ingestit.pipeline;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.vihit.ingestit.HDFSIngestion;
import com.vihit.ingestit.model.*;
import com.vihit.ingestit.util.Logger;

public class LocalToHDFS {

	private static final String MODULE = "LocalToHDFSPipeline";
	private ThreadPoolExecutor ingestionThreadPool;
	Pipeline pipeline;
	int maxIngestionSize;
	int filesPerThread;
	
	public LocalToHDFS(Pipeline pipeline) {
		this.pipeline = pipeline;
	}

	public void executePipeline() {
		prepareDivideIngest();
	}

	private void prepareDivideIngest() {
		List<Path> fileList = listFiles(pipeline.getIn().get("input"), pipeline.getIn().get("fileNameRule"));
		maxIngestionSize = Integer.parseInt(pipeline.getIn().get("maxIngestionSize"));
		filesPerThread = Integer.parseInt(pipeline.getIn().get("filesPerThread"));
		int filesInLastSubList;
		if (fileList != null && fileList.size() > 0) {
			if (maxIngestionSize > fileList.size())
				maxIngestionSize = fileList.size();
			List<Path> mainSubList = fileList.subList(0, maxIngestionSize);

			int totalSubLists = calculateTotalSubLists();
			filesInLastSubList = maxIngestionSize % filesPerThread;

			execute(mainSubList, totalSubLists, filesInLastSubList);
		}
	}

	public List<Path> listFiles(String inputPath, final String fileNameRules) {
		List<Path> fileList = null;
		try {
			fileList = Files.walk(Paths.get(inputPath))
					.filter(f -> Files.isRegularFile(f) && f.getFileName().toString().matches(fileNameRules))
					.collect(Collectors.toList());
		} catch (Exception ex) {
			Logger.logError(MODULE, "Error occurred while accessing input folder : " + inputPath);
		}
		return fileList;
	}

	public int calculateTotalSubLists() {
		int totalSubLists = maxIngestionSize / filesPerThread;
		int filesInLastSubList = maxIngestionSize % filesPerThread;
		if (filesInLastSubList != 0) {
			totalSubLists++;
		}

		return totalSubLists;
	}

	public void execute(List<Path> mainFileList, int totalSubLists, int fileCountInLastSubList) {

		ingestionThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(totalSubLists);
		
		for (int i = 0; i < totalSubLists; i++) {
			List<Path> subList;
			if (i == totalSubLists - 1 && fileCountInLastSubList != 0) {
				subList = mainFileList.subList(filesPerThread * i, fileCountInLastSubList + filesPerThread * i);
			} else {
				subList = mainFileList.subList(filesPerThread * i, filesPerThread + filesPerThread * i);
			}
			if (subList.size() > 0) {
				ingestionThreadPool.execute(new HDFSIngestion(subList, pipeline));
				System.out.println("Creating thread with list size : " + subList.size());
			}
		}
	}
}
