package vidis.util;

import java.util.Formatter;

public class Rounding {
	public static String round(double num, int place) {
		return new Formatter().format("%."+place+"f", num).toString();
	}
	public static String round(float num, int place) {
		return new Formatter().format("%."+place+"f", num).toString();
	}
}
