/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.annotation;

import java.awt.Color;

/**
 * these color types are used by the ComponentColor annotation
 * 
 * @author Dominik
 * 
 * @see ComponentColor
 */
public enum ColorType implements Comparable<ColorType> {
	BLACK(Color.black),
	WHITE(Color.white),
	
	GREY(Color.gray),
	GREY_DARK(Color.darkGray),
	GREY_LIGHT(Color.lightGray),
	
	BLUE(Color.blue),
	BLUE_LIGHT(Color.blue.brighter()),
	BLUE_DARK(Color.blue.darker()),
	
	CYAN(Color.cyan),
	CYAN_LIGHT(Color.cyan.brighter()),
	CYAN_DARK(Color.cyan.darker()),
	
	GREEN(Color.green),
	GREEN_LIGHT(Color.green.brighter()),
	GREEN_DARK(Color.green.darker()),
    
    MAGENTA(Color.magenta),
    MAGENTA_DARK(Color.magenta.darker()),
    MAGENTA_LIGHT(Color.magenta.brighter()),
    
    ORANGE(Color.orange),
    ORANGE_DARK(Color.orange.darker()),
    ORANGE_LIGHT(Color.orange.brighter()),
    
    PINK(Color.pink),
    PINK_DARK(Color.pink.darker()),
    PINK_LIGHT(Color.pink.brighter()),
    
    RED(Color.red),
    RED_DARK(Color.red.darker()),
    RED_LIGHT(Color.red.brighter()),
    
    YELLOW(Color.yellow),
    YELLOW_DARK(Color.yellow.darker()),
    YELLOW_LIGHT(Color.yellow.brighter()),
    
    TRANSPARENT(new Color(0,0,0,0)),
    /**
     * initially:
     * R G B A
     * 0 0 0 0
     * 
     * can be edited by using setColor(r,g,b) or setColor(r,g,b,a)
     * 
     * BUT NOTE THIS: only enum constants are allowed for enumerated 
     * 	annotations, therefore you can only set one of the pre-set
     * 	colors in combination with the annotation!
     * @see ColorType.setColor
     */
    CUSTOM(0,0,0,0);

    private Color color;

    private ColorType(int r, int g, int b, int a) {
    	color = new Color(r, g, b, a);
    }
    private ColorType(int r, int g, int b) {
    	color = new Color(r, g, b);
    }
    private ColorType(Color c1) {
    	color = c1;
    }
    public ColorType setColor(int r, int g, int b, int a) {
		this.color = new Color(r, g, b, a);
		return this;
	}
    public ColorType setColor(int r, int g, int b) {
    	color = new Color(r, g, b);
    	return this;
    }
    public ColorType setColor(Color color) {
		this.color = color;
		return this;
	}
    
    public static ColorType valueOf(Color color) {
    	return ColorType.CUSTOM.setColor(color);
    }

    public Color color() {
    	return color;
    }
}
