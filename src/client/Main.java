package client;

import java.lang.Thread;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;

public class Main {
  public static final int NUMBER_OF_CONSUMERS = 3;
  public static final String[] TOPICS = {
    "MUvPSG", "CPvEVE", "MCvLIV"
  };
  public static final String[] IDS = {
    "1","2","3"
  };

  public static void main(String[] args) {
    if(args.length != 1) {
      System.out.println("Incorrect usage: <id>");
      return;
    }
    CountDownLatch consumersFinished = new CountDownLatch(NUMBER_OF_CONSUMERS);
    try {
      ConnectionFactory factory = (ConnectionFactory)
          new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
      Connection connection = factory.createConnection();
      connection.setClientID(args[0]);
      Session session = connection.createSession(false,
          Session.CLIENT_ACKNOWLEDGE);
      ArrayList<Thread> list = new ArrayList<Thread>();
      for(int i=0; i<NUMBER_OF_CONSUMERS; i++) {
        list.add(new Thread(
            new SoccerConsumer(TOPICS[i], IDS[i], connection, session, consumersFinished)));
        list.get(i).start();
      }
      // wait for consumers to finish and their threads to finish before closing
      // connection
      try {
        consumersFinished.await();
      } catch(InterruptedException ie) {ie.printStackTrace();}
      connection.close();
    }
    catch(JMSException je) {
      je.printStackTrace();
    }
  }
}
