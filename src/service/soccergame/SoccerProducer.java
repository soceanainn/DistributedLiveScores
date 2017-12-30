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

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SoccerProducer {
	private String game;
	private long startTime;
	private Scanner read;

	public SoccerProducer(String game) {
		try {
			this.read = new Scanner(new File("../match.txt"));
		} catch(FileNotFoundException fe) {fe.printStackTrace();}
		this.game = game;
	}

	public void startGameBroadcast() {
		this.startTime = System.currentTimeMillis();
		ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    try {
      Connection connection = connectionFactory.createConnection();
      connection.setClientID("sender");
      Session session = connection.createSession(false,
          Session.AUTO_ACKNOWLEDGE);
			Destination matchDestination = session.createTopic(this.game);
			MessageProducer matchProducer = session.createProducer(matchDestination);
			update(matchProducer, session);
    } catch (JMSException e) { e.printStackTrace(); }
	}

	public void update(MessageProducer matchProducer, Session session) {
		Event currentEvent;
		do {
			try {
				Thread.sleep(60000);
			} catch(InterruptedException ie) {ie.printStackTrace();}
			currentEvent = getNextEvent();
			try {
				ObjectMessage event = session.createObjectMessage(currentEvent);
				matchProducer.send(event);
			} catch(JMSException je) {je.printStackTrace();}
		} while(currentEvent.eventType != EventType.FULL_TIME);
	}

	// placeholder
	public Event getNextEvent() {
		String[] event = read.nextLine().split("\\s+");
		return new Event(Integer.valueOf(event[0]), EventType.getTypeFromString(event[1])
			,Team.getTeamFromString(event[2]), System.currentTimeMillis()-this.startTime);
	}

	public static void main(String[] args) {
		SoccerProducer sp = new SoccerProducer("MUvPSG");
		sp.startGameBroadcast();
	}
}
