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
	
	
	
	public final int FPS 			= 10001;
	
	
	
}
