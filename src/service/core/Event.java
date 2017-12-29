package service.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.Serializable;

public class Event implements Serializable {
	public static final long serialVersionUID = 1L;
	private static DateFormat TimeFormat = new SimpleDateFormat("HH:mm");

	public enum EventType{
		GOAL, POINT, PENALTY, FREE, RED_CARD, YELLOW_CARD, HALF_TIME, FULL_TIME, SECOND_HALF, START;
	}

	public enum Team {
		HOME, AWAY
	}

	public Event (int p, EventType e, Team t, long time) {
		player = p;
		eventType = e;
		team = t;
		this.time = Math.round(time / 60000);
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
