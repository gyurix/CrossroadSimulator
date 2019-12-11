package gyurix.paralpr.task3.entities;

import gyurix.paralpr.task3.enums.Direction;
import gyurix.paralpr.task3.enums.RoadSide;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Entity {
  private final RoadSide at;
  private final Direction dir;
  private final int id;
}
