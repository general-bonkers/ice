package org.ice.graphics.i3d.vector;

import org.ice.graphics.Graphics;

/**
 * Wireframe implementation of The VectorObject class. Uses a single color to define the object.
 * 
 * @author home
 *
 */
public class WireVectorObject extends VectorObject {

	public int color;
	
	public WireVectorObject()
	{
		super();
		color = 15; // white
	}
	
	/**
	 * Sets the color for this object based on the ICE 256 color system.
	 * 
	 * @param color
	 */
	public void setColor( int color )
	{
		if ( color <= 255 )
		{
			this.color = color;
		}
	}
	
	public void render( Graphics graphics )
	{
		Point start, end;
		Line line;
		for ( int i = 0; i < lines.size(); i++ )
		{
			line = lines.get( i );
			start = line.start;
			end = line.end;
			graphics.line( (int)start.x2d, (int)start.y2d, (int)end.x2d, (int)end.y2d, this.color );			
		}
	}
	
}
