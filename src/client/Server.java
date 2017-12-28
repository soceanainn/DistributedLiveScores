package client;

import service.soccerGame.SoccerApplication;

public class Server {
	public static void main(String[] args) throws Exception {
		// Start the soccer service
		SoccerApplication.main(args);
		// Run the test service - should be removed when we have proper clients 
		service.soccerGame.TestClient.main(args);
	}
}
