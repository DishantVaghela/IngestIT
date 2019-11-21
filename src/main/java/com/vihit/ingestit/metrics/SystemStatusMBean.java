package com.vihit.ingestit.metrics;

public interface SystemStatusMBean {

	Integer getNumberOfFilesIngested();
	Integer getNumberOfFilesRejected();
	Long getIngestionStartTime();
	Long getSessionId();
}
