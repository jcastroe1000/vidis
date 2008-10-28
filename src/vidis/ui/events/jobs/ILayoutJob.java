package vidis.ui.events.jobs;

import java.util.Collection;

import vidis.data.sim.SimNode;
import vidis.ui.model.graph.layouts.GraphLayout;

public interface ILayoutJob extends IJob {
	public GraphLayout getLayout();
	public Collection<SimNode> getNodes();
}
