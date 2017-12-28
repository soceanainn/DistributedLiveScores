package service.soccerGame;

import java.io.IOException;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.NamedValue;
import org.restlet.util.Series;
import com.google.gson.Gson;
import client.Main;
import service.core.Event;
import service.core.Event.EventType;
import service.core.Event.Team;

public class TestClient {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ResourceException, IOException {
		Gson gson = new Gson();
		ClientResource client = new ClientResource(Main.SOCCER_SERVICE);
		client.post(gson.toJson("MU:PSG"));
		String location = ((Series<NamedValue<String>>) client.getResponseAttributes().get("org.restlet.http.headers")).getFirstValue("Location");
		System.out.println("URL: " + location);
		ClientResource game = new ClientResource(location);
		
		// Throws error without this - not sure why since it doesn't try to make a PUT request until previous
		// request has returned
		game.get().write(System.out);
		
		Event event = new Event(5, EventType.GOAL, Team.HOME);
		game.put(gson.toJson(event));
		
		event = new Event(1, EventType.RED_CARD, Team.AWAY);
		game.put(gson.toJson(event));
		game.get().write(System.out);
	}
}
