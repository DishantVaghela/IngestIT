package com.vihit.ingestit.metrics;

import com.vihit.ingestit.HDFSIngestion;
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
	public Long getIngestionStartTime() {
		return ingestionStartTime;
	}

	@Override
	public Long getSessionId() {
		return sessionId;
	}

	

}
