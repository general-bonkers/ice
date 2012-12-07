package org.ice.graphics.driver;

import java.awt.Color;
import java.awt.image.IndexColorModel;

import org.ice.graphics.DOSPalette;

/**
 * 8 bit palette class for ICE
 * @author home
 *
 */
public class Palette {
 
	/*
	 * The ICE engine assumes 8 bit emulation in a 16 or 32bit graphical environment. The palette will store both the palette component values (r,g,b)
	 * as well as the 32bit int RGB representation of these values. The latter is used to write to the screen, while the former is used to communicate
	 * palette shifts and other mechanisms to isolate the end user from 32bit colors.
	 * 
	 * Why are colors encoded in 32bit ARGB format? Well I have not found a good way to create a color object that doesn't include the alpha channel,
	 * even if it's an "opaque" object. The R,G,B constructor automatically adds the alpha channel in. Apparently the single RGB int constructor does
	 * the same. The alpha channel is never used, but in order to match with the way that java.awt.Color represents a packed color value, we must follow
	 * suit. This is largely because the primatives in org.ice.graphics.driver.Display rely on color object, and if we want to read pixels back we
	 * must make sure the values for a single color match up. For example:
	 * 
	 * RGB green (color 2 in dos palette) is 43520
	 * ARGB green (as defined by Color object) is -16733696
	 * 
	 * 
	 * 
	 * Whenever any of the color component values are updated, the 32bit color representation must be updated as well (with reIndexColor)
	 *  
	 */
	
	public int pal[];            // palette (32bit int representation)
	private int palR[];          // red component
	private int palG[];          // green component
	private int palB[];          // blue component
	public int transparentColor; // transparent color (if used)
	
	private static final int ALPHA = 255; // no alpha!
	
	public static void main ( String args[] )
	{
		Palette pal = new Palette( DOSPalette.pal );
	}
	
	public Palette( int[] pal )
	{
		this.pal = new int[Display.COLORS];
		palR = new int[Display.COLORS];
		palG = new int[Display.COLORS];
		palB = new int[Display.COLORS];
		loadPalette( pal );
		transparentColor = -1;
	}

	public void loadPalette( IndexColorModel indexColorModel )
	{
		if ( indexColorModel != null ) 
		{
			byte r[] = new byte[256];
			byte g[] = new byte[256];
			byte b[] = new byte[256];
			indexColorModel.getReds( r );
			indexColorModel.getGreens( g );
			indexColorModel.getBlues( b );
			for ( int i = 0; i < 256; i++ )
			{
				palR[i] = r[i] & 0xFF;
				palG[i] = g[i] & 0xFF;
				palB[i] = b[i] & 0xFF;
				this.pal[i] = get32BitColor( palR[i], palG[i], palB[i] );
			}
			r = null;
			b = null;
			g = null;
		}
	}
	
	public void setTransparentColor( int color )
	{
		this.transparentColor = color;
	}
	
	/**
	 * Loads a color palette into the current object. The pal array is expected to be 768 bytes
	 * long with a sequential R,G,B repeating pattern with 8 bit color 0 ~ 255 values.
	 * 
	 * @param pal
	 */
	public void loadPalette( int[] pal )
	{
		// define the components and 32bit color value.
		int index = 0;
		for ( int i = 0; i < pal.length-3; i++ )
		{
			palR[index] = pal[i];
			palG[index] = pal[++i];
			palB[index] = pal[++i];			
			this.pal[index] = get32BitColor( palR[index], palG[index], palB[index] );
			index++;
		}
	}
	
	/**
	 * Shifts colors in a given palette. This method performs a looping operation, where the last color
	 * takes place of the first, and the first moves to the second position. 
	 * 
	 * Bounds checking takes place here since this shouldn't be an intensive task. If your bounds 
	 * are out of range, or the start comes after the end, then nothing will happen. 
	 * 
	 * @param start the first color to start shifting
	 * @param end the last color to shift
	 */
	public void shiftColors( int start, int end, int count )
	{
		// temp colors.
		int tmpR = 0;
		int tmpG = 0;
		int tmpB = 0;
		if ( start >= 0 && start <= 255 && end >= 0 && end <= 255 && start < end )
		{
			for ( int i = 0; i < count; i++ )
			{
				for ( int j = end; j > start; i-- )
				{					
					if ( j == end )
					{
						tmpR = palR[j];
						tmpG = palG[j];
						tmpB = palB[j];
					}
					if ( j > start )
					{
						// shift colors forward.
						palR[j-1] = palR[j];
						palG[j-1] = palG[j];
						palB[j-1] = palB[j];
					}
					else
					{
						palR[j] = tmpR;
						palG[j] = tmpG;
						palB[j] = tmpB;
					}
					// Reindex color now that we shifted component values.
					reIndexColor(j); 
				}
			}
		}
	}
	
	/**
	 * Shift the red component for a given color. Value may be positive or negative.
	 * @param color
	 * @param i
	 */
	public void shiftR( int color, int i )
	{
		if ( color + i >= 0 && color + i <= 255 )
		{
			palR[color]+= i;
			reIndexColor( color );
		}
	}
	
	/**
	 * Shift the green component for a given color. Value may be positive or negative.
	 * @param color
	 * @param i
	 */
	public void shiftG( int color, int i )
	{
		if ( color + i >= 0 && color + i <= 255 )
		{
			palG[color]+= i;
			reIndexColor( color );
		}
	}
	
	/**
	 * Shift the blue component for a given color. Value may be positive or negative.
	 * @param color
	 * @param i
	 */
	public void shiftB( int color, int i )
	{
		if ( color + i >= 0 && color + i <= 255 )
		{
			palR[color]+= i;
			reIndexColor( color );
		}
	}
	
	/**
	 * Recalculates the color for an indexed value in the palette. This must be called after shifting (modifying) any of the
	 * red green or blue components. 
	 * @param color
	 */
	private void reIndexColor( int color )
	{
		pal[color] = get32BitColor( palR[color], palG[color], palB[color] );
	}
	
	/**
	 * gets 32bit encoded value for an R,G,B component value.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int get32BitColor( int r, int g, int b )
	{
		/* return r << 16 | g << 8 | b; */
		return ( ( ALPHA & 0xFF ) << 24 ) |
		         ( ( r & 0xFF ) << 16 )   |
		         ( ( g & 0xFF ) << 8 )    |
		         ( ( b & 0xFF ) << 0 );
	}
	
	public int getRedCompontentForColor( int color )
	{
		return this.palR[color];
	}
	
	public int getGreenCompontentForColor( int color )
	{
		return this.palG[color];
	}
	
	public int getBlueCompontentForColor( int color )
	{
		return this.palB[color];
	}
	
	public int getRGB( int color )
	{
		return this.pal[color];
	}
}
