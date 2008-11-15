package vidis.ui.model.impl.guielements;

public enum Mode {
	MINIMIZED( 2, 0 ),
	NORMAL( 4, 0 ),
	EXPANDED( 4, 11 ),
	EXPANDED2( 4, 26 );
	
	private double height;
	private double topHeight;
	private double mainHeight;
	
	private Mode( double topHeight, double mainHeight ) {
		this.topHeight = topHeight;
		this.mainHeight = mainHeight;
		this.height = topHeight + mainHeight;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public double getTopHeight() {
		return this.topHeight;
	}
	
	public double getMainHeight() {
		return this.mainHeight;
	}
	
	public double getTopY() {
		return this.mainHeight;
	}
	
	public double getMainY() {
		if ( mainHeight == 0 ) {
			return Double.MAX_VALUE;
		}
		else {
			return 0;
		}
	}
}
