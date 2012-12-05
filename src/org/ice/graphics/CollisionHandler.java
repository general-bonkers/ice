package org.ice.graphics;

import java.util.Vector; 

/**
 * Base class for handling collisions.
 * 
 * @author home
 *
 */
public class CollisionHandler {

	private Vector<SpriteObject> spriteObjects; // Threadsafe
	
	public CollisionHandler()
	{
		spriteObjects = new Vector();		
	}
	
	public void addSpriteObject( SpriteObject spriteObject )
	{
		spriteObjects.add( spriteObject );
	}
	
	public void checkCollision()
	{
		for (int i = 0; i < spriteObjects.size(); i++ )
		{
			SpriteObject srcObject = spriteObjects.get( i );
			
			// Only check active objects
			if ( srcObject.isActive )
			{
				for ( int j = 0; j < spriteObjects.size(); i++ )
				{
					if ( i != j )
					{
						SpriteObject destObject = spriteObjects.get( j );
						if ( srcObject.getRectangle().intersects( destObject.getRectangle() ) )
						{
							srcObject.handleCollision( destObject );
						}
					}
				}
			}
			
		}
	}
}
