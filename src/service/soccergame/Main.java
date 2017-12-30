package service.soccergame;

import java.util.ArrayList;
import java.lang.Thread;

public class Main {
  public static final int NUMBER_OF_PRODUCERS = 3;
  public static final String[] TOPICS = {
    "MUvPSG", "CPvEVE", "MCvLIV"
  };

  public static void main(String[] args) {
    ArrayList<Thread> list = new ArrayList<Thread>();
    for(int i=0; i<NUMBER_OF_PRODUCERS; i++) {
      list.add(new Thread(new SoccerProducer(TOPICS[i])));
      list.get(i).start();
    }
  }

}
