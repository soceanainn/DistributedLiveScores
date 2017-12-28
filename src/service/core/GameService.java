package service.core;

import java.util.List;

import service.registry.Service;

/**
 * Interface to define the behaviour of a game service.
 */
public interface GameService extends Service {
	public List<Event> getEvents(Game g);

	public void addEvent(Game g, Event e);
	
	public Game createGame(String t1, String t2);
}
