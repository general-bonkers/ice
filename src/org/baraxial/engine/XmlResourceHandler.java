package org.baraxial.engine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlResourceHandler {

    private SAXBuilder builder;
    
    public XmlResourceHandler()
    {
    	builder = new SAXBuilder();
    }
	
    public static void main ( String args[] )
    {
    	XmlResourceHandler xmlResourceHandler = new XmlResourceHandler();
    	xmlResourceHandler.loadLevelPack( "./data/levels.xml" );
    }
    /**
     * Loads a levelPack from disk and returns a campaign object.
     * 
     * @param levelPack
     */
	public void loadLevelPack( String levelPack )
	{
		//!TODO better error handling.
		  File xmlFile = new File( levelPack );
	 
		  try {
	 
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List levels = rootNode.getChildren("level");
	 	    System.out.println ( levels.size() + " levels " );
			
			// Iterate over each level
			for (int i = 0; i < levels.size(); i++) {
	 
			   Element level = (Element)levels.get(i);
			   
			   // Title
			   System.out.println ( level.getAttributeValue( "title" ) );
			   		   
			   List bricks = level.getChild("bricks").getChildren( "brick" );
			   System.out.println ( bricks.size() + " bricks " );

			   // Iterate over bricks
			   for ( int j = 0; j < bricks.size(); j++ )
			   {
				   Element brick = (Element)bricks.get( j );
				   System.out.print ( "Brick " + ( j + 1 ) + ": " +  brick.getAttributeValue( "column" ) + " " );
				   System.out.print ( brick.getAttributeValue( "row" ) + " " );
				   System.out.print ( brick.getAttributeValue( "color" ) + " "  );
				   System.out.print ( brick.getAttributeValue( "strength" ) + " "  );
				   System.out.print ( brick.getAttributeValue( "type" ) + "\n"  );
			   }

			   List balls = level.getChild("balls").getChildren( "ball" );

			   // Iterate over balls in level
			   System.out.println ( balls.size() + " ball(s) " );
			   for ( int j = 0; j < balls.size(); j++ )
			   {
				   Element ball = (Element)balls.get( j );
				   System.out.print ( "Ball " + j+1 + ": " );
				   System.out.print ( ball.getAttributeValue( "xpos" ) + " " );
				   System.out.print ( ball.getAttributeValue( "ypos" ) + " " );
				   System.out.print ( ball.getAttributeValue( "speed" ) + " " );
	  		       System.out.print ( ball.getAttributeValue( "status" ) + "\n" );
			   }
			   
			   // Iterate over misc objects
			   List doodads = level.getChild( "doodads" ).getChildren( "doodad" );

			   // Iterate over balls in level
			   System.out.println ( doodads.size() + " doodad(s) " );
			   for ( int j = 0; j < doodads.size(); j++ )
			   {
				   Element doodad = (Element)doodads.get( j );
				   System.out.print ( "doodad: " + j+1 + ": " );
				   System.out.print ( doodad.getAttributeValue( "name" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "xpos" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "ypos" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "frequency" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "speed" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "color" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "ballspeed" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "killball" ) + " " );
				   System.out.print ( doodad.getAttributeValue( "ballstatus" )  + "\n ");
			   }		   		    
			}
	 
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
	}
}
