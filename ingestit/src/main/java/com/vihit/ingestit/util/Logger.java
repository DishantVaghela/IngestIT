package com.vihit.ingestit.util;

public class Logger extends org.apache.log4j.Logger {

	protected Logger(String name) {
		super(name);
	}

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();

	private static final String REASON_TOKEN_MESSAGE = ", reason :";

	private static final String MESSAGE_PREFIX = "[ ";

	private static final String MESSAGE_CONCATOR = " ] : ";

	public static void logError(String moduleName, String strMessage) {
		logger.error(MESSAGE_PREFIX + moduleName + MESSAGE_CONCATOR + strMessage);
	}

	public static void logDebug(String moduleName, String strMessage) {
		logger.debug(MESSAGE_PREFIX + moduleName + MESSAGE_CONCATOR + strMessage);
	}

	public static void logInfo(String moduleName, String strMessage) {
		logger.info(MESSAGE_PREFIX + moduleName + MESSAGE_CONCATOR + strMessage);
	}

	public static void logWarn(String moduleName, String strMessage) {
		logger.warn(MESSAGE_PREFIX + moduleName + MESSAGE_CONCATOR + strMessage);
	}

	public static void setLogger(org.apache.log4j.Logger newlogger) {
		logger = newlogger;
	}

	public static org.apache.log4j.Logger getLogger() {
		return logger;
	}
}
