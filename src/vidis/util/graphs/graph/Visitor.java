package vidis.util.graphs.graph;

import java.io.*;

/**
 * A interface for a visitor in the Visitor Pattern.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public interface Visitor extends Serializable {

  public boolean visit( Vertex vertexToVisit );
}

