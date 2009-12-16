package dhost.event;

public interface EventMonitor {
	public void setMonitorVoteHandler(MonitorVoteHandler monitorVoteHandler);
	public void monitorEvent(Event event);
	
}
