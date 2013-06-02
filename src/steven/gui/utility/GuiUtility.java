/**
 * 
 */
package steven.gui.utility;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;

import steven.gui.GuiPaintCallback;

/**
 * @author steven.lam.t.f
 * 
 */
public final class GuiUtility{
	private GuiUtility(){
	}
	public static final GraphicsConfiguration getGraphicsConfiguration(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	}
	public static final VolatileImage createCompatibleVolatileImage(final int width, final int height, final int transparency){
		return createCompatibleVolatileImage(width, height, transparency, getGraphicsConfiguration());
	}
	public static final VolatileImage createCompatibleVolatileImage(final int width, final int height, final int transparency, final GraphicsConfiguration gc){
		return gc.createCompatibleVolatileImage(width, height, transparency);
	}
	public static final VolatileImage validateVolatileImage(final VolatileImage image){
		return validateVolatileImage(image, getGraphicsConfiguration());
	}
	public static final VolatileImage validateVolatileImage(final VolatileImage image, final GraphicsConfiguration gc){
		VolatileImage tmp = image;
		while(tmp.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE){
			tmp = createCompatibleVolatileImage(tmp.getWidth(), tmp.getHeight(), tmp.getTransparency(), gc);
		}
		return tmp;
	}
	public static final VolatileImage render(final Graphics g, final VolatileImage image, final GuiPaintCallback callback){
		VolatileImage tmp = image;
		do{
			tmp = GuiUtility.validateVolatileImage(tmp);
			final Graphics2D g2d = tmp.createGraphics();
			callback.paint(g2d, tmp.getWidth(), tmp.getHeight());
			g2d.dispose();
			g.drawImage(tmp, 0, 0, null);
		}while(tmp.contentsLost());
		return tmp;
	}
}
