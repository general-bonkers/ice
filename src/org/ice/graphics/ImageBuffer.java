package org.ice.graphics;

public class ImageBuffer {

	public int width;
	public int height;
	private int[] buffer;
	
	public ImageBuffer( int width, int height )
	{
		this.width = width;
		this.height = height;
		buffer = new int[ width + ( width * height ) ];
	}
	
	public int[] getBuffer()
	{
		return this.buffer;
	}
}
