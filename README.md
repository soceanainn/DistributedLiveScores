# DistributedLiveScores
A distributed live scores application

To run the program run server.java then main.java (both are in src.client).

# To-Do
* Add 2 clients that will create games and add events to those games
* In Main.java correctly parse the list of all games before asking the user to pick a game
* In SoccerService.java (service.soccerGame) and in Event.java change the way the time is represented from a time in HH:mm to a string of   xx minutes (difference in current event time and start time of game).
* When creating a game, change the POST method to take the home and away team as input to be used as a prefix instead of just a random prefix (i.e. that home and away teams will be MU and PSG and the prefix will then be MUvsPSG).
* Implement actors so that we can listen to a game and get new updates without making repeated get requests for a game
* Write report
