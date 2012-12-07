package org.ice.graphics.driver;

/**
 * Extension of the java.awt.Color class. This derivative we created out of the need to manipulate the individual components of a color object. 
 * Due to the changing nature of the indexed palette and color values, pooling Color objects was not an option. Furthermore the performance hit
 * of creating new color objects when altering the palette or drawing primative objects made no sense. This subclass will allow us to use a single
 * Color object while updating the color values as needed.
 * 
 * @author home
 *
 */
public class Color extends java.awt.Color {

	private int rgba;  
	public Color(int r, int g, int b) {
		super(0, 0, 0);
		setRGB( r, g, b );
	}

	
	public int getRGB()
	{
		return rgba;
	}
	
	public void setRGB( int r, int g, int b )
	{
		rgba = Palette.get32BitColor( r, g, b );		
	}
	
	/**
	 * Sets the RGBA value for the color. The alpha channel is needed for color object. Assume
	 * alpha is 255 since it's not used otherwise.
	 * 
	 * @param rgba
	 */
	public void setRGB( int rgba )
	{
		this.rgba = rgba;
	}
}
