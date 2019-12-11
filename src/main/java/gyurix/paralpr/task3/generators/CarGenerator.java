package gyurix.paralpr.task3.generators;

import gyurix.paralpr.task3.Crossroad;
import gyurix.paralpr.task3.Settings;
import gyurix.paralpr.task3.entities.Car;
import gyurix.paralpr.task3.entities.Entity;
import gyurix.paralpr.task3.enums.Direction;
import gyurix.paralpr.task3.enums.RoadSide;

import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class CarGenerator implements Runnable {
  private final Random rand = new Random();
  private final Crossroad road;
  private final Object semaphoreMutex;
  private final EnumMap<RoadSide, List<Entity>> waiting;

  public CarGenerator(Crossroad road, EnumMap<RoadSide, List<Entity>> waiting) {
    this.road = road;
    this.semaphoreMutex = road.getSemaphoreMutex();
    this.waiting = waiting;
  }


  public void run() {
    System.out.println("[CarGenerator] Started");
    while (true) {
      synchronized (semaphoreMutex) {
        if (road.isFinished()) {
          System.out.println("[CarGenerator] Stopped");
          return;
        }
        synchronized (waiting) {
          Car p = new Car(RoadSide.values()[rand.nextInt(4)], Direction.values()[rand.nextInt(3)]);
          waiting.get(p.getAt()).add(p);
          System.out.println("[CarGenerator] Generated " + p);
        }
      }
      try {
        Thread.sleep(Settings.CAR_GENERATION_RATE);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
