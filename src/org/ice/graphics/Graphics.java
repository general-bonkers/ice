package org.ice.graphics;

import java.awt.GraphicsDevice;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.ice.graphics.driver.Display;
import org.ice.graphics.driver.Palette;

/**
 * Wrapping graphics handler for Ice Pixel Engine
 * @author home
 *
 */

public class Graphics {

	public static int MAX_IMAGE_BUFFERS = 2048;  // Mostly arbitrary number.
	
	private Display display;
	private Vector<ImageBuffer> imageBuffers;
	
	public GraphicsDevice getGraphicsDevice()
	{
		return display.graphicsDevice;
	}
	
	public Graphics()
	{		
		display = new Display();
		imageBuffers = new Vector<ImageBuffer>();
	}
	
	public void clearScreen( int color )
	{
		display.clearScreenP( color );
	}
	
	public void render()
	{
		display.renderFrameBuffer();
	}
	
	public int getScreenWidth()
	{
		return display.width;
	}
	
	public int getScreenHeight()
	{
		return display.height;
	}
	
	public void init( int displayMode )
	{
		display.init( displayMode, true );
	}
	
	public void line( int x, int y, int x2, int y2, int color )
	{
		display.lineP( x, y, x2, y2, color );
	}
	
	public void box( int x, int y, int w, int h, int color, boolean bf )
	{
		display.boxP( x, y, w, h, color, bf );
	}
	
	public void circle( int x, int y, int diameter, int color, boolean cf )
	{
		display.circleP( x, y, diameter, color, cf );
	}
	
	public void poke( int x, int y, int color )
	{
		display.pokeP( x, y, color );
	}
	
	public int peek( int x, int y )
	{
		return display.peekP( x, y );
	}
	
	public void exit()
	{
		display.returnToDesktop();
	}
	
	/**
	 * Creates a new Graphics image buffer for writing and storing of image data.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public ImageBuffer createImageBuffer( int width, int height )
	{
		ImageBuffer buff = null;
		if ( imageBuffers.size() < MAX_IMAGE_BUFFERS )
		{
			buff = new ImageBuffer( width, height );
			imageBuffers.add( buff );					
		}
		return buff;
	}
	
	/**
	 * This method is used to push a full screen image to the display as fastly as possible. Note that the pixel
	 * engine is already pretty fast as-is. This method is only provided for convenience. It may make a nice function
	 * for showing full-screen animations, full screen background (static), or a hud display. Transparency is ignored. 
	 * 
	 * 
	 * The image must match the dimensions of the screen or it will not be drawn. This method relies on the System.arraycopy 
	 * method to efficiently move the image.
	 * 
	 * This may also be useful for a static full screen back
	 * 
	 * @param image image to render.
	 */
	public void fastDrawScreenImage( ImageBuffer image )
	{
		if ( image != null & image.width == display.width && image.height == display.height )
		{
			System.arraycopy( image, 0, display.getVideoBuffer(), 0, display.width * display.height );
		}
	}
	
	/**
	 * Draws an image to the screen. If transparency is enabled, the transparent color will be obtained from the 
	 * current palette. Clipping is enabled as well to allow for partial (or full) offscreen rendering.
	 * 
	 * @param image ImageBuffer object
	 * @param x x location on screen buffer to write to
	 * @param y y location on screen buffer to write to
	 */
	public void drawImage( ImageBuffer image, int x, int y, boolean transparent )
	{
		int startSeg = 0;
		int destSeg = x + ( getScreenWidth() * y );
		int offset = image.width + ( image.width * image.height );
		
		Palette palette = display.getPalette();
		int src[] = image.getBuffer();
		int dest[] = display.getVideoBuffer();
		
		int plotx, ploty, pixel, transColor;
		
		transColor = display.getPalette().transparentColor;
		
		for ( int dy = 0; dy < image.height; dy++ )
		{
			for ( int dx = 0; dx < image.width; dx++ )
			{
				plotx = x + dx;
				ploty = y + dy;
				
				if ( plotx >=0 && ploty >=0 && plotx <= display.width && ploty <= display.height )
				{
					pixel = src[ dx + ( image.width * dy ) ];
					if ( !transparent || ( transColor == -1 ) || ( transparent && pixel != transColor ) )
					{
						display.pokeP( plotx, ploty, pixel );
					}
				}
			}
		}

	}				
	
	/**
	 * Loads an 8-bit image into memory and returns an <code>ImageBuffer</code> object containing
	 * the image data. If loadPalette is set to true, the current palette will be replaced with the
	 * one stored in the image. If the image contains more than 256 colors null is returned. In addition
	 * if the Image buffer list is full <see>Graphics.MAX_IMAGE_BUFFERS</see> then null will be returned
	 * as well.
	 * 
	 *  Note this has not been tested with animated images.
	 *  
	 * @param fileName path and name of file to load
	 * @param loadPalette set to true if you wish to change the current palette to the one stored in the image
	 * @return an <code>ImageBuffer</code> object containing image.
	 *  
	 * @throws Exception
	 */
	public ImageBuffer loadImage( String fileName, boolean loadPalette ) throws Exception
	{
		ImageBuffer image = null;
		if ( imageBuffers.size() < MAX_IMAGE_BUFFERS )
		{			
			BufferedImage tmpImage = ImageIO.read( new File( fileName ) );

			// Ensure we're dealing with an 8-bit image.
			if ( tmpImage.getType() == BufferedImage.TYPE_BYTE_INDEXED  ) 
			{
				image = createImageBuffer( tmpImage.getWidth(), tmpImage.getHeight() );
				
				if ( loadPalette )
				{
					display.getPalette().loadPalette( (IndexColorModel)tmpImage.getColorModel() );
				}
				
				byte[] tmpImageBuff = ((DataBufferByte)tmpImage.getRaster().getDataBuffer()).getData();
				int[] imageBuff = image.getBuffer();				
				for ( int i = 0; i < tmpImageBuff.length; i++ )
				{
					// convert from unsigned byte to int.
					imageBuff[i] = tmpImageBuff[i] & 0xff;
					
				}
				tmpImage = null;
			}
			else
			{
				// throw exception?
			}
		}		
		return image;
	}
	
	public static void main ( String args[] ) throws Exception
	{
		Graphics g = new Graphics();
		g.init(0);
		try {
		ImageBuffer i = g.loadImage( "./gfx/wombat_logo2.bmp", true );
		g.drawImage(i, 100, 100, false);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		g.render();
		Thread.sleep(2000);
		g.exit();
	}
}
