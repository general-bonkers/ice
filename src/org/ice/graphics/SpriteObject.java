package org.ice.graphics;

import java.awt.Rectangle;

public abstract class SpriteObject {

	public int current_x = 0;		// starting x,y of the paddle after level start, or ball drop.
	public int current_y = 0;
	public int height = 0;
	public int width = 0;
	public boolean isActive;        // Only active objects will be checked to see if they are colliding with other (active or inactive) objects.
	
	private Rectangle rectangle;
		
	public Rectangle getRectangle()
	{
		return this.rectangle;	
	}
	
	public abstract void handleCollision( SpriteObject collideObject );
	
}
