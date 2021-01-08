package ch.brickwork.bsetl.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class PropertyStructure {
  private final String columnName;
  private final String dataType;
  private final Integer length;
}
