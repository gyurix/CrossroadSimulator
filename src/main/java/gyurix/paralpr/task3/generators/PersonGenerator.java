package gyurix.paralpr.task3.generators;

import gyurix.paralpr.task3.Crossroad;
import gyurix.paralpr.task3.Settings;
import gyurix.paralpr.task3.entities.Entity;
import gyurix.paralpr.task3.enums.Direction;
import gyurix.paralpr.task3.entities.Person;
import gyurix.paralpr.task3.enums.RoadSide;

import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class PersonGenerator implements Runnable {
  private final Random rand = new Random();
  private final Crossroad road;
  private final Object semaphoreMutex;
  private final EnumMap<RoadSide, List<Entity>> waiting;

  public PersonGenerator(Crossroad road, EnumMap<RoadSide, List<Entity>> waiting) {
    this.road = road;
    this.semaphoreMutex = road.getSemaphoreMutex();
    this.waiting = waiting;
  }


  public void run() {
    System.out.println("[PersonGenerator] Started");
    while (true) {
      synchronized (semaphoreMutex) {
        if (road.isFinished()) {
          System.out.println("[PersonGenerator] Stopped");
          return;
        }
        synchronized (waiting) {
          Person p = new Person(RoadSide.values()[rand.nextInt(4)], rand.nextBoolean() ? Direction.LEFT : Direction.RIGHT);
          waiting.get(p.getAt()).add(p);
          System.out.println("[PersonGenerator] Generated " + p);
        }
      }
      try {
        Thread.sleep(Settings.PERSON_GENERATION_RATE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
