package gyurix.paralpr.task3;

import gyurix.paralpr.task3.entities.Car;
import gyurix.paralpr.task3.entities.Entity;
import gyurix.paralpr.task3.entities.Person;
import gyurix.paralpr.task3.enums.RoadSide;
import gyurix.paralpr.task3.enums.SemaphoreMode;
import gyurix.paralpr.task3.generators.CarGenerator;
import gyurix.paralpr.task3.generators.PersonGenerator;
import lombok.Getter;

import java.util.*;

public class Crossroad implements Runnable {
  final EnumMap<RoadSide, List<Entity>> completedCars = new EnumMap<>(RoadSide.class);
  final EnumMap<RoadSide, List<Entity>> completedPersons = new EnumMap<>(RoadSide.class);
  final EnumMap<RoadSide, Entity> onRoad = new EnumMap<>(RoadSide.class);
  final EnumMap<RoadSide, List<Entity>> waitingCars = new EnumMap<>(RoadSide.class);
  final EnumMap<RoadSide, List<Entity>> waitingPersons = new EnumMap<>(RoadSide.class);
  @Getter
  private final EnumMap<RoadSide, Object> roadLock = new EnumMap<>(RoadSide.class);
  @Getter
  private final Object semaphoreMutex = new Object();
  @Getter
  private boolean finished;
  @Getter
  private SemaphoreMode semaphoreMode = SemaphoreMode.FRONT_BACK;

  public int count(EnumMap<RoadSide, List<Entity>> map) {
    int count = 0;
    for (List<Entity> l : map.values())
      count += l.size();
    return count;
  }

  public void run() {
    System.out.println("[CrossRoad] Started semaphore switcher");
    while (true) {
      synchronized (semaphoreMutex) {
        if (finished) {
          System.out.println("[CrossRoad] Finished semaphore switcher");
          return;
        }
        semaphoreMode = SemaphoreMode.values()[(semaphoreMode.ordinal() + 1) % SemaphoreMode.values().length];
        System.out.println("[CrossRoad] Set semaphore mode to " + semaphoreMode);
      }
      try {
        Thread.sleep(Settings.SEMAPHORE_SWITCH_DELAY);
      } catch (InterruptedException ignored) {
      }
    }
  }

  public void setFinished() {
    synchronized (semaphoreMutex) {
      this.finished = true;
    }
  }

  public void start() {
    for (RoadSide rs : RoadSide.values()) {
      waitingCars.put(rs, new ArrayList<>());
      waitingPersons.put(rs, new ArrayList<>());
      onRoad.put(rs, null);
      roadLock.put(rs, new Object());
      completedCars.put(rs, new ArrayList<>());
      completedPersons.put(rs, new ArrayList<>());
    }
    Task3.createAndStartThread(new CarGenerator(this, waitingCars), "CarGenerator");
    Task3.createAndStartThread(new PersonGenerator(this, waitingPersons), "PersonGenerator");
    Task3.createAndStartThread(this, "Crossroad");
    for (RoadSide rs : RoadSide.values()) {
      Task3.createAndStartThread(new PeopleWalker(this, rs), "PeopleWalk - " + rs);
      Task3.createAndStartThread(new CarGo(this, rs), "CarGo - " + rs);
    }
  }
}
