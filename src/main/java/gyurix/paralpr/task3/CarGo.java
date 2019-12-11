package gyurix.paralpr.task3;

import gyurix.paralpr.task3.entities.Entity;
import gyurix.paralpr.task3.enums.RoadSide;
import gyurix.paralpr.task3.enums.SemaphoreMode;

import java.util.Iterator;

public class CarGo implements Runnable {
  private final Crossroad road;
  private final RoadSide side;

  public CarGo(Crossroad road, RoadSide side) {
    this.road = road;
    this.side = side;
  }

  @Override
  public void run() {
    System.out.println("[CarGo - " + side + "] Started");
    while (true) {
      Entity c = null;
      RoadSide target = null;
      synchronized (road.getSemaphoreMutex()) {
        if (road.isFinished()) {
          System.out.println("[CarGo - " + side + "] Finished");
          return;
        }
        SemaphoreMode mode = road.getSemaphoreMode();
        synchronized (road.waitingPersons) {
          Iterator<Entity> it = road.waitingCars.get(side).iterator();
          while (it.hasNext()) {
            c = it.next();
            if (mode.allows(c)) {
              target = c.getAt().apply(c.getDir());
              synchronized (road.getRoadLock().get(c.getAt())) {
                synchronized (road.getRoadLock().get(target)) {
                  if (road.onRoad.get(c.getAt()) == null && road.onRoad.get(target) == null) {
                    road.onRoad.put(c.getAt(), c);
                    road.onRoad.put(target, c);
                    it.remove();
                    break;
                  }
                }
              }
            }
            c = null;
          }
        }
      }
      if (c != null) {
        try {
          System.out.println("[CarGo - " + side + "] Enter road - " + c);
          Thread.sleep(Settings.CAR_GO_TIME);
          System.out.println("[CarGo - " + side + "] Exit road - " + c);
        } catch (InterruptedException ignored) {
        }

        synchronized (road.getRoadLock().get(c.getAt())) {
          synchronized (road.getRoadLock().get(target)) {
            road.onRoad.put(c.getAt(), null);
            road.onRoad.put(target, null);
          }
        }
        synchronized (road.completedCars) {
          road.completedCars.get(target).add(c);
        }
      }
      try {
        Thread.sleep(Settings.CAR_GO_LOOP_DELAY);
      } catch (InterruptedException ignored) {
      }
    }
  }
}
