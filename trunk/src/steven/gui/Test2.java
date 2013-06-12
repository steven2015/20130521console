/**
 * 
 */
package steven.gui;

import java.awt.GraphicsEnvironment;

/**
 * @author Steven
 * 
 */
public class Test2{
	public static final void main(final String[] args){
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getAvailableAcceleratedMemory());
	}
}
