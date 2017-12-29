package client;

import java.util.List;

import service.core.Event;
import service.core.Event.EventType;
import service.core.Event.Team;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.ObjectMessage;
import javax.jms.Message;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {

	/**
	 * This prints out all the events for a certain game.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ConnectionFactory factory = (ConnectionFactory)
					new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			Connection connection = factory.createConnection();
			connection.setClientID("receiver");
			Session session = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
			Destination destination = session.createTopic(args[0]);
			MessageConsumer consumer = session.createConsumer(destination);
			connection.start();
			Event currentEvent;
			do {
				ObjectMessage message = (ObjectMessage) consumer.receive();
				currentEvent = (Event) message.getObject();
				displayEvent(currentEvent);
				message.acknowledge();
			} while(currentEvent.eventType != EventType.FULL_TIME);
			connection.close();
		} catch (JMSException e) { e.printStackTrace(); }
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
						event.time + " " + event.status + ": #" + event.player + " scored a goal for the " + (event.team == Team.HOME ? "home"
								: "away") + " team.");
				break;

			case FREE:
				System.out.println(event.time + " " + event.status + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was awarded a free.");
				break;

			case START:
				System.out.println(event.time + " " + event.status + ": The match has begun.");
				break;

			case FULL_TIME:
				System.out.println(event.time + " " + event.status + ": Full time.");
				break;

			case HALF_TIME:
				System.out.println(event.time + " " + event.status + ": Half time.");
				break;

			case SECOND_HALF:
				System.out.println(event.time + " " + event.status + ": The second half has begun.");
				break;

			case PENALTY:
				System.out.println(event.time + " " + event.status + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was awarded a penalty.");
				break;

			case POINT:
				System.out.println(
						event.time + " " + event.status + ": #" + event.player + " scored a point for the " + (event.team == Team.HOME ? "home"
								: "away") + " team.");
				break;

			case RED_CARD:
				System.out.println(event.time + " " + event.status + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was given a red card.");
				break;

			case YELLOW_CARD:
				System.out.println(event.time + " " + event.status + ": #" + event.player + " from the " +  (event.team == Team.HOME ? "home"
						: "away") + " team was given a yellow card.");
				break;
		}
	}
}
