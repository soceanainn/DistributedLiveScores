package service.soccerGame;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.routing.Router;

import com.google.gson.Gson;

import service.core.GameService;
import service.core.Event;
import service.core.Game;

public class SoccerApplication extends Application {
	static Map<String, Game> games = new HashMap<String, Game>();
	static Gson gson = new Gson();
	static GameService gs = new SoccerService();

	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/games", new Restlet() {
			public void handle(Request request, Response response) {
				if (request.getMethod().equals(Method.GET)) {
					String out = "[";
					boolean first = true;
					for (String key : games.keySet()) {
						if (first)
							first = false;
						else
							out += ",";
						String url = request.getResourceRef().getPath() + "/games/" + key;
						out += "{\"reference\" : \"" + key + "\", \"link\":\"" + url + "\"}";
					}
					response.setEntity(out + "]", MediaType.APPLICATION_JSON);
				} else if (request.getMethod().equals(Method.POST)) {
					String prefix = gson.fromJson(request.getEntityAsText(), String.class);
					String[] teams = prefix.split(":");
					Game game = gs.createGame(teams[0], teams[1]);
					if (!games.containsKey(game.reference)) {
						games.put(game.reference, game);
						response.setLocationRef(request.getHostRef() + "/games/" + game.reference);
					} else
						response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
				} else
					response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
			}
		});
		router.attach("/games/{reference}", new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				String id = (String) request.getAttributes().get("reference");
				if (games.containsKey(id)) {
					response.setStatus(Status.SUCCESS_OK);

					if (request.getMethod().equals(Method.GET)) {
						response.setEntity(gson.toJson(games.get(id).getEvents()), MediaType.APPLICATION_JSON);
						response.setStatus(Status.SUCCESS_OK);
					} else if (request.getMethod().equals(Method.PUT)) {
						Event event = gson.fromJson(request.getEntityAsText(), Event.class);
						games.get(id).addEvent(event);
						response.setStatus(Status.SUCCESS_NO_CONTENT);
					} else if (request.getMethod().equals(Method.DELETE)) {
						games.remove(id);
						response.setStatus(Status.SUCCESS_NO_CONTENT);
					}
				} else {
					response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				}
			}
		});

		return router;
	}

	public static void main(String[] args) throws Exception {
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 9001);
		component.getDefaultHost().attach("", new SoccerApplication());
		component.start();
	}
}
