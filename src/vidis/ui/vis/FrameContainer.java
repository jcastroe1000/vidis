package vidis.ui.vis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

public class FrameContainer extends Frame {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( FrameContainer.class );
	
	public FrameContainer(String title){
		super(title);
		
		logger.debug( "Constructor()" );
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		
		setLayout( new FlowLayout() );
		
		add( new Label( "loading..." ) );
		
		setVisible( true );	
		this.addWindowListener( new WindowListener() {
			public void windowActivated(WindowEvent e) {
				logger.info("windowActivated");
			}
			public void windowClosed(WindowEvent e) {
				logger.info("windowClosed");
			}
			public void windowClosing(WindowEvent e) {
				logger.info("windowClosing");
				System.exit(0);
			}
			public void windowDeactivated(WindowEvent e) {
				logger.info("windowDeactivated");
			}
			public void windowDeiconified(WindowEvent e) {
				logger.info("windowDeiconified");
			}
			public void windowIconified(WindowEvent e) {
				logger.info("windowIconified");
			}
			public void windowOpened(WindowEvent e) {
				logger.info("windowOpened");
			}
			
		});
	}
	
	public void addGLCanvas( GLCanvas glCanvas ) {
		removeAll();
		setLayout( new BorderLayout() );
		add( glCanvas, BorderLayout.CENTER );
	}
	
	public void fullscreen(){
		logger.debug( "fullscreen()" );
		 GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		 gd.getAvailableAcceleratedMemory();
		
	}

}
