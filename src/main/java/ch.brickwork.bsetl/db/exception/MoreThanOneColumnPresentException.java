package ch.brickwork.bsetl.db.exception;

public class MoreThanOneColumnPresentException extends Throwable {

  public MoreThanOneColumnPresentException() {
    super("More than one column present!");
  }

}
