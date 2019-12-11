package gyurix.paralpr.task3.entities;

import gyurix.paralpr.task3.enums.Direction;
import gyurix.paralpr.task3.enums.RoadSide;

import java.util.concurrent.atomic.AtomicInteger;

public class Car extends Entity {
  public static final AtomicInteger nextId = new AtomicInteger();

  public Car(RoadSide at, Direction dir) {
    super(at, dir, nextId.incrementAndGet());
  }

  @Override
  public String toString() {
    return "Car #" + getId() + " - " + getAt() + " --> " + getDir() + " === " + getAt().apply(getDir());
  }
}
