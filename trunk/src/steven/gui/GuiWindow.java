/**
 * 
 */
package steven.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.KeyListener;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;

/**
 * @author Steven
 * 
 */
public class GuiWindow{
	private final GuiFrame frame;

	public GuiWindow(){
		this(null);
	}
	public GuiWindow(final GuiPaintCallback paintCallback){
		this(paintCallback, null);
	}
	public GuiWindow(final GuiPaintCallback paintCallback, final GuiAfterPaintCallback afterPaintCallback){
		this.frame = new GuiFrame(paintCallback, afterPaintCallback);
	}
	public void setSize(final int width, final int height){
		this.frame.setSize(width, height);
	}
	public void setVisible(final boolean visible){
		this.frame.setVisible(visible);
	}
	public void setPaintCallback(final GuiPaintCallback paintCallback){
		this.frame.setPaintCallback(paintCallback);
	}
	public void setAfterPaintCallback(final GuiAfterPaintCallback afterPaintCallback){
		this.frame.setAfterPaintCallback(afterPaintCallback);
	}
	public void repaint(){
		this.frame.repaint();
	}
	public void setTitle(final String title){
		this.frame.setTitle(title);
	}
	public void setMouseEventListener(final MouseEventListener mouseEventListener){
		this.frame.setMouseEventListener(mouseEventListener);
	}
	public double getAverageRenderTickSpent(){
		return this.frame.getAverageRenderTickSpent();
	}
	public double getAverageRenderPeriod(){
		return this.frame.getAverageRenderPeriod();
	}
	public long getLastRenderTickSpent(){
		return this.frame.getLastRenderTickSpent();
	}
	public void setKeyListener(final KeyListener keyListener){
		this.frame.setKeyListener(keyListener);
	}
}

class GuiFrame extends JFrame{
	private static final long serialVersionUID = -4760232218042117165L;
	private static final double MOVING_AVERAGE_PREVIOUS_FACTOR = 0.7;
	private static final double MOVING_AVERAGE_CURRENT_FACTOR = 0.3;
	private volatile MouseEventListener mouseEventListener;
	private volatile KeyListener keyListener;
	private volatile boolean needRepaint;
	private GuiPaintCallback paintCallback;
	private GuiAfterPaintCallback afterPaintCallback;
	private VolatileImage buffer;
	private long lastTick;
	private double averageRenderTickSpent;
	private double averageRenderPeriod;
	private long lastRenderTickSpent;

	GuiFrame(){
		this(null);
	}
	GuiFrame(final GuiPaintCallback paintCallback){
		this(paintCallback, null);
	}
	GuiFrame(final GuiPaintCallback paintCallback, final GuiAfterPaintCallback afterPaintCallback){
		this.paintCallback = paintCallback;
		this.afterPaintCallback = afterPaintCallback;
		this.buffer = createCompatibleVolatileImage(1, 1, Transparency.OPAQUE);
		this.needRepaint = false;
		this.lastTick = System.currentTimeMillis();
		this.averageRenderTickSpent = 0;
		this.averageRenderPeriod = 0;
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setResizable(false);
	}
	@Override
	public void paint(final Graphics g){
		if(this.paintCallback != null){
			if(super.isVisible()){
				final long startTick = System.currentTimeMillis();
				do{
					validateBuffer();
					final Graphics2D g2d = this.buffer.createGraphics();
					this.paintCallback.paint(g2d, super.getWidth(), super.getHeight());
					g2d.dispose();
					g.drawImage(this.buffer, 0, 0, null);
				}while(this.buffer.contentsLost());
				this.needRepaint = false;
				final long currentTick = System.currentTimeMillis();
				this.lastRenderTickSpent = currentTick - startTick;
				this.averageRenderTickSpent = this.averageRenderTickSpent * MOVING_AVERAGE_PREVIOUS_FACTOR + this.lastRenderTickSpent * MOVING_AVERAGE_CURRENT_FACTOR;
				this.averageRenderPeriod = this.averageRenderPeriod * MOVING_AVERAGE_PREVIOUS_FACTOR + (currentTick - this.lastTick) * MOVING_AVERAGE_CURRENT_FACTOR;
				this.lastTick = currentTick;
				if(this.afterPaintCallback != null){
					this.afterPaintCallback.afterPaint(this.lastRenderTickSpent, this.averageRenderTickSpent, this.averageRenderPeriod);
				}
			}
		}
	}
	@Override
	public void setSize(final int width, final int height){
		super.setSize(width, height);
		this.buffer = createCompatibleVolatileImage(width, height, Transparency.OPAQUE);
	}
	@Override
	public void repaint(){
		if(this.needRepaint == false){
			this.needRepaint = true;
			super.repaint();
		}
	}
	private void validateBuffer(){
		final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		while(this.buffer.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE){
			this.buffer = createCompatibleVolatileImage(super.getWidth(), super.getHeight(), Transparency.OPAQUE, gc);
		}
	}
	private static VolatileImage createCompatibleVolatileImage(final int width, final int height, final int transparency){
		final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		return createCompatibleVolatileImage(width, height, transparency, gc);
	}
	private static VolatileImage createCompatibleVolatileImage(final int width, final int height, final int transparency, final GraphicsConfiguration gc){
		return gc.createCompatibleVolatileImage(width, height, transparency);
	}
	public final void setPaintCallback(final GuiPaintCallback paintCallback){
		this.paintCallback = paintCallback;
	}
	public final void setAfterPaintCallback(final GuiAfterPaintCallback afterPaintCallback){
		this.afterPaintCallback = afterPaintCallback;
	}
	public final void setMouseEventListener(final MouseEventListener mouseEventListener){
		super.removeMouseListener(this.mouseEventListener);
		super.removeMouseMotionListener(this.mouseEventListener);
		super.removeMouseWheelListener(this.mouseEventListener);
		this.mouseEventListener = mouseEventListener;
		super.addMouseListener(mouseEventListener);
		super.addMouseMotionListener(mouseEventListener);
		super.addMouseWheelListener(mouseEventListener);
	}
	public final void setKeyListener(final KeyListener keyListener){
		super.removeKeyListener(this.keyListener);
		this.keyListener = keyListener;
		super.addKeyListener(keyListener);
	}
	public final double getAverageRenderTickSpent(){
		return this.averageRenderTickSpent;
	}
	public final double getAverageRenderPeriod(){
		return this.averageRenderPeriod;
	}
	public final long getLastRenderTickSpent(){
		return this.lastRenderTickSpent;
	}
}
