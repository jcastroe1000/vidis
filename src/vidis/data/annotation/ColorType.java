package vidis.data.annotation;

import java.awt.Color;

import javax.vecmath.Color3f;

import vidis.util.ColorGenerator;

/**
 * these color types are used by the ComponentColor annotation
 * 
 * @author Dominik
 * 
 * @see ComponentColor
 */
public enum ColorType implements Comparable<ColorType> {
    // color1 color2
    RED(Color.red, ColorGenerator.nearByColor(Color.red, ColorType.shift,
					      ColorType.shift, ColorType.shift)),
    GREEN(Color.green, ColorGenerator.nearByColor(Color.green, ColorType.shift,
						  ColorType.shift,
						  ColorType.shift)),
    BLUE(Color.blue, ColorGenerator.nearByColor(Color.blue, ColorType.shift,
						ColorType.shift,
						ColorType.shift)),
    YELLOW(Color.yellow, ColorGenerator.nearByColor(Color.yellow,
						    ColorType.shift,
						    ColorType.shift,
						    ColorType.shift)),
    WHITE(Color.white, ColorGenerator.nearByColor(Color.white, ColorType.shift,
						  ColorType.shift,
						  ColorType.shift)),
    LIGHT_GREY(Color.lightGray, ColorGenerator.nearByColor(Color.lightGray,
							   ColorType.shift,
							   ColorType.shift,
							   ColorType.shift)),
    GREY(Color.gray, ColorGenerator.nearByColor(Color.gray, ColorType.shift,
						ColorType.shift,
						ColorType.shift)),
    DARK_GREY(Color.darkGray, ColorGenerator.nearByColor(Color.darkGray,
							 ColorType.shift,
							 ColorType.shift,
							 ColorType.shift)),
    BLACK(Color.black, ColorGenerator.nearByColor(Color.black, ColorType.shift,
						  ColorType.shift,
						  ColorType.shift));

    private Color3f color1;
    private Color3f color2;
    private static final int shift = 20;

    private ColorType(Color c1, Color c2) {
	color1 = new Color3f(c1);
	color2 = new Color3f(c2);
    }

    @Deprecated
    public Color3f getColor3f() {
	return color1;
    }

    public Color3f getColor1() {
	return color1;
    }

    public Color3f getColor2() {
	return color2;
    }
}
