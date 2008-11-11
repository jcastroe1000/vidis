package vidis.modules.vartest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserPacket;

/**
 * Node zum Variablen und Annotations testen
 * @author Christoph
 *
 */
@ComponentInfo(name = "VarTestNode")
@ComponentColor(color=ColorType.BLUE)
public class VarTestNode extends AUserNode {

    public void receive(IUserPacket packet) {
	// TODO Auto-generated method stub

    }
    
    private ColorType color = ColorType.BLACK;
    
    @DisplayColor()
	public ColorType getColor() {
		return color;
	}

    @Display( name = "reset" )
    public void reset() {
    	integerExecuteCount = 0;
    }
    
    @Display(name = "int exCount")
    public int integerExecuteCount;
    @Display(name = "byte exCount")
    public byte byteExecuteCount() {
    	return (byte) integerExecuteCount;
    }
    @Display(name = "float exCount")
    public float floatExecuteCount() {
    	return (float) integerExecuteCount;
    }
    @Display(name = "double exCount")
    public double doubleExecuteCount() {
    	return (double) integerExecuteCount;
    }
    @Display(name = "long exCount")
    public long longExecuteCount() {
    	return (long) integerExecuteCount;
    }
    
    private LinkedList<Integer> list;

    public VarTestNode() {
	integerExecuteCount = 0;

    }
    
    @Override
    public void init() {
    	// TODO Auto-generated method stub
    	list = new LinkedList<Integer>();
    	for (int i = 0; i < 6; i++) {
    	    list.add((int) (100 * Math.random()));
    	}
    }

    @Display(name = "test LIST")
    public List<Integer> getSomeList() {
	
	return list;
    }

    @Display(name = "int exCount()")
    public int getIntegerExecuteCount() {
	return integerExecuteCount;
    }

    @Display(name = "string exCount()")
    public String getStringExecuteCount() {
	return "" + integerExecuteCount;
    }

    @Display(name = "nameOverride")
    public String getName() {
	if (hasVariable("name"))
	    return getVariable("name").getData().toString();
	else
	    return "";
    }

    public void execute() {
    	integerExecuteCount++;
		color = ColorType.values()[integerExecuteCount%(ColorType.values().length)];
		Collections.shuffle(list);
    }

}
