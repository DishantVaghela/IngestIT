package com.vihit.ingestit.metrics;

public interface SystemStatusMBean {

	Integer getNumberOfFilesIngested();
	Integer getNumberOfFilesRejected();
	Integer getNumberOfKafkaFilesIngested();
	Integer getNumberOfKafkaFilesRejected();
	Integer getNumberOfKafkaRecordsIngested();
	Integer getNumberOfKafkaRecordsRejected();
	Long getIngestionStartTime();
	Long getSessionId();
}
