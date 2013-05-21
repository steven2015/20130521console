/**
 * 
 */
package steven.gui;

import java.awt.event.KeyEvent;

/**
 * @author Steven
 * 
 */
public interface KeyEventListener{
	public void keyDown(KeyEvent event, int keyCode);
	public void keyUp(KeyEvent event, int keyCode);
	public void keyTyped(KeyEvent event, char keyChar);
}
