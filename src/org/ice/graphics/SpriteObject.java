package org.ice.graphics;

import java.awt.Rectangle;

public abstract class SpriteObject {

	public int current_x = 0;		// starting x,y of the paddle after level start, or ball drop.
	public int current_y = 0;
	public int height = 0;
	public int width = 0;

	private Rectangle rectangle;
		
	public Rectangle getRectangle()
	{
		return this.rectangle;
	}
}
