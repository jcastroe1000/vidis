package vidis.modules.vectorClockAlgorithm;

import vidis.data.AUserLink;
import vidis.data.annotation.ComponentInfo;

/**
 * a common default link that does nothing
 * @author Dominik
 *
 */
@ComponentInfo(name = "Default Link")
public class DefaultLink extends AUserLink {

	public void execute() {
	}
}
