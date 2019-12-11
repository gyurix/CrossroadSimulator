package gyurix.paralpr.task3;

import gyurix.paralpr.task3.entities.Entity;
import gyurix.paralpr.task3.enums.RoadSide;
import gyurix.paralpr.task3.enums.SemaphoreMode;

import java.util.Iterator;

public class PeopleWalker implements Runnable {
  private final Crossroad road;
  private final RoadSide side;

  public PeopleWalker(Crossroad road, RoadSide side) {
    this.road = road;
    this.side = side;
  }

  @Override
  public void run() {
    System.out.println("[PeopleWalker - " + side + "] Started");
    while (true) {
      Entity e = null;
      RoadSide target = null;
      synchronized (road.getSemaphoreMutex()) {
        if (road.isFinished()) {
          System.out.println("[PeopleWalker - " + side + "] Finished");
          return;
        }
        SemaphoreMode mode = road.getSemaphoreMode();
        synchronized (road.waitingPersons) {
          Iterator<Entity> it = road.waitingPersons.get(side).iterator();
          while (it.hasNext()) {
            e = it.next();
            if (mode.allows(e)) {
              target = e.getAt().apply(e.getDir());
              synchronized (road.getRoadLock().get(target)) {
                if (road.onRoad.get(target) == null) {
                  road.onRoad.put(target, e);
                  it.remove();
                  break;
                }
              }
            }
            e = null;
          }
        }
      }
      if (e != null) {
        try {
          System.out.println("[PeopleWalker - " + side + "] Enter road - " + e);
          Thread.sleep(Settings.PEOPLE_WALK_TIME);
          System.out.println("[PeopleWalker - " + side + "] Exit road - " + e);
        } catch (InterruptedException ignored) {
        }

        synchronized (road.getRoadLock().get(target)) {
          road.onRoad.put(target, null);
        }
        synchronized (road.completedPersons) {
          road.completedPersons.get(target).add(e);
        }
      }
      try {
        Thread.sleep(Settings.PEOPLE_WALKER_LOOP_DELAY);
      } catch (InterruptedException ignored) {
      }
    }
  }
}
