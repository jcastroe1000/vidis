package vidis.modules.vartest;

import java.util.LinkedList;
import java.util.List;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserPacket;

/**
 * Node zum Variablen und Annotations testen
 * @author Christoph
 *
 */
@ComponentInfo(name = "VarTestNode")
@ComponentColor(color = ColorType.BLUE)
public class VarTestNode extends AUserNode {

    public void receive(IUserPacket packet) {
	// TODO Auto-generated method stub

    }

    @Display(name = "int exCount")
    public int integerExecuteCount;

    public VarTestNode() {
	integerExecuteCount = 0;

    }
    
    @Override
    public void init() {
    	// TODO Auto-generated method stub
    	
    }

    @Display(name = "test LIST")
    public List<Integer> getSomeList() {
	LinkedList<Integer> list = new LinkedList<Integer>();
	for (int i = 0; i < 10 * Math.random(); i++) {
	    list.add((int) (100 * Math.random()));
	}
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

    }

}
