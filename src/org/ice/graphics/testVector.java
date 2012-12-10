package org.ice.graphics;



import org.ice.graphics.i3d.vector.Point;
import org.ice.graphics.i3d.vector.VectorObject;
import org.ice.graphics.i3d.vector.WireVectorObject;

public class testVector {

	
	
	
	public static void main ( String args[] ) throws Exception
	{
		Graphics g = new Graphics();

		g.init( 0 );
		Thread.sleep( 400 );
		
		WireVectorObject prism = new WireVectorObject();
		Point camera = new Point();
				
		
		
		int maxX = 640;
		int maxY = 480;
		
		// Load points
		prism.addPoint( 30, 1, 1 );
		prism.addPoint( 1, 30, 1 );
		prism.addPoint( 1, 1, 30 );
		prism.addPoint( -30, -30, -30 );
		
		prism.addLine( 0, 1 );
		prism.addLine( 0, 2 );
		prism.addLine( 0, 3 );
		prism.addLine( 1, 2 );
		prism.addLine( 1, 3 );
		prism.addLine( 2, 3 );
		camera.setPoint( -30, -40, -300 );
		
		// Calculate Camera coordinates
		prism.moveObject( camera );		
		Point velocity = new Point( -1, -1, -1 );
		Point rotation = new Point ( 1, 0, 0 );
		for ( int i = 0; i < 100; i++ )
		{
			// Inverted since calculating screen cordinates require subtracting camera from Prism!
			prism.rotateObject( rotation );
			prism.calculateScreenCoords( maxX, maxY );
			g.clearScreen(0);
			prism.render( g );
			g.box( 200, 200, 10, 10, 14, false );
			g.render();
			Thread.sleep(50);
		}
		Thread.sleep( 2000 );
		g.exit();
		System.exit(0);
	}
}
