package gyurix.paralpr.task3.enums;

import lombok.Getter;

public enum Direction {
  LEFT(-1),
  STRAIGHT(2),
  RIGHT(1);
  @Getter
  private int effect;

  Direction(int effect) {
    this.effect = effect;
  }
}
