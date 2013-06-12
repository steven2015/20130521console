/**
 * 
 */
package steven.gui.exception;

/**
 * @author Steven
 * 
 */
public class UnstableVolatileImageException extends Exception{
	private static final long serialVersionUID = 9054445731758280750L;

	public UnstableVolatileImageException(){
		super("Cannot get a stable VolatileImage.");
	}
}
