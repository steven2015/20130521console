/**
 * 
 */
package steven.gui;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.VolatileImage;

import steven.gui.exception.UnstableVolatileImageException;
import steven.gui.utility.GuiUtility;

class GuiVolatileImageFrame extends GuiBaseFrame{
	private static final long serialVersionUID = -5471626178377510843L;
	private VolatileImage vi;

	GuiVolatileImageFrame(){
		super();
	}
	GuiVolatileImageFrame(final GuiPaintCallback paintCallback){
		super(paintCallback);
	}
	GuiVolatileImageFrame(final GuiPaintCallback paintCallback, final GuiAfterPaintCallback afterPaintCallback){
		super(paintCallback, afterPaintCallback);
	}
	@Override
	void resetDisplayBuffer(final int newWidth, final int newHeight){
		this.vi = GuiUtility.createCompatibleVolatileImage(newWidth, newHeight, Transparency.OPAQUE);
	}
	@Override
	void render(final Graphics g, final GuiPaintCallback paintCallback){
		try{
			this.vi = GuiUtility.render(g, this.vi, paintCallback);
		}catch(final UnstableVolatileImageException e){
			e.printStackTrace();
		}
	}
}