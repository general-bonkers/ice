package org.ice.graphics.io;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Draw {

	private java.awt.Graphics graphics; 
	public Draw( BufferStrategy bufferStrategy )
	{
		graphics = bufferStrategy.getDrawGraphics();
	}
	
	public Graphics graphics()
	{
		return this.graphics;
	}
	
	//!TODO code
	public void setPalette() 
	{
						
	}
	public void cls ( int color )
	{
		graphics.setColor( Color.blue );
		//!HACK get resolution from parent object?
		graphics.fillRect( 0, 0, 640, 480 );
	}
	
	public void line( int x, int y, int x2, int y2, int color )
	{
		graphics.setColor( Color.MAGENTA );
		graphics.drawLine( x, y, x2, y2 );
	}
	
	public void box( int x, int y, int w, int h, int color, boolean bf )
	{
		graphics.setColor( new Color(255, 255, 0 ) );
		if ( bf )
		{
			graphics.fillRect( x, y, w, h );
		}
		else
		{
			graphics.drawRect( x, y, w, h );
		}
	}
	
	public void pset( int x, int y, int color )
	{
		graphics.setColor( new Color( 255, 255, 0 ) );
		graphics.drawLine( x, y, x, y );
	}
	
	public void dispose()
	{
		graphics.dispose();
	}
} 