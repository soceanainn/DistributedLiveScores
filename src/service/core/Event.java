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

		public static EventType getTypeFromString(String s) {
			for(EventType e : EventType.values()) {
				if(e.toString().equals(s)) return e;
			}
			return null;
		}
	}

	public enum Team {
		HOME, AWAY;

		public static Team getTeamFromString(String s) {
			for(Team t : Team.values()) {
				if(t.toString().equals(s)) return t;
			}
			return null;
		}
	}

	public Event (int p, EventType e, Team t, long time) {
		player = p;
		eventType = e;
		team = t;
		this.time = String.valueOf(Math.round(time / 60000)) + "m";
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
