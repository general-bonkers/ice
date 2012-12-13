package org.sandbox;

import java.util.Vector;

public class Vectors {

	public static void main ( String args[] )
	{
		// object type goes between < >
		Vector<String> message = new Vector<String>();

		String user = "Adam";
		String user2 = "Jeff";
		String user3 = "Bob";
		
		System.out.println ( "Adding 2 users.." );
		message.add( user );		
		message.add( user3 );
		
		
		System.out.println( "UH-OH, I think we forgot someone..." );
		if ( !message.contains( user2 ) )
		{
			System.out.println( "Yes! We forgot Jeff!" );
			message.add( user2 ); 
		}
		
		System.out.println( "We now have " );
		System.out.println ( message.size() );
		System.out.println ( "Users!" );
		System.out.println ();
		
		for ( int i = 0; i < message.size(); i++ )
		{
			System.out.println( i + "-" + message.get( i ) );
		}
		System.out.println ();

		System.out.println ( "Deleting users! " );
		
		// lets delete some by object name
		message.remove( user3 );
		// and by index
		message.remove( 1 );
		message.remove( 0 );
		
		System.out.println ();
		System.out.println ( message.size() + " Users left! " );

	}
}
