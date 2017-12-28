package service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import service.core.Event.EventType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Game {
	private static DateFormat TimeFormat = new SimpleDateFormat("HH:mm");
	
	public Game (String ref) {
		reference = ref;
		startTime = TimeFormat.format(new Date());
		events.add(new Event(0, EventType.START, null));
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public List<Event> getEvents(){
		return events;
	}

	public String startTime;
	public String homeTeam;
	public String awayTeam;
	public String homeScore = "0";
	public String awayScore = "0";
	
	private List<Event> events = new ArrayList<Event>();
	public String reference;
	
}
