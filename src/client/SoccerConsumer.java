package client;

import java.util.List;
import java.lang.Runnable;
import java.util.concurrent.CountDownLatch;

import service.core.Event;
import service.core.Event.EventType;
import service.core.Event.Team;

import javax.jms.Connection;
import javax.jms.Topic;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.ObjectMessage;
import javax.jms.Message;
import javax.jms.TopicSubscriber;


public class SoccerConsumer implements Runnable {

	private String game;
	private String id;
	private Session session;
	private Connection connection;
	private CountDownLatch done;

	public SoccerConsumer(String game, String id, Connection connection, Session session, CountDownLatch done) {
		this.game = game;
		this.id = id;
		this.connection = connection;
		this.session = session;
		this.done = done;
	}

	public void run() {
		try {
			Topic topic = session.createTopic(this.game);
			// create subscriber so that previous events in the game can be received
			// if the user with this.id hasn't seen them yet.
			MessageConsumer consumer = session.createDurableSubscriber(topic,this.id+this.game);
			connection.start();
			Event currentEvent;
			do {
				ObjectMessage message = (ObjectMessage) consumer.receive();
				currentEvent = (Event) message.getObject();
				displayEvent(currentEvent);
				message.acknowledge();
			} while(currentEvent.eventType != EventType.FULL_TIME);
			// tidy up
			consumer.close();
			//session.unsubscribe(this.id+this.game);
			this.done.countDown();
		} catch (JMSException e) { e.printStackTrace(); }
	}

	/**
	 * Display an event
	 *
	 * @param event
	 */
	public void displayEvent(Event event) {
		System.out.println(this.game+":");
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
