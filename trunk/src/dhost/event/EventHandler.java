package dhost.event;

public interface EventHandler {
	public void handleEvent(Event event);
	public String getStatus();
}
