package vidis.util.graphs.util;

/**
 * Thrown by methods on the Queue class to indicate that the Queue is empty
 */
public class EmptyQueueException extends Exception {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1687758464448537298L;

/**
   * Creates a new EmptyQueueException will null as its error message.
   */
  public EmptyQueueException() {
    super();
  }
}