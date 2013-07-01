/**
 * 
 */
package steven.console;

import java.awt.Font;
import java.awt.Graphics2D;

import steven.gui.GuiWindow;

/**
 * @author Steven
 * 
 */
public class ConsoleWindow extends GuiWindow{
	private static final long INTERNAL_REPAINT_INTERVAL = 30;
	private final RepaintThread repaintThread = new RepaintThread(this, INTERNAL_REPAINT_INTERVAL);
	private final Font asciiFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
	private final Font nonAsciiFont = new Font(Font.MONOSPACED, Font.PLAIN, 16);

	public ConsoleWindow(final int width, final int height){
		super(GuiWindow.TYPE_VOLATILE);
		super.setSize(width, height);
		super.setVisible(true);
		new Thread(this.repaintThread).start();
	}
	@Override
	public void paint(final Graphics2D g, final int canvasWidth, final int canvasHeight){
		g.setFont(this.asciiFont);
		g.drawString("abc123!@#ABC", 0, 50);
		g.setFont(this.nonAsciiFont);
		g.drawString("你好嗎一二三", 0, 100);
	}
}

class RepaintThread implements Runnable{
	private final GuiWindow window;
	private final long repaintInterval;
	private volatile boolean active;

	RepaintThread(final GuiWindow window, final long repaintInterval){
		this.window = window;
		this.repaintInterval = repaintInterval;
	}
	@Override
	public void run(){
		this.active = true;
		while(this.active){
			this.window.repaint();
			this.window.setTitle(String.valueOf(Math.round(this.window.getFps() * 10) / 10.0));
			try{
				Thread.sleep(this.repaintInterval);
			}catch(final InterruptedException e){
			}
		}
	}
	void terminate(){
		this.active = false;
	}
}
