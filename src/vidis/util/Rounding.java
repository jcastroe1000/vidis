package vidis.util;

import java.util.Formatter;

public class Rounding {
	private static final Formatter fm = new Formatter();
	public static String round(double num, int place) {
		String s = fm.format("%."+place+"d", num).toString();
		fm.flush();
		return s;
	}
	public static String round(float num, int place) {
		String s = fm.format("%."+place+"f", num).toString();
		fm.flush();
		return s;
	}
}
