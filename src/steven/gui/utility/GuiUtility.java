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
import steven.gui.exception.UnstableVolatileImageException;

/**
 * @author steven.lam.t.f
 * 
 */
public final class GuiUtility{
	private static final int INTERNAL_RETRY_TIMES = 10;

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
	public static final VolatileImage validateVolatileImage(final VolatileImage image) throws UnstableVolatileImageException{
		return validateVolatileImage(image, getGraphicsConfiguration());
	}
	public static final VolatileImage validateVolatileImage(final VolatileImage image, final GraphicsConfiguration gc) throws UnstableVolatileImageException{
		return validateVolatileImage(image, gc, INTERNAL_RETRY_TIMES);
	}
	public static final VolatileImage validateVolatileImage(final VolatileImage image, final GraphicsConfiguration gc, final int retryTimes) throws UnstableVolatileImageException{
		VolatileImage tmp = image;
		int count = retryTimes;
		while(tmp.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE){
			if(count-- <= 0){
				throw new UnstableVolatileImageException();
			}
			tmp = createCompatibleVolatileImage(tmp.getWidth(), tmp.getHeight(), tmp.getTransparency(), gc);
		}
		return tmp;
	}
	public static final VolatileImage render(final Graphics g, final VolatileImage image, final GuiPaintCallback callback) throws UnstableVolatileImageException{
		return render(g, image, callback, INTERNAL_RETRY_TIMES);
	}
	public static final VolatileImage render(final Graphics g, final VolatileImage image, final GuiPaintCallback callback, final int retryTimes) throws UnstableVolatileImageException{
		VolatileImage tmp = image;
		int count = retryTimes;
		do{
			if(count-- <= 0){
				throw new UnstableVolatileImageException();
			}
			tmp = GuiUtility.validateVolatileImage(tmp);
			final Graphics2D g2d = tmp.createGraphics();
			try{
				callback.paint(g2d, tmp.getWidth(), tmp.getHeight());
			}finally{
				g2d.dispose();
			}
			g.drawImage(tmp, 0, 0, null);
		}while(tmp.contentsLost());
		return tmp;
	}
}
