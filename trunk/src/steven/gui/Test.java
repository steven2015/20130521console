/**
 * 
 */
package steven.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

/**
 * @author Steven
 * 
 */
public class Test{
	public static void main(final String[] args){
		final GuiWindow w = new GuiWindow();
		final GameController ctrl = new GameController(w);
		w.setSize(1024, 768);
		w.setPaintCallback(ctrl);
		w.setAfterPaintCallback(ctrl);
		w.setMouseEventListener(ctrl);
		w.setKeyListener(ctrl);
		w.setVisible(true);
		new Thread(new RenderThread(w, 30)).start();
		new Thread(new LogicThread(ctrl, 100)).start();
	}
}

class GameController extends MouseEventAdapter implements GuiPaintCallback, GuiAfterPaintCallback, KeyListener{
	private final GuiWindow window;

	GameController(final GuiWindow window){
		this.window = window;
	}
	@Override
	public void mouseMoved(final MouseEvent e){
	}
	@Override
	public void paint(final Graphics2D g, final int canvasWidth, final int canvasHeight){
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, canvasWidth, canvasHeight);
		//g.setColor(Color.RED);
		//g.fillOval(this.mouseX - this.ballRadius, this.mouseY - this.ballRadius, this.ballRadius * 2, this.ballRadius * 2);
	}
	@Override
	public void afterPaint(final long tickSpent, final double averageTickSpent, final double averagePaintPeriod){
		final long integralAverageTickSpent = ((long)(averageTickSpent * 1000)) / 1000;
		final long integralFPS = ((long)(1000 / averagePaintPeriod * 1000)) / 1000;
		this.window.setTitle("Paint spent: " + integralAverageTickSpent + " FPS: " + integralFPS);
	}
	public void timeTick(){
	}
	@Override
	public void keyTyped(final KeyEvent e){
		System.out.println("typed: " + e.getKeyChar());
	}
	@Override
	public void keyPressed(final KeyEvent e){
		System.out.println("down: " + e.getKeyCode());
	}
	@Override
	public void keyReleased(final KeyEvent e){
		System.out.println("up: " + e.getKeyCode());
	}
}

class RenderThread implements Runnable{
	private final GuiWindow window;
	private final boolean active;
	private final long targetTickInterval;

	RenderThread(final GuiWindow window, final int targetFps){
		this.window = window;
		this.active = true;
		this.targetTickInterval = 1000 / targetFps;
	}
	@Override
	public void run(){
		while(this.active){
			this.window.repaint();
			final long sleepTickCount = this.targetTickInterval - (long)this.window.getAverageRenderTickSpent();
			if(sleepTickCount > 0){
				try{
					Thread.sleep(sleepTickCount);
				}catch(final InterruptedException e){
				}
			}
		}
	}
}

class LogicThread implements Runnable{
	private final GameController ctrl;
	private final boolean active;
	private final long targetTickInterval;

	LogicThread(final GameController ctrl, final int targetFps){
		this.ctrl = ctrl;
		this.active = true;
		this.targetTickInterval = 1000 / targetFps;
	}
	@Override
	public void run(){
		while(this.active){
			this.ctrl.timeTick();
			final long sleepTickCount = this.targetTickInterval;
			if(sleepTickCount > 0){
				try{
					Thread.sleep(sleepTickCount);
				}catch(final InterruptedException e){
				}
			}
		}
	}
}
