package gyurix.paralpr.task3.enums;

import gyurix.paralpr.task3.entities.Car;
import gyurix.paralpr.task3.entities.Entity;
import gyurix.paralpr.task3.entities.Person;

public enum SemaphoreMode {
  FRONT_BACK {
    @Override
    public boolean allows(Car car) {
      return (car.getDir() == Direction.STRAIGHT || car.getDir() == Direction.LEFT) && (car.getAt() == RoadSide.FRONT || car.getAt() == RoadSide.BACK);
    }

    @Override
    public boolean allows(Person person) {
      return ((person.getAt() == RoadSide.LEFT) || (person.getAt() == RoadSide.RIGHT) && person.getDir() == Direction.RIGHT) ||
              ((person.getAt() == RoadSide.FRONT) || (person.getAt() == RoadSide.BACK) && person.getDir() == Direction.LEFT);
    }
  }, LEFT_RIGHT {
    @Override
    public boolean allows(Car car) {
      return (car.getDir() == Direction.STRAIGHT || car.getDir() == Direction.LEFT) && (car.getAt() == RoadSide.LEFT || car.getAt() == RoadSide.RIGHT);
    }

    @Override
    public boolean allows(Person person) {
      return ((person.getAt() == RoadSide.LEFT) || (person.getAt() == RoadSide.RIGHT) && person.getDir() == Direction.LEFT) ||
              ((person.getAt() == RoadSide.FRONT) || (person.getAt() == RoadSide.BACK) && person.getDir() == Direction.RIGHT);
    }
  }, RIGHT_TURN {
    @Override
    public boolean allows(Person person) {
      return false;
    }

    @Override
    public boolean allows(Car car) {
      return car.getDir() == Direction.RIGHT;
    }
  };

  public boolean allows(Entity entity) {
    return entity instanceof Person ? allows((Person) entity) : allows((Car) entity);
  }

  public abstract boolean allows(Person person);

  public abstract boolean allows(Car car);
}
