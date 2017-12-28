package service.soccerGame;

import service.core.Event;
import service.core.Event.EventType;
import service.core.Event.Team;
import service.core.Game;

import java.util.List;

import service.core.AbstractGameService;
import service.core.GameService;

/**
 * Implementation of Game Service for a soccer game
 *
 */
public class SoccerService extends AbstractGameService implements GameService {	
	@Override
	public List<Event> getEvents(Game g) {
		return g.getEvents();
	}
	
	@Override
	public void addEvent(Game g, Event e) {
		if (e.eventType == EventType.GOAL) {
			if (e.team == Team.HOME) {
				g.homeScore = "" + (Integer.parseInt(g.homeScore) + 1);
			} else {
				g.awayScore = "" + (Integer.parseInt(g.awayScore) + 1);
			}
		}
		g.addEvent(e);
	}
	
	@Override
	public Game createGame(String prefix) {
		return new Game(generateReference(prefix));
	}
}
