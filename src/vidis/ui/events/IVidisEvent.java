/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.events;

public interface IVidisEvent {
	
	public int getID();
	
	public final int KeyPressedEvent = 0;
	public final int KeyReleasedEvent = 1;
	public final int KeyTypedEvent = 2;
	
	public final int MousePressedEvent = 3;
	public final int MouseReleasedEvent = 4;
	public final int MouseClickedEvent = 5;
	public final int MouseMovedEvent = 13;
	
	public final int Init = 6;
	public final int InitScene = 7;
	public final int InitSimulator = 9;
	public final int InitWindow = 10;
	public final int InitGui = 11;
	public final int InitCamera = 12;
	public final int RegisterCanvas = 14;

	
	public final int CameraRegister = 20;
	public final int CameraUnregister = 21;
	public final int ObjectRegister = 22;
	public final int ObjectUnregister = 23;
	
	
	public final int ScrollUp = 1001;
	public final int ScrollLeft = 1002;
	public final int ScrollDown = 1003;
	public final int ScrollRight = 1004;
	public final int SkewUp = 1005;
	public final int SkewDown = 1006;
	public final int RotateLeft = 1007;
	public final int RotateRight = 1008;
	public final int ZoomIn = 1009;
	public final int ZoomOut = 1010;
	
	public final int StartNodeCapturing = 1500;
	public final int NodeCapturingResult = 1501;
	public final int StartPacketCapturing = 1502;
	public final int PacketCapturingResult = 1503;
	
	public final int GuiResizeEvent = 2001;
	public final int GuiMouseEvent = 2002;

	public final int SimulatorPlay = 5001;
	public final int SimulatorLoad = 5002;
	public final int SimulatorReload = 5003;
	public final int SimulatorPause = 5004;
	
	
	public final int FPS 			= 10001;
	public final int ShowGuiContainer = 238472389;
	public final int SelectASimObject = 213121232;
	
	public final int LayoutApplyGraphElectricSpring = 5010;
	public final int LayoutApplyRandom = 5011;
	public final int LayoutApplySpiral = 5012;
	public final int LayoutApplyGrid = 5013;
	public final int LayoutReLayout = 5014;
	
	public final int AppendJob = 6000;
	public final int CleanDoneJobs = 6001;
	
}
