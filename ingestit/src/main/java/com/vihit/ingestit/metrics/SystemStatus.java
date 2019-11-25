package com.vihit.ingestit.metrics;

import com.vihit.ingestit.process.*;
import com.vihit.ingestit.IngestionService;

public class SystemStatus implements SystemStatusMBean {

	private Long sessionId;
	private Long ingestionStartTime;

	public SystemStatus() {
		this.ingestionStartTime = IngestionService.startTime;
		this.sessionId = IngestionService.startTime;
	}

	@Override
	public Integer getNumberOfFilesIngested() {
		return HDFSIngestion.filesIngested.intValue();
	}

	@Override
	public Integer getNumberOfFilesRejected() {
		return HDFSIngestion.filesErrored.intValue();
	}

	@Override
	public Integer getNumberOfKafkaFilesIngested() {
		return KafkaFilesIngestion.filesIngested.intValue();
	}

	@Override
	public Integer getNumberOfKafkaFilesRejected() {
		return KafkaFilesIngestion.filesErrored.intValue();
	}
	
	@Override
	public Integer getNumberOfKafkaRecordsIngested() {
		return KafkaFilesIngestion.recordsIngested.intValue();
	}

	@Override
	public Integer getNumberOfKafkaRecordsRejected() {
		return KafkaFilesIngestion.recordsErrored.intValue();
	}
	
	@Override
	public Long getIngestionStartTime() {
		return ingestionStartTime;
	}

	@Override
	public Long getSessionId() {
		return sessionId;
	}

	

}
