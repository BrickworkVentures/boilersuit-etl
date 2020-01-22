package ch.brickwork.bsetl.db.exception;

public class MoreThanOneRowPresentException extends Throwable {

  public MoreThanOneRowPresentException() {
    super("More than one row is present!");
  }

  public MoreThanOneRowPresentException(String s) {
    super(s);
  }
}
