/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.vis;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

public class VecUtil {
	private static Logger logger = Logger.getLogger(VecUtil.class);

	public static Vector3d cross( Tuple3d A, Tuple3d B ){
		Vector3d ret = new Vector3d();
		ret.x = A.y * B.z - A.z * B.y;
		ret.y = A.x * B.z - A.z * B.x;
		ret.z = A.x * B.y - A.y * B.x;
		return ret;
	}
}
