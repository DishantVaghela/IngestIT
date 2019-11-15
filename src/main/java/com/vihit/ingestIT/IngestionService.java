package com.vihit.ingestIT;

import metrics.*;
import javax.management.*;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.vihit.ingestIT.config.ConfigReader;
import com.vihit.ingestIT.model.IngestionModel;
import com.vihit.ingestIT.util.*;

public class IngestionService {

	private ThreadPoolExecutor ingestionThreadPool;
	private LinkedBlockingQueue<Runnable> linkedQueue;
	private String sourcePath = null;
	private IngestionModel configModel = null;
	private Thread pollingThread;
	private static String MODULE = "INGESTION-SERVICE";

	public static void main(String[] args) throws MalformedObjectNameException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException, InstanceNotFoundException {

		IngestionService ingestionService = new IngestionService();
		if (args != null && args.length > 0 && args[0].trim().length() > 0)
			ingestionService.sourcePath = args[0];
		else {
			Logger.logError(MODULE, "Please provide the resources folder path to IngestIT");
			System.exit(1);
		}
		SystemStatus systemStatus = new SystemStatus();
		MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
		ObjectName objectName = new ObjectName("com.vihit.ingestIT:name=IngestIT");
		platformMBeanServer.registerMBean(systemStatus, objectName);

		try {
			ingestionService.startService();
		} catch (Exception e) {
			e.printStackTrace();
		}

		systemStatus.backgroundThread.interrupt();
	}

	private void startService() throws Exception {
		init();

		if (configModel != null) {
			pollingThread = new Thread(new IngestionPoller());
			pollingThread.setPriority(6);
			pollingThread.start();

			linkedQueue = new LinkedBlockingQueue<Runnable>();
			ingestionThreadPool = new ThreadPoolExecutor(2, 2, 3600000L, TimeUnit.SECONDS, linkedQueue);
		}
	}

	private void init() {
		configModel = ConfigReader
				.readIngestionModel(sourcePath + Constants.PATH_SEPERATOR + Constants.INGESTION_CONFIG_NAME);
	}

	private class IngestionPoller implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					prepareDivideIngest();
					System.out.println("Will be back in a minute");
					System.out.println("Files ingested: "+HDFSIngestionProcess.filesIngested);
					System.out.println("Files errored: "+HDFSIngestionProcess.filesErrored);
					Thread.sleep(60000);
				}
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void prepareDivideIngest() {
		List<Path> fileList = listFiles(configModel.getInputPath(), configModel.getFileNameRule());
		int maxIngestionSize = 100;
		if (fileList != null && fileList.size() > 0) {
			if (maxIngestionSize > fileList.size())
				maxIngestionSize = fileList.size();
			List<Path> mainSubList = fileList.subList(0, maxIngestionSize);

			int totalSubList = 10;
			int subListSize = maxIngestionSize / totalSubList;

			for (int i = 0; i < totalSubList; i++) {
				List<Path> subList = mainSubList.subList(subListSize * i, subListSize + subListSize * i);
				ingestionThreadPool.execute(new HDFSIngestionProcess(subList, configModel));
			}
		}
	}

	public static List<Path> listFiles(String inputPath, final String fileNameRules) {
		List<Path> fileList = null;
		try {
			fileList = Files.walk(Paths.get(inputPath))
					.filter(f -> Files.isRegularFile(f) && f.getFileName().toString().matches(fileNameRules))
					.collect(Collectors.toList());
		} catch (Exception ex) {
			Logger.logError(MODULE, "Error occurred while accessing input folder : "+inputPath);
		}
		return fileList;
	}

}
