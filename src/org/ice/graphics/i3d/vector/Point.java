package org.ice.graphics.i3d.vector;

public class Point {
	public double x;
	public double y;
	public double z;
	
	public double x2d;
	public double y2d;
	
	public Point()
	{
		x = 0;
		y = 0;
		z = 0;		
		x2d = 0;
		y2d = 0;
	}
	
	public void set2d( double x, double y )
	{
		x2d = x;
		y2d = y;
			
	}
	
	public Point( double x, double y, double z )
	{
		setPoint( x, y, z );
	}
	
	/**
	 * Moves a point by a specified amount. Negative or positive values may be passed. If you wish
	 * to exclude an axi, simply pass 0.0 for that argument.
	 *   
	 * @param vx
	 * @param vy
	 * @param vz
	 */
	public void movePoint( double vx, double vy, double vz )
	{
		x += vx;
	    y += vy;
	    z += vz;
	}
	
	/**
	 * Sets a location for a point based on the values passed through.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPoint( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;		
	}
}
