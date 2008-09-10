package vidis.modules.demo;

import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.ComponentInfo;

@ComponentInfo(name = "ping")
@ComponentColor(color = ColorType.RED)
public class DemoPingPacket extends ADemoPacket {
}
