package com.vihit.ingestit;

import javax.management.*;
import java.lang.management.ManagementFactory;
import com.vihit.ingestit.config.ConfigReader;
import com.vihit.ingestit.model.IngestionModel;
import com.vihit.ingestit.util.*;
import com.vihit.ingestit.metrics.*;

public class IngestionService {

	// private LinkedBlockingQueue<Runnable> linkedQueue;
	private String sourcePath = null;
	private IngestionModel ingestionModel = null;
	private Thread pollingThread;
	private static String MODULE = "INGESTION-SERVICE";
	public static Long startTime = System.currentTimeMillis();
	private static String applicationName;
	CommonResourceManager commonResourceManager = CommonResourceManager.getInstance();
	Ingestor ingestor = new Ingestor();

	public static void main(String[] args) {

		try {
			IngestionService ingestionService = new IngestionService();
			if (args != null && args.length > 1 && args[0].trim().length() > 0 && args[1].trim().length() > 0) {
				Logger.logInfo(MODULE, "Starting IngestIT");
				ingestionService.sourcePath = args[0];
				applicationName = args[1].trim();
			} else {
				Logger.logError(MODULE, "Please provide the resources folder path to IngestIT");
				System.exit(1);
			}

			SystemStatus systemStatus = new SystemStatus();
			MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName("com.vihit.ingestit:name=" + applicationName);

			platformMBeanServer.registerMBean(systemStatus, objectName);
			ingestionService.startService();

		} catch (MalformedObjectNameException malformedObjectNameException) {

		} catch (InstanceAlreadyExistsException instanceAlreadyExistsException) {

		} catch (MBeanRegistrationException mBeanRegistrationException) {

		} catch (NotCompliantMBeanException notCompliantMBeanException) {

		} catch (InstanceNotFoundException instanceNotFoundException) {

		} catch (Exception e) {
			Logger.logError(MODULE, "Some error occurred : " + e);
		}
	}

	private void startService() throws Exception {
		init();

		if (ingestionModel != null) {
			pollingThread = new Thread(new IngestionPoller());
			pollingThread.setPriority(6);
			pollingThread.start();

			// linkedQueue = new LinkedBlockingQueue<Runnable>();
			// ingestionThreadPool = new ThreadPoolExecutor(2, 2, 3600000L,
			// TimeUnit.SECONDS, linkedQueue);
			// ingestionThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		}
	}

	private void init() {
		ingestionModel = ConfigReader
				.readIngestionModel(sourcePath + Constants.PATH_SEPERATOR + Constants.INGESTION_CONFIG_NAME);
		commonResourceManager.setConnectionModel(ConfigReader
				.readConnectionModel(sourcePath + Constants.PATH_SEPERATOR + Constants.CONNECTION_CONFIG_NAME));
	}

	private class IngestionPoller implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					ingestor.executeIngestionModel(ingestionModel);
					System.out.println("Will be back in a few minutes");
					Thread.sleep(120 * 1000);
				}
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
