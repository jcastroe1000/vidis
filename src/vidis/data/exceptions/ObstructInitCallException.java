package vidis.data.exceptions;

import java.lang.Exception;

public class ObstructInitCallException extends Exception {

	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = 8624360034050740898L;
	public ObstructInitCallException() {
		super("You must NEVER call init(), it is done by the API; please refer to the cook book.");
	}
}
