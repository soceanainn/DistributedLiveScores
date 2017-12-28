package client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import service.core.Event;
import service.core.Event.Team;

public class Main {
	public static final String SOCCER_SERVICE = "http://localhost:9001/games";

	static Type type = new TypeToken<List<Event>>() {}.getType();
	static Gson gson = new Gson();

	/**
	 * This prints out all the events for a certain game.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// Create the soccer service
		ClientResource soccerService = new ClientResource(SOCCER_SERVICE);
		
		// Get all games from a simple get request, should be parsed into a list of references
		System.out.println(soccerService.get().getText());
		
		// Should read the game ID from the user
		String gameID = "MUvsPSG001000";
		
		ClientResource game = new ClientResource(SOCCER_SERVICE+ "/" + gameID);
		String json = game.get().getText();
		// Parse json to events
		List<Event> events = gson.fromJson(json, type);

		// Retrieve quotations from the broker and display them...
		for (Event e : events) {
			displayEvent(e);
		}
	}

	/**
	 * Display an event
	 * 
	 * @param event
	 */
	public static void displayEvent(Event event) {
		switch (event.eventType) {
			case GOAL:
				System.out.println(
						event.time + ": #" + event.player + " scored a goal for the " + (event.team == Team.HOME ? "home"
								: "away") + " team.");
				break;
				
			case FREE:
				System.out.println(event.time + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was awarded a free.");
				break;
				
			case START: 
				System.out.println(event.time + ": The match has begun.");
				break;
				
			case FULL_TIME:
				System.out.println(event.time + ": Full time.");
				break;
				
			case HALF_TIME:
				System.out.println(event.time + ": Half time.");
				break;
				
			case SECOND_HALF:
				System.out.println(event.time + ": The second half has begun.");
				break;
				
			case PENALTY:
				System.out.println(event.time + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was awarded a penalty.");
				break;
				
			case POINT:
				System.out.println(
						event.time + ": #" + event.player + " scored a point for the " + (event.team == Team.HOME ? "home"
								: "away") + " team.");
				break;
				
			case RED_CARD:
				System.out.println(event.time + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was given a red card.");
				break;
				
			case YELLOW_CARD:
				System.out.println(event.time + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was given a yellow card.");
				break;
		}
	}
}
