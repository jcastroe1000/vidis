/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
