package dhost.event;

import dhost.examples.gamedemo.DemoGameEvent;

public class AppEventFactory {

	public static AppEvent createEvent(AppEventType appEventType, String eventData)
	{
		switch (appEventType) {
		
		case DEMO_GAME_EVENT:
			return new DemoGameEvent(eventData);
		default:
			return null;
		}
	}

}
