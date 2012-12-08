package org.sandbox;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import org.baraxial.engine.Ball;
import org.baraxial.engine.Brick;
import org.baraxial.engine.Paddle;
import org.baraxial.engine.Ball.BallSpeed;
import org.baraxial.engine.Ball.BallStatus;
import org.baraxial.engine.Brick.Type;
import org.baraxial.engine.Paddle.PaddleSpeed;
import org.baraxial.engine.Paddle.PaddleStatus;
import org.ice.graphics.CollisionHandler;
import org.ice.graphics.Graphics;
import org.ice.graphics.ImageBuffer;
import org.ice.io.Mouse;

public class Baraxial {

  private static int counter = 0;
  private static int counter2 = 100;

//private static final int MAX = 50;

  /* Dev NOTES ** Add your to-do's here as you think of them **
   * 
   *  TODO:
   *  
   * - code cleanup
   * - convert brick array to vector (so that we may drop items from array)
   * - fix bounce logic.. sometimes acts up (goes through paddle)
   * - make it so paddle shoots left when on left side and right when on right side
   * - hide pointer
   * - ball moving straight up destroys all blocks (not good). It appears to not reverse direction.
   * - paddle physics need more tweaking.
   * 
   * 
   * 
   */


public static void main(String args[]) throws Exception {
	
	  Graphics graphics = new Graphics();
	  graphics.init(0); // 0 is 640x480
	  
	  //!TODO handle better.
	  Mouse mouse = new Mouse( graphics.getGraphicsDevice() );

      //TODO! move into initLevel function.
      Brick brick[] = new Brick[14 * 21];
            
      int columnWidth = 0;
      int rowWidth = 0;
      
      for(int columns = 0; columns < 14; columns++)
      {
    	  for(int rows = 0; rows < 21; rows++)
    	  {
    		  double rndColor = Math.random();
    		  int color = 0;
    		  
    		  if(rndColor < 0.1d)
    		  {
    			  rndColor = 0.1d;
    		  }
    		  
    		  if(rndColor > 0.7d)
    		  {
    			  rndColor = 0.7d;
    		  }
    		      		  
    		  color = (int) Math.round(rndColor * 10);
    		  if(color == 0)
    		  {
    			  color = 1;
    		  }
    		  brick[columns + (rows * 14)] = new Brick(20 + columnWidth, 40 + rowWidth, 30, 10, color, (int)Math.round(rndColor * 4)+1, Type.Normal);
    		  
    		  //columnWidth = 0;
    		  rowWidth = rowWidth + 12;    		  
    	  }
    	  
    	  rowWidth = 0;
    	  columnWidth = columnWidth + 32;
      }
      
      //////////////////////////////////////////////////////////////////////////
      // Paddle Initialization
      //////////////////////////
      Paddle paddle;
      //                    x,   y,  w,  h, c,   s,              Speed,              Status
      paddle = new Paddle(320, 420, 45, 10, 6, 100, PaddleSpeed.Normal, PaddleStatus.Normal);
      
      Ball ball;
      
      ball = new Ball(300, 400, 4, 4, 7, 100, BallSpeed.Normal, BallStatus.Normal);
      
      CollisionHandler collisionHandler = new CollisionHandler();
      
      // Add bricks
      for ( int i = 0; i < brick.length; i++ )
      {
    	  collisionHandler.addSpriteObject( brick[i] );
      }
      
      // Add rest of items
      collisionHandler.addSpriteObject( paddle );
      collisionHandler.addSpriteObject( ball );
      
      long time = System.currentTimeMillis()+1000;
      int clock = 0;
      int fps = 0;
      
      ImageBuffer logo = graphics.loadImage( "./gfx/wombat_logo2.bmp", false );
      
      while (!done()) {
    	  clock++;
    	  if ( System.currentTimeMillis() > time )
    	  {
    		  fps = clock;
    		  clock = 0;
    	      time = System.currentTimeMillis()+1000;
    	      //System.out.println ( fps );
    	  }
    	  graphics.clearScreen( 0 );
         
    	  // check for collisions:
    	  collisionHandler.checkCollision();
    	  
    	  graphics.line( counter - 1, (counter - 1) * 5, counter2 - 1, (counter2 - 1) * 5, 2 );
    	  graphics.line( counter - 1 +100, (counter - 1) * 5, counter2 - 1, (counter2 - 1) * 5, 3  );
    	  graphics.line( counter2 - 1 +300, (counter2 - 1) * 5, counter - 1, (counter - 1) * 5, 4  );
/*
    	  graphics.graphics().drawString( "TEST!", 1, 1 ); 
    	  graphics.graphics().setColor(Color.RED);
			
    	  graphics.graphics().setColor( Color.WHITE );
    	  graphics.graphics().drawString( "Mouse: " + String.valueOf( mouse.getMouseX() ) + "|" + String.valueOf( mouse.getMouseY() ), 20, 20 );
    	  graphics.graphics().drawString( "FPS:" + fps, 20, 30 );
*/    	  
          for(int columns = 0; columns < 14; columns++)
          {
        	  for(int rows = 0; rows < 21; rows++)
        	  {
        		  
        		  if ( brick[columns + (rows * 14)].strength < 1 )
        		  {
        			  collisionHandler.removeSpriteObject( brick[columns +(rows * 14 )] );
        		  }
        		  // !TODO switch array to vector so we can physically remove them.
        		  else
        		  {
            		  brick[columns + (rows * 14)].DrawBrick( graphics );  

        		  }
        	  }
          }

          paddle.current_x = mouse.getMouseX();
          //paddle.screen_y = mouse.getMouseY();		// This would allow the paddle to move up or down as well.
          
          paddle.DrawPaddle( graphics );
          ball.DrawBall( graphics );
          //graphics.drawImage( logo, 200, 200, false );
    	  graphics.render();
    	  Thread.sleep(12);
         
    	  if ( mouse.getMouseButton() == Mouse.MouseClick.RIGHT )
    	  {
    		  break;
    	  }
      }
      
      if ( graphics != null) {
            graphics.exit();
      }

      System.exit(0);
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