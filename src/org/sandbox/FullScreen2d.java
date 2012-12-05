package org.sandbox;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import org.ice.graphics.io.Draw;
import org.ice.io.Mouse;
import org.sandbox.Brick.Type;

public class FullScreen2d {

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

  @SuppressWarnings("null")
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

      Brick b[] = new Brick[14 * 22];
      
      int columnWidth = 0;
      int rowWidth = 0;
      
      for(int columns = 0; columns < 14; columns++)
      {
    	  for(int rows = 0; rows < 22; rows++)
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
    		  b[columns + (rows * 14)] = new Brick(20 + columnWidth, 40 + rowWidth, 32, 12, color, 1, Type.Standard);
    		  
    		  //columnWidth = 0;
    		  rowWidth = rowWidth + 14;    		  
    	  }
    	  
    	  rowWidth = 0;
    	  columnWidth = columnWidth + 34;
      }
            
      while (!done()) {
    	  draw.cls( 0 );
         
    	  draw.line( counter - 1, (counter - 1) * 5, counter2 - 1, (counter2 - 1) * 5, 2 );
    	  draw.line( counter - 1 +100, (counter - 1) * 5, counter2 - 1, (counter2 - 1) * 5, 3  );
    	  draw.line( counter2 - 1 +300, (counter2 - 1) * 5, counter - 1, (counter - 1) * 5, 4  );
    	  draw.graphics().drawString( "TEST!", 1, 1 ); 
    	  draw.graphics().setColor(Color.RED);
			
    	  draw.graphics().setColor( Color.WHITE );
    	  draw.graphics().drawString( "Mouse: " + String.valueOf( mouse.getMouseX() ) + "|" + String.valueOf( mouse.getMouseY() ), 20, 20 );

          for(int columns = 0; columns < 14; columns++)
          {
        	  for(int rows = 0; rows < 22; rows++)
        	  {
        		  b[columns + (rows * 14)].DrawBrick( draw );       		  
        	  }
          }

    	  
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