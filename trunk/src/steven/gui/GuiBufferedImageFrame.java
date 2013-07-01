/**
 * 
 */
package steven.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

class GuiBufferedImageFrame extends GuiBaseFrame{
	private static final long serialVersionUID = 7351823957824620943L;
	private BufferedImage bi;
	private int width;
	private int height;

	GuiBufferedImageFrame(){
		super();
	}
	GuiBufferedImageFrame(final GuiPaintCallback paintCallback){
		super(paintCallback);
	}
	GuiBufferedImageFrame(final GuiPaintCallback paintCallback, final GuiAfterPaintCallback afterPaintCallback){
		super(paintCallback, afterPaintCallback);
	}
	@Override
	void resetDisplayBuffer(final int newWidth, final int newHeight){
		this.bi = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		this.width = newWidth;
		this.height = newHeight;
	}
	@Override
	void render(final Graphics g, final GuiPaintCallback paintCallback){
		final Graphics2D g2d = this.bi.createGraphics();
		try{
			paintCallback.paint(g2d, this.width, this.height);
		}finally{
			g2d.dispose();
		}
		g.drawImage(this.bi, 0, 0, this.width, this.height, null);
	}
}
