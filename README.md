# DistributedLiveScores
A distributed live scores application

To run the program run server.java then main.java (both are in src.client).

# To-Do
* Add 2 clients - basically copies of test client that doesn't print out anything to create and fill a game with a load of messages
* In Main.java correctly parse the list of all games before asking the user to pick a game
* In SoccerService.java (service.soccerGame) and in Event.java change the way the time is represented from a time in HH:mm to a string of   xx minutes (difference in current event time and start time of game).
* Fix the status message in events, not sure why it's not working and impossible to debug
* Implement actors so that we can listen to a game and get new updates without making repeated get requests for a game
* Write report
