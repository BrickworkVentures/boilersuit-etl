package ch.brickwork.bsetl.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ViewStructure {
  private final String viewName;
  private final String viewDefinition;
}
