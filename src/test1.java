import java.awt.Rectangle;


public class test1 {

	
	public static void main ( String args[] )
	{
		Rectangle r1 = new Rectangle ( 10,10,10,10 );
		Rectangle r2 = new Rectangle ( 8,8,10,10 );
		
		System.out.println ( r1.intersects( r2 ) ); 
		
		Rectangle r3 = r1.intersection( r2 );
		
		System.out.println ( r3.x );
		System.out.println ( r3.y );
		System.out.println ( r3.width );
		System.out.println ( r3.height );
		
		
		
	}
}
