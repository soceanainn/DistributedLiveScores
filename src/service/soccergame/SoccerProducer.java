package service.soccergame;

import service.core.Event;
import service.core.Event.EventType;
import service.core.Event.Team;
import service.core.Game;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import java.lang.InterruptedException;
import java.lang.Runnable;

import service.core.AbstractGameService;
import service.core.GameService;

import javax.jms.JMSException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.ObjectMessage;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SoccerProducer implements Runnable {
	private String game;
	private long startTime;
	private Scanner read;

	public SoccerProducer(String game) {
		try {
			this.read = new Scanner(new File("../match.txt"));
		} catch(FileNotFoundException fe) {fe.printStackTrace();}
		this.game = game;
	}

	public void run() {
		this.startTime = System.currentTimeMillis();
		ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    try {
      Connection connection = connectionFactory.createConnection();
      connection.setClientID(game+"sender");
      Session session = connection.createSession(false,
          Session.AUTO_ACKNOWLEDGE);
			Destination matchDestination = session.createTopic(this.game);
			MessageProducer matchProducer = session.createProducer(matchDestination);
			// set time-to-live so messages last for length of game
			matchProducer.setTimeToLive(7200000); // 7200000 = 120 minutes = 2 hours
			update(matchProducer, session);
    } catch (JMSException e) { e.printStackTrace(); }
	}

	public void update(MessageProducer matchProducer, Session session) {
		Event currentEvent;
		do {
			try {
				Thread.sleep(5000 + ThreadLocalRandom.current().nextInt(0, 1001));
			} catch(InterruptedException ie) {ie.printStackTrace();}
			currentEvent = getNextEvent();
			try {
				ObjectMessage event = session.createObjectMessage(currentEvent);
				matchProducer.send(event);
			} catch(JMSException je) {je.printStackTrace();}
		} while(currentEvent.eventType != EventType.FULL_TIME);
	}

	public Event getNextEvent() {
		String[] event = read.nextLine().split("\\s+");
		return new Event(Integer.valueOf(event[0]), EventType.getTypeFromString(event[1])
			,Team.getTeamFromString(event[2]), System.currentTimeMillis()-this.startTime);
	}
}
