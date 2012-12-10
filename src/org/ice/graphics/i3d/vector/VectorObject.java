package org.ice.graphics.i3d.vector;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Simple class for containing a Vector Object. A Vector Object is a collection of points and lines for 3d use.
 * 
 * @author home
 *
 */

    public abstract class VectorObject {
	protected ArrayList <Line>lines;             //
	protected ArrayList <Point>points;           //

	/**
	 * Creates a new vector object.
	 */
	public VectorObject()
	{
		this.lines = new ArrayList<Line>();
		this.points = new ArrayList<Point>();
	}
	
	/**
	 * Add a 3d <code>Point</code> to the object.
	 * @param point
	 */
	public int addPoint( Point point )
	{
		points.add( point );
		return points.indexOf( point );
	}

	/**
	 * Add a 3d <code>Point</code> using x,y,z coordinates
	 * @param x
	 * @param y
	 * @param z
	 */
	public int addPoint( double x, double y, double z )
	{
		Point point = new Point( x, y, z );
		points.add( point );
		return points.indexOf( point );		
	}
	
	/**
	 * Adds a new line from existing points within the object. If the points in the argument 
	 * are not first part of the object, then the line will not be created. No checking is
	 * currently done for multiple lines, so be careful about adding duplicates.
	 * @param start
	 * @param end
	 */
	public int addLine( Point start, Point end )
	{
		int seg = -1;
		if ( points.contains( start ) && points.contains( end ) )
		{			
			Line newLine = new Line( start, end );
			lines.add( newLine );
			seg = lines.indexOf( newLine );
		}
		return seg;
	}
	
	public int addLine( int startSeg, int endSeg )
	{
		int seg = -1;
		if ( points.size() >= startSeg && points.size() >= endSeg )
		{
			seg = addLine( points.get( startSeg ), points.get( endSeg ) );
		}
		return seg;
	}
	
	/**
	 * This method moves an object by iterating through all of the points and adding
	 * the velocity to each point. Velocity may be positive or negative.
	 *  
	 * @param velocity amount you wish to move the object by
	 */
	public void moveObject( Point velocity )
	{
		for ( int i = 0; i < points.size(); i++ )
		{
			points.get(i).x += velocity.x;
			points.get(i).y += velocity.y;
			points.get(i).z += velocity.z;
		}
	}
	/**
	 * This is simlar to <code>VectorObject.moveObject</code> with the exception that the 
	 * the object is actually placed in the location described by the argument.
	 * 
	 * @param location
	 */
	public void setObjectLocation( Point location )
	{
		for ( int i = 0; i < points.size(); i++ )
		{
			points.get(i).x = location.x;
			points.get(i).y = location.y;
			points.get(i).z = location.z;
		}		
	}
	
	public void calculateScreenCoords( double worldW, double worldH )	
	{
		double x, y;
		Point point;
		for ( int i = 0; i < points.size(); i++ )
		{
			point = points.get( i );			
		    x =  point.x / point.z  *  worldW + worldW / 2 ;
			y =  point.y / point.z  *  worldH + worldH / 2 ;
			point.set2d( x, y );
		}
	}
	
	/** This method is broken **/
	public void rotateObject( Point rotation )
	{
		Point point;
		for ( int i = 0; i < points.size(); i++ )
		{			
			point = points.get( i );
			double xy, yx;
			rotation.x = Math.PI / ( 180 / rotation.x );
			if ( rotation.x != 0 )
			{				
				//point.x += ( point.x * Math.cos( rotation.x ) ) - ( point.y * Math.sin( rotation.x ) );
				//point.y += ( point.y * Math.cos( rotation.x ) ) + ( point.y * Math.sin( rotation.x ) );
				point.y = point.y  * Math.cos( rotation.x )  - point.z * Math.sin( rotation.x );
				point.z = point.y * Math.sin( rotation.x ) + point.z * Math.cos( rotation.x );
				
				/*
				angle = pi / (180 / angle)
						FOR i = 1 TO nbpoint
						XY = (coor(i, 1) - coor(0, 1)) * COS(angle) - (coor(i, 2) - coor(0, 2)) * SIN(angle)
						YX = (coor(i, 2) - coor(0, 2)) * COS(angle) + (coor(i, 1) - coor(0, 1)) * SIN(angle)
						coor(i, 1) = XY + coor(0, 1)
						coor(i, 2) = YX + coor(0, 2)
				*/

			}
			if ( rotation.y != 0 )
			{
				point.x += point.x * Math.cos( rotation.y ) + point.z * Math.sin( rotation.y );
				point.z += -point.x * Math.sin( rotation.y ) + point.z * Math.cos( rotation.y );
			}
			if ( rotation.z != 0 )
			{
				point.y += point.y * Math.cos ( rotation.z ) - point.z * Math.sin( rotation.z );
				point.z += point.y * Math.cos( rotation.z ) + point.z * Math.cos( rotation.z );
			}
		}
		
	}
	
	public int getPoints()
	{
		return points.size();
	}
	
	public int getLines()
	{
		return lines.size();
	}
	
	public Point getPoint( int seg )
	{
		return points.get( seg );
	}
	
	public Line getLine( int seg )
	{
		return lines.get( seg );
	}
		
}
