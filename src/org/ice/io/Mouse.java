package org.ice.io;

import java.awt.GraphicsDevice;

/**
 * User-friendly mouse class. Uses MouseDriver and only exposes methods needed by engine.
 * 
 * @author home
 *
 */
public class Mouse {

	private MouseDriver mouseDriver;
	
	public Mouse( GraphicsDevice graphicsDevice )
	{
		this.mouseDriver = new MouseDriver();
		graphicsDevice.getFullScreenWindow().addMouseListener( mouseDriver );
		graphicsDevice.getFullScreenWindow().addMouseMotionListener( mouseDriver );
	}
	
	public int getMouseButton()
	{
		return mouseDriver.getMouseButton();
	}
	
	public int getMouseX()
	{
		return mouseDriver.getMouseX();
	}
	
	public int getMouseY()
	{
		return mouseDriver.getMouseY();
	}
}
