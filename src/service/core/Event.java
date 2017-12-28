package service.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
	private static DateFormat TimeFormat = new SimpleDateFormat("HH:mm");
	
	public enum EventType{
		GOAL, POINT, PENALTY, FREE, RED_CARD, YELLOW_CARD, HALF_TIME, FULL_TIME, SECOND_HALF, START;
	}
	
	public enum Team {
		HOME, AWAY
	}
	
	public Event (int p, EventType e, Team t) {
		player = p;
		eventType = e;
		team = t;
		time = TimeFormat.format(new Date());
		status = "";
	}
	
	public Event setStatus (String s) {
		this.status = s;
		return this;
	}
	
	public String status;
	public int player;
	public EventType eventType;
	public Team team;
	public String time;
}
