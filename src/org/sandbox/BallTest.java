package org.sandbox;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.Vector;

import org.baraxial.engine.Brick;
import org.baraxial.engine.Brick.Type;
import org.ice.graphics.io.Draw;
import org.ice.io.Mouse;
import org.ice.math.Ice2DVector;

public class BallTest {

  private static int counter = 0;
  private static int counter2 = 100;
  private static final int MAX = 50;

  private static DisplayMode MODES[] = new DisplayMode[] {
      new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 16, 0),
      new DisplayMode(640, 480, 8, 0) };

  private static DisplayMode getBestDisplayMode(GraphicsDevice device) {
    for (int x = 0, xn = MODES.length; x < xn; x++) {
      DisplayMode[] modes = device.getDisplayModes();
      for (int i = 0, in = modes.length; i < in; i++) {
        if (modes[i].getWidth() == MODES[x].getWidth()
            && modes[i].getHeight() == MODES[x].getHeight()
            && modes[i].getBitDepth() == MODES[x].getBitDepth()) {
          return MODES[x];
        }
      }
    }
    return null;
  }

  
  public static void main(String args[]) throws Exception {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
        .getLocalGraphicsEnvironment();
    GraphicsDevice graphicsDevice = graphicsEnvironment
        .getDefaultScreenDevice();
    DisplayMode originalDisplayMode = graphicsDevice.getDisplayMode();

      Frame frame = new Frame();
      frame.setUndecorated(true);
      frame.setIgnoreRepaint(true);
      
      
      
      graphicsDevice.setFullScreenWindow(frame);
      if (graphicsDevice.isDisplayChangeSupported()) {
        graphicsDevice
            .setDisplayMode(getBestDisplayMode(graphicsDevice));
      }
      frame.createBufferStrategy(2); // 2 buffers
      Rectangle bounds = frame.getBounds();
      
      Mouse mouse = new Mouse( graphicsDevice );
      
      
      BufferStrategy bufferStrategy = frame.getBufferStrategy();
      Draw draw = new org.ice.graphics.io.Draw( bufferStrategy );
      Brick[] bricks = new Brick[10];
                  
      bricks[0] = new Brick(0, 124, 32, 12, 1, 1, Type.Normal);
      bricks[1] = new Brick(124, 0, 32, 12, 2, 1, Type.Normal);
      bricks[2] = new Brick(255, 124, 32, 12, 2, 1, Type.Normal);
      bricks[3] = new Brick(124, 255, 32, 12, 4, 1, Type.Normal);
      bricks[4] = new Brick(124, 300, 32, 12, 5, 1, Type.Normal);
      bricks[5] = new Brick(300, 124, 32, 12, 6, 1, Type.Normal);
      bricks[6] = new Brick(124, 124, 32, 12, 1, 1, Type.Normal);
      bricks[7] = new Brick(500, 124, 32, 12, 2, 1, Type.Normal);
      bricks[8] = new Brick(124, 424, 32, 12, 3, 1, Type.Normal);
      bricks[9] = new Brick(524, 224, 32, 12, 5, 1, Type.Normal);
      
	  Ice2DVector v3 = new Ice2DVector();
	  Ice2DVector paddle = new Ice2DVector();
	  Ice2DVector ballPaddleIntersect = new Ice2DVector();
	  paddle.x = 20;
	  paddle.y = 20;
	  paddle.vy = 0;
	  paddle.vx = 640;
	  
      long time = 0;
      long currentFrame = 0;
      long lastFrame = 0;    
      Ball ball = new Ball();
      ball.x = 50;
      ball.y = 350;
      ball.vx = 2;
      ball.vy = 1;
	  Rectangle ballRect = new Rectangle ( ball.x, ball.y, 15, 15 ); 

      
      while (!done()) {
    	  time = System.currentTimeMillis();
    	     	  
    			      	  
    	  draw.cls( 0 );
    	  
    	  for ( int i = 0; i < bricks.length; i++ )
    	  {
    		  bricks[i].DrawBrick( draw );
    	  }
    	
    	  currentFrame = ( time - ball.timeFrame ) / 1000;
    	  // last frame needs to exist for each object!
//    	  ball.x = Math.round( ball.x + ball.vx * currentFrame );
//    	  ball.y = Math.round( ball.y + ball.vy * currentFrame );
    	  ball.x = Math.round( ball.x + ball.vx  );
    	  ball.y = Math.round( ball.y + ball.vy  );
    	  ballRect.setLocation( ball.x, ball.y );
    	  ball.timeFrame = currentFrame;

    	  // Visit: http://www.tonypa.pri.ee/vectors for more info.
    	  //draw.box( ball.x, ball.y, 15, 15, 4, true );
    	  draw.circle( ball.x, ball.y, 8, 15, true );
    	  draw.circle( ball.x, ball.y, 8, 8, false );
    	  // Calculate V3
    	     	  
    	  
    	  
     	  // Detect bottom & top face.
    	  for ( int i = 0; i < bricks.length; i++ )
    	  {
    		  System.out.println ( ballRect.toString() );
    		  System.out.println( bricks[i].getRectangle() );
    		  if ( ballRect.intersects( bricks[i].getRectangle() ) )
    		  {
    			  if ( bricks[i].current_y >= ball.y && bricks[i].current_x >= ball.x )
    			  {
    				  if ( bricks[i].current_y - ball.y > bricks[i].current_x - ball.x && ball.vx > 0 )
    				  {
    					  ball.vy = -ball.vy;    					  
    				  }
    				  else
    				  {
    					  ball.vx = - ball.vx;
    				  }
    			  }
    		    
	    		  else if ( bricks[i].current_y >= ball.y && bricks[i].current_x <= ball.x )
	    		  {
	    			  if ( bricks[i].current_y - ball.y > bricks[i].current_x - ball.x && ball.vx > 0 )
	    			  {
	    				  ball.vy = -ball.vy;    					  
    				  }
    				  else
    				  {
    					  ball.vx = - ball.vx;
    				  }
				  }

    		      else if ( bricks[i].current_y <= ball.y && bricks[i].current_x >= ball.x )
			      {
					  if ( bricks[i].current_y - ball.y > bricks[i].current_x - ball.x && ball.vx < 0 )
					  {
						  ball.vy = -ball.vy;    					  
					  }
					  else
					  {
						  ball.vx = - ball.vx;
					  }
			      }
		    
				  else if ( bricks[i].current_y <= ball.y && bricks[i].current_x <= ball.x )
				  {
					  if ( bricks[i].current_y - ball.y > bricks[i].current_x - ball.x && ball.vx < 0 )
					  {
						  ball.vy = -ball.vy;    					  
					  }
					  else
					  {
						  ball.vx = - ball.vx;
					  }
				  }
      		}
    }  
    	  
    	  // Wall Collision must come last!
    	  if ( ball.y + 1 > 400 )
    		  ball.vy = -ball.vy;
    	  else if ( ball.y -1 < 1 )
    		  ball.vy = -ball.vy;

    	  if ( ball.x + 1 > 600 )
    		  ball.vx = -ball.vx;
    	  else if ( ball.x -1 < 1 )
    		  ball.vx = -ball.vx;

    	  
    	  
/*    	  
    	  v3.vx = paddle.x - ball.x;
    	  v3.vx = paddle.y - ball.y;
    	  int t = 0;
    	  if ( perpP( ball, paddle ) > 0 )
    		  t = perpP( v3, paddle ) / perpP( ball, paddle );
    	  
    	  ballPaddleIntersect.x = ball.x + paddle.vx * t;
    	  ballPaddleIntersect.y = ball.y + paddle.vy * t;
*/    	  
    	  // Check for collision

    	  
    	  
    	  draw.line( counter - 1, (counter - 1) * 5, counter2 - 1, (counter2 - 1) * 5, 2 );
    	  draw.line( counter - 1 +100, (counter - 1) * 5, counter2 - 1, (counter2 - 1) * 5, 3  );
    	  draw.line( counter2 - 1 +300, (counter2 - 1) * 5, counter - 1, (counter - 1) * 5, 4  );
    	  draw.graphics().drawString( "TEST!", 1, 1 ); 
    	  draw.graphics().setColor(Color.RED);
			
    	  draw.graphics().setColor( Color.WHITE );
    	  draw.graphics().drawString( "Mouse: " + String.valueOf( mouse.getMouseX() ) + "|" + String.valueOf( mouse.getMouseY() ), 20, 20 );
    	  String message = "Ball: " + ball.x + "," + ball.y + " : timeFrame " +  ball.timeFrame;    	  
    	  draw.graphics().drawString( message, 20, 30 );
    	  bufferStrategy.show();
    	  Thread.sleep(16);
         
    	  if ( mouse.getMouseButton() == Mouse.MouseClick.RIGHT )
    	  {
    		  break;
    	  }
      }
      
      if (draw != null) {
            draw.dispose();
      }

      graphicsDevice.setDisplayMode(originalDisplayMode);
      graphicsDevice.setFullScreenWindow(null);
      System.exit(0);
  }

  
  
  /**
   * Figures Perp Product for two given vectors.
   * @return 
   */
  public static int perpP( Ice2DVector vectorA, Ice2DVector vectorB )
  {
	  System.out.println( ( vectorA.vx * vectorB.vy ) - ( vectorA.vy * vectorB.vx ) + "] " + ( vectorA.vx * vectorB.vy ) + " | " + ( vectorA.vy * vectorB.vx ) ) ;
	  return ( vectorA.vx * vectorB.vy ) - ( vectorA.vy * vectorB.vx );
  }
  
  private static boolean done() {
    //return (counter++ == MAX);
	counter++;
	counter2--;
	if ( counter > 100 )
	{
		counter = 0;
		counter2 = 100;
	}
	return false;
  }
  
}
class Ball extends Ice2DVector
{
	long timeFrame = 0;
}
