package service.soccergame;

import service.core.Event;
import service.core.Event.EventType;
import service.core.Event.Team;
import service.core.Game;

import java.util.List;

import service.core.AbstractGameService;
import service.core.GameService;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.ObjectMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.concurrent.ThreadLocalRandom;

public class SoccerProducer {
	private String game;
	private long startTime;

	public SoccerProducer(String game) {
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
			Thread.sleep(1000);
			currentEvent = getNextEvent();
			ObjectMessage event = session.createObjectMessage(currentEvent);
			matchProducer.send(event);
		} while(currentEvent.eventType != EventType.FULL_TIME);
	}

	// placeholder
	public Event getNextEvent() {
		if(ThreadLocalRandom.current().nextInt(0,101) > 95) {
			return new Event(1, EventType.FULL_TIME, Team.HOME, this.startTime-System.currentTimeMillis());
		}
		else {
			return new Event(1, EventType.GOAL, Team.HOME, this.startTime-System.currentTimeMillis());
		}
	}
}
