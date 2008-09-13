package vidis.util;

import java.awt.Color;
import java.util.Random;

/**
 * a static function that may be used to generate nice pairs of color
 * 
 * @author Dominik
 *
 */
public class ColorGenerator {
	/**
	 * generates a nice color in the neighborhood of the provided color by
	 * transformation over the HSB color circle; the rotationFactor provided is
	 * the amount of degrees that the color is adjusted by and the second
	 * parameter defines the relative brightness to the original color
	 * 
	 * @param color original color
	 * @param rotationFactor [-360..360]
	 * @param relativeBrightness [-100..100]
	 * @return a new color
	 */
	public static Color nearByColor(Color color, int rotationFactor, int relativeSaturation, int relativeBrightness) {
		float[] hsb = new float[3];
		hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		// adjust hue
		float new_hsb_h = hsb[0] * 360f + rotationFactor;
		while (new_hsb_h < 0)
			new_hsb_h += 360f;
		if (new_hsb_h > 360f)
			new_hsb_h %= 360f;
		new_hsb_h /= 360f;
		hsb[0] = new_hsb_h;
		// adjust saturation
		float new_hsb_s = hsb[1] * 100f + relativeSaturation;
		new_hsb_s /= 100f;
		hsb[1] = new_hsb_s;
		// adjust brightness
		float new_hsb_b = hsb[2] * 100f + relativeBrightness;
		new_hsb_b /= 100f;
		hsb[2] = new_hsb_b;
		// done adjusting
		normalize(hsb);
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
	}

	private static void normalize(float[] rgbORhsb) {
		for (int i = 0; i < rgbORhsb.length; i++)
			rgbORhsb[i] = Math.max(0f, Math.min(1f, rgbORhsb[i]));
	}
	
	public static Color generateRandomColor() {
		Random r = new Random(System.currentTimeMillis());
		return new Color( r.nextFloat(), r.nextFloat(), r.nextFloat() );
	}
}
