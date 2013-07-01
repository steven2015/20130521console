/**
 * 
 */
package steven.gui;

import java.awt.Graphics;

import javax.swing.JFrame;

abstract class GuiBaseFrame extends JFrame{
	private static final long serialVersionUID = -4760232218042117165L;
	private volatile boolean needRepaint;
	private GuiPaintCallback paintCallback;
	private GuiAfterPaintCallback afterPaintCallback;
	private long lastTick;

	GuiBaseFrame(){
		this(null);
	}
	GuiBaseFrame(final GuiPaintCallback paintCallback){
		this(paintCallback, null);
	}
	GuiBaseFrame(final GuiPaintCallback paintCallback, final GuiAfterPaintCallback afterPaintCallback){
		this.paintCallback = paintCallback;
		this.afterPaintCallback = afterPaintCallback;
		this.needRepaint = false;
		this.lastTick = System.currentTimeMillis();
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setResizable(false);
		resetDisplayBuffer(1, 1);
	}
	@Override
	public void paint(final Graphics g){
		if(this.paintCallback != null){
			if(super.isVisible()){
				final long startTick = System.currentTimeMillis();
				render(g, this.paintCallback);
				this.needRepaint = false;
				final long currentTick = System.currentTimeMillis();
				final long renderTickSpent = currentTick - startTick;
				final long sleepTickSpent = startTick - this.lastTick;
				this.lastTick = currentTick;
				if(this.afterPaintCallback != null){
					this.afterPaintCallback.afterPaint(sleepTickSpent, renderTickSpent);
				}
			}
		}
	}
	@Override
	public void setSize(final int width, final int height){
		super.setSize(width, height);
		resetDisplayBuffer(width, height);
	}
	@Override
	public void repaint(){
		if(this.needRepaint == false){
			this.needRepaint = true;
			super.repaint();
		}
	}
	abstract void resetDisplayBuffer(int newWidth, int newHeight);
	abstract void render(Graphics g, GuiPaintCallback paintCallback);
	public final void setPaintCallback(final GuiPaintCallback paintCallback){
		this.paintCallback = paintCallback;
	}
	public final void setAfterPaintCallback(final GuiAfterPaintCallback afterPaintCallback){
		this.afterPaintCallback = afterPaintCallback;
	}
}