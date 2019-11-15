package metrics;

public interface SystemStatusMBean {

	Integer getNumberOfFilesIngested();
	Integer getNumberOfFilesRejected();
	Long getNumberOfSecondsRunning();
}
