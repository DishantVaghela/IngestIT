package metrics;

import com.vihit.ingestIT.HDFSIngestionProcess;

public class SystemStatus implements SystemStatusMBean {

	private Long numberOfSecondsRunning;
	private Integer numberOfFilesIngested;
	private Integer numberOfFilesRejected;
	public Thread backgroundThread;
	
	public SystemStatus() {
		this.backgroundThread = new Thread();
		this.numberOfFilesIngested = 0;
		this.numberOfFilesRejected = 0;
		this.numberOfSecondsRunning = 0L;
		
		this.backgroundThread = new Thread(() -> {
			try {
				while(true)
				{
					numberOfSecondsRunning += 1;
					Thread.sleep(1000L);
				}
			}
			catch(Exception ex) {
				
			}
		});
		this.backgroundThread.setName("Metrics Thread");
		this.backgroundThread.start();
	}

	@Override
	public Integer getNumberOfFilesIngested() {
		return HDFSIngestionProcess.filesIngested.intValue();
	}

	@Override
	public Integer getNumberOfFilesRejected() {
		return HDFSIngestionProcess.filesErrored.intValue();
	}

	@Override
	public Long getNumberOfSecondsRunning() {
		return numberOfSecondsRunning;
	}
	
	
	
}
