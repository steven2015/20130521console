/**
 * 
 */
package steven.gui;

import java.awt.Graphics2D;

/**
 * @author Steven
 * 
 */
public abstract class GuiWindow implements GuiPaintCallback, GuiAfterPaintCallback{
	public static final int TYPE_VOLATILE = 1;
	public static final int TYPE_BUFFERED = 2;
	private static final double INTERNAL_OLD_COEFFICIENT = 0.8;
	private static final double INTERNAL_NEW_COEFFICIENT = 1000 * (1 - INTERNAL_OLD_COEFFICIENT);
	private final GuiBaseFrame frame;
	private double fps;

	protected GuiWindow(final int bufferType){
		switch(bufferType){
			case TYPE_VOLATILE:
				this.frame = new GuiVolatileImageFrame(this, this);
				break;
			case TYPE_BUFFERED:
			default:
				this.frame = new GuiBufferedImageFrame(this, this);
				break;
		}
	}
	@Override
	public void afterPaint(final long sleepTickSpent, final long renderTickSpent){
		this.fps = INTERNAL_OLD_COEFFICIENT * this.fps + INTERNAL_NEW_COEFFICIENT / (sleepTickSpent + renderTickSpent);
	}
	@Override
	public abstract void paint(Graphics2D g, int canvasWidth, int canvasHeight);
	public void setSize(final int width, final int height){
		this.frame.setSize(width, height);
	}
	public void setTitle(final String title){
		this.frame.setTitle(title);
	}
	public void repaint(){
		this.frame.repaint();
	}
	public void setVisible(final boolean visible){
		this.frame.setVisible(visible);
	}
	public final double getFps(){
		return this.fps;
	}
}
