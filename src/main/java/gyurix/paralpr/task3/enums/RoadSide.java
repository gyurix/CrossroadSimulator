package gyurix.paralpr.task3.enums;

public enum RoadSide {
  FRONT, RIGHT, BACK, LEFT;

  public RoadSide apply(Direction dir) {
    return RoadSide.values()[(ordinal() + dir.getEffect() + 4) % 4];
  }
}
