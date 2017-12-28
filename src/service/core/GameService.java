package service.core;

import java.util.List;

/**
 * Interface to define the behaviour of a game service.
 */
public interface GameService {
	public List<Event> getEvents(Game g);

	public void addEvent(Game g, Event e);

	public Game createGame(String t1, String t2);
}
