package org.ice.graphics;

import java.awt.Rectangle;

public abstract class SpriteObject {

	public int screen_x = 0;		// starting x,y of the paddle after level start, or ball drop.
	public int screen_y = 0;
	public int height = 0;
	public int width = 0;

	private Rectangle rectangle;
		
	public Rectangle getRectangle()
	{
		return this.rectangle;
	}
}
