package vidis.modules.demo;

import java.util.Set;

import vidis.data.AUserPacket;

public abstract class ADemoPacket extends AUserPacket {
	public String toString() {
		if(hasVariable("name")) {
			return getVariable("name").getData().toString();
		} else {
			Set<String> ids = getVariableIdentifiers();
			if(ids != null)
				return ids.toString();
			return "<?>";
		}
	}
}
