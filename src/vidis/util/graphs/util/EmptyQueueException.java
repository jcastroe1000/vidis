package vidis.util.graphs.util;

/**
 * Thrown by methods on the Queue class to indicate that the Queue is empty
 */
public class EmptyQueueException extends Exception {

  /**
   * Creates a new EmptyQueueException will null as its error message.
   */
  public EmptyQueueException() {
    super();
  }
}