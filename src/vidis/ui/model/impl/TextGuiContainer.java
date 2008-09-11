package vidis.ui.model.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;
import javax.swing.JOptionPane;

import vidis.ui.events.GuiMouseEvent;
import vidis.ui.model.structure.AGuiContainer;

import com.sun.opengl.util.j2d.TextRenderer;


public class TextGuiContainer extends AGuiContainer {

	private String text;
	private Color color = Color.black;
	private Color textColor = Color.red;
	
	//private static TextRenderer textRenderer = new TextRenderer( new Font("Times New Roman", Font.PLAIN, 130 ), true, true );
	
	private void renderStrokeString(GL gl, int font, String string, double contwith) {
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glColor3d(1, 0, 0);
        gl.glLineWidth(2.0f);

		// Center Our Text On The Screen
        float strwidth = glut.glutStrokeLength(font, string);
       
        double scale = contwith / strwidth;
        gl.glTranslated(0, contwith / 2d, 0); //FIXME y offset
        gl.glScaled(scale, -scale, 1);
        // Render The Text
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            glut.glutStrokeCharacter(font, c);
        }
    }
	
	@Override
	public void renderContainer(GL gl) {
		requireTextRenderer();
//		renderStrokeString(gl, GLUT.STROKE_ROMAN, text, getWidth());
		Rectangle2D bounds = textRenderer.getBounds(text);
		gl.glPushMatrix();
		float scale = (float) getWidth() / (float) bounds.getWidth();
		gl.glTranslated(0, 2, 0);
		gl.glScaled( scale, -scale, 1 );
		textRenderer.begin3DRendering();
		textRenderer.setUseVertexArrays(false);
		textRenderer.draw3D( text, 0f, 0f, 0f, 0.001f );
		textRenderer.end3DRendering();
		gl.glPopMatrix();
	}
	
	public void setText(String txt){
		this.text = txt;
	}
	
	@Override
	protected void onClick( GuiMouseEvent e ) {
		JOptionPane.showMessageDialog(null, "you clicked on an TextGuiContainer!");
	}

}
