package org.ice.graphics.io;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Draw {

	public static int MAX_IMAGES = 256; // arbitrary
	
	private BufferedImage[] images;
	private int activeImages;
	
	public Draw()
	{
		activeImages = 0;
		images = new BufferedImage[MAX_IMAGES];
	}
	
	private static Color getColor( int color )
	{
		return DOSPalette.pal[ color ];
	}
	
	private java.awt.Graphics graphics; 
	public Draw( BufferStrategy bufferStrategy )
	{
		this();
		graphics = bufferStrategy.getDrawGraphics();
	}
	
	public Graphics graphics()
	{
		return this.graphics;		
	}
	
	//!TODO ADD SPECIFIC EXCEPTIONS
	/**
	 * Loads an image into the game engine, and returns the index of the image.
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public int loadImage( String fileName ) throws Exception
	{
		int index = -1;
		if ( activeImages+1 < MAX_IMAGES )
		{
			index = getNewImage();
			//!TODO check to ensure image is 256 colors. not sure how
			final File file = new File ( fileName );
			images[index] = ImageIO.read( file );			
		}		
		return index;
	}
	
	public void drawImage( int id, int x, int y )
	{
		graphics.drawImage( images[id], x, y, null );
	}
	
	/**
	 * Loads a package of multiple images. Image pack should be defined in image xml file.
	 * 
	 * This should return a sprite object that contains multiple sequences and multiple frames.
	 *
	 * 
	 * @param imgPackFile
	 * @return
	 */
	public Sprite loadSprite( String imgPackFile ) throws Exception
	{
		return null;
	}
	
	/**
	 * Frees an image from the graphics framework. The amount of images available
	 * are limited, so freeing up images as not needed are a good idea. 
	 * @param index
	 */
	public void freeImage( int index )
	{
		if ( activeImages > 0 && images[index] != null )
		{
			images[index].flush();
			images[index] = null;
			index--;
		}
	}
	
	
	/**
	 * Returns the index of a new (null) image slot to create an image in.
	 * @return
	 */
	private int getNewImage()
	{
		if ( activeImages < MAX_IMAGES )
		{
			for ( int i = 0; i < MAX_IMAGES; i++ )
			{
				if ( images[i] == null )
				{
					return i;
				}
			}
		}		
		return -1;
	}
	
	
	//!TODO code
	public void setPalette() 
	{
						
	}
	public void cls ( int color )
	{
		graphics.setColor( getColor( color ) );
		//!HACK get resolution from parent object?
		graphics.fillRect( 0, 0, 640, 480 );
	}
	
	public void line( int x, int y, int x2, int y2, int color )
	{
		graphics.setColor( getColor( color ) );
		
		graphics.drawLine( x, y, x2, y2 );
	}
	
	public void box( int x, int y, int w, int h, int color, boolean bf )
	{
		graphics.setColor( getColor( color ) );
		if ( bf )
		{
			graphics.fillRect( x, y, w, h );
		}
		else
		{
			graphics.drawRect( x, y, w, h );
		}
	}
	
	public void circle( int x, int y, int diameter, int color, boolean cf )
	{
		graphics.setColor( getColor( color ) );
		if ( cf )
		{
			graphics.fillOval( x, y, diameter, diameter );
		}
		else
		{
			graphics.drawOval( x, y, diameter, diameter );
		}
	}
	
	public void pset( int x, int y, int color )
	{
		graphics.setColor( getColor( color ) );
		graphics.drawLine( x, y, x, y );
	}
	
	public void dispose()
	{
		graphics.dispose();
	}
} 
