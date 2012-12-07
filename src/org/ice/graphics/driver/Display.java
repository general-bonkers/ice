package org.ice.graphics.driver;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import org.ice.graphics.DOSPalette;

/**
 * 8-bit Display driver for ice. This class interfaces directly with the Java graphics api layer. This layer is the one that 
 * performs the actual rendering.Very little bounds checking is performed.
 * 
 * The system uses a buffered image to write to. The array which holds the image data is stored in the frameBuffer object. This is
 * what the engine writes to. The imagebuffer is then rendered to the screen upon request. Using this approach, only a single buffer
 * is needed to enable flicker free animation.
 * 
 * The Palette object stores indexed color infrmation invarious forms. Since the Color object requires RGBA values, the Palette class stores 
 * index, component, and RGBA color values. On the developer/API side of the engine, the indexed color values are seen and used for graphical
 * manipulation. On the rendering side however, the indexed colors are quickly converted to RGBA before rendering.
 * 
 * TODO Look for java graphics performance options.
 * 
 * Perf test (brute force pixel push with lookup)
 * 640x480 * 10 (10 passes on the filling the 'screen') with one render after every 10 passes: 52fps
 *  
 * @author home
 *
 */
public class Display {

	/*
	 * Display mode 0 (640x480)
	 * Display mode 1 (800x600)
	 */
	public static final int COLORS = 256;  // don't touch this.
	public static final int VID_PAGES = 2; // video pages (each page is a page  buffer)
	public static DisplayMode DISPLAY_MODES[][] = new DisplayMode[][] 
			{ { new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 16, 0) }, 
			{ new DisplayMode(800, 600, 32, 0), new DisplayMode(800, 600, 16, 0) } };
			
	private Frame frame;                    // main frame object for drawing to screen
	private Graphics frameGraphics;         // writer for visual frame.
	private BufferStrategy bufferStrategy;  // bufferStrategy for handling video
	private Palette palette;                // Main palette;
	private int[] iceBuffer;                // video Buffer for writing.
	private int[] screenBuffer;
	private BufferedImage screen;           // transport from buffer to screen.
	private Graphics videoPageGraphics;     // writer to video page (for fast primitives).	
	public int width;                      // display width
	public int height;                     // display height
	//public int currentVideoPage;          // current "active" video page.
	public GraphicsDevice graphicsDevice;
	private DisplayMode originalDisplayMode;
	
	private Color color;                    // This is required for drawing. 
	
	public Display()
	{
	}
	
	
	public static void main ( String args[] ) 
	{
		try {
		Display display = new Display();
		display.init(0, true );
		int col=1;
		// performance test
		long timer = 0;
		int fps = 0;
		int counter = 0;

    	display.lineP( -30, 10, 30, 40, 2 );

    	display.boxP( 40, 40, 20, 10, 3, true );

    	display.circleP( 200, 200, 20, 5, false );
    	
    	display.pokeP( 100, 100, 4 );
    	display.pokeP( 101, 101, 4 );
    	display.pokeP( 102, 102, 4 );
    	display.pokeP( 103, 103, 4 );
    	//display.lineP( 100, 100, 30, 40, 3 );

    	int a = 0;
		display.renderFrameBuffer();

		//System.out.println ("Fps:" + fps);
		Thread.sleep(2000);
		
		
		
		display.returnToDesktop();
		System.exit(0);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public int[] getVideoBuffer()
	{
		return this.iceBuffer;
	}
	
	public void returnToDesktop()
	{
	    graphicsDevice.setDisplayMode(originalDisplayMode);
	    graphicsDevice.setFullScreenWindow(null);
		bufferStrategy.dispose();
	    frameGraphics.dispose();
	    videoPageGraphics.dispose();
	    frame.dispose();
	}
	/**
	 * Initializes the display driver for engine using a defined display mode. fullScreen is not implemented yet (always fullscreen)
	 * 
	 * Current Modes:
	 *  0 - 640x480
	 *  1 - 800x600
	 *  	  
	 * @param displayMode
	 * @param fullScreen
	 */
	public void init( int displayMode, boolean fullScreen )
	{
	    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
	            .getLocalGraphicsEnvironment();
	        graphicsDevice = graphicsEnvironment
	            .getDefaultScreenDevice();
	        originalDisplayMode = graphicsDevice.getDisplayMode();

	          frame = new Frame();
	          frame.setUndecorated(true);
	          frame.setIgnoreRepaint(true);
	          
	          graphicsDevice.setFullScreenWindow(frame);   
	          if (graphicsDevice.isDisplayChangeSupported()) {
	            graphicsDevice
	                .setDisplayMode(getBestDisplayMode(graphicsDevice, displayMode ));
	          }
	          
	          frame.createBufferStrategy(1); // 2 buffers	                
	          bufferStrategy = frame.getBufferStrategy();
	          frameGraphics = frame.getGraphics();

	          this.width = frame.getWidth();
	          this.height = frame.getHeight();
	          iceBuffer = new int[width + ( width * height ) ];	  		  

	  			  screen = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
	  			  videoPageGraphics = screen.getGraphics();
	  			  screenBuffer = ((DataBufferInt)screen.getRaster().getDataBuffer()).getData();	  			  
	  		  //currentVideoPage = 0;
	  		  
	  		  // Init palette
	  		  palette = new Palette( DOSPalette.pal );
	  		  

	  		  
	}
	
	public void renderFrameBuffer()
	{
		int offset = ( width * height );		
		for ( int i = 0; i < offset; i++ )
		{
			screenBuffer[i] = palette.getRGB( iceBuffer[i] );
		}
		frameGraphics.drawImage( screen, 0, 0, null );		
		bufferStrategy.show();
	}
	
	 /**
	  * Finds the best display mode for a given system based on the displayMode number selected.
	  * See DISPLAY_MODES[][] for more info.
	  * 
	  * Mode 0 - 640x480
	  * Mode 1 - 800x600
	  * 
	  * @param device
	  * @param displayMode
	  * @return
	  */
	 private static DisplayMode getBestDisplayMode(GraphicsDevice device, int displayMode ) {
	    for (int x = 0, xn = DISPLAY_MODES[displayMode].length; x < xn; x++) 
	    {
	      DisplayMode[] deviceModes = device.getDisplayModes();
	      for (int i = 0, in = deviceModes.length; i < in; i++) {
	        if (deviceModes[i].getWidth() == DISPLAY_MODES[displayMode][x].getWidth()
	            && deviceModes[i].getHeight() == DISPLAY_MODES[displayMode][x].getHeight()
	            && deviceModes[i].getBitDepth() == DISPLAY_MODES[displayMode][x].getBitDepth()) {
	          return DISPLAY_MODES[displayMode][x];
	        }
	      }
	    }
	    return null;
	 }
/*
 * ------------------------------------ Primatives --------------------------	 
 */
	 
	/**
	 * Clear screen primative. 
	 * @param color
	 */
	public void clearScreenP( int color )
	{
		// set the Color object value from the indexed color
		this.color = new Color( palette.getRGB( color ) );
		videoPageGraphics.setColor( this.color );
		videoPageGraphics.fillRect( 0, 0, width, height );
		//temp workaround for now.
		for ( int i = 0; i < width * height; i++ )
		{
			iceBuffer[i] = 0;
		}
	}

	
	/**
	 * Line draw primative.
	 * 
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @param color
	 */
	public void lineP( int x, int y, int x2, int y2, int color )
	{
		// http://en.wikipedia.org/wiki/Bresenham's_line_algorithm
		int sx = 0;
		int sy = 0;
		int e2 = 0;

		int dx = Math.abs(x2 - x );
		int dy = Math.abs(y2 - y );

		if (x < x2 )
			sx = 1;
		else 
			sx = -1;

		if ( y < y2 )
			sy = 1;
		else 
			sy = -1;

		int err = dx - dy;
		
		//!TODO suppress warning for this line.
		while ( 1 == 1 )
		{
			pokeP( x, y, color );
			if ( x == x2 && y == y2 )
			{
				break;
			}
			e2 = 2 * err;
			if (e2 > -dy )
			{
				err = err - dy;
				x = x + sx;
			}
			if ( e2 <  dx )
			{
				err = err + dx;
				y = y + sy;
			}
		}
	}
	/**
	 * 	Box draw primative. 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param color
	 * @param bf boxfill boolean
	 */
	public void boxP( int x, int y, int w, int h, int color, boolean bf )
	{
		// set the Color object value from the indexed color
		if ( bf )
		{
			w += x;
			h += y;
			int tmp = 0;
			if ( x > w )
			{
				x = tmp;
				x = w;
				w = tmp; 
			}
			
			if ( y > h )
			{
				y = tmp;
				y = h;
				h = tmp; 
			}
			for ( int yy = y; yy < h; yy++ )
			{
				for ( int xx = x; xx < w; xx++ )
				{
					pokeP( xx, yy, color );
				}
			}
			
			//videoPageGraphics.fillRect( x, y, w, h );
		}
		else
		{
			lineP ( 120,120, 120,150, 8 );
			// sides
			lineP( x, y, x, h + y, color );
			lineP( w + x, y, w + x, h + y, color );
			
			// top and bottom
			lineP( x, y, w + x, y, color );
			lineP( x, h + y, w + x, h + y, color );
		}
	}
	
	/**
	 * Circle draw primative. 
	 * 
	 * @param x
	 * @param y
	 * @param diameter
	 * @param color
	 * @param cf circlefill boolean
	 */
	public void circleP( int x0, int y0, int radius, int color, boolean cf )
	{
		// http://en.wikipedia.org/wiki/Midpoint_circle_algorithm
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;

		pokeP(x0, y0 + radius, color );
		pokeP(x0, y0 - radius, color );
		pokeP(x0 + radius, y0, color );
		pokeP(x0 - radius, y0, color );

		while(x < y)
		{
		// ddF_x == 2 * x + 1;
		// ddF_y == -2 * y;
		// f == x*x + y*y - radius*radius + 2*x - y + 1;
		if(f >= 0) 
		{
		y--;
		ddF_y += 2;
		f += ddF_y;
		}
		x++;
		ddF_x += 2;
		f += ddF_x;    
		pokeP(x0 + x, y0 + y, color );
		pokeP(x0 - x, y0 + y, color );
		pokeP(x0 + x, y0 - y, color );
		pokeP(x0 - x, y0 - y, color );
		pokeP(x0 + y, y0 + x, color );
		pokeP(x0 - y, y0 + x, color );
		pokeP(x0 + y, y0 - x, color );
		pokeP(x0 - y, y0 - x, color );
		}
	}
	
	/**
	 * Writes a pixel to the screen. This method uses bounds checking to void writing outside of the buffer array. This allows you to safely
	 * call this method when drawing objects that may be partially (or fully) out of view
	 * @param x
	 * @param y
	 * @param color
	 */
	public void pokeP( int x, int y, int color )
	{
		if ( x >= 0 && y >= 0 && x<= width && y <= height )
		{
			int seg = x + ( this.width * y );
			iceBuffer[seg] = color;
		}
	}
	
	/**
	 * Polls the screen for a pixel and returns the color of a given coordinate. This method uses bounds checking as well to avoid going outside
	 * of the buffer. 	 
	 * @param x
	 * @param y
	 * @return
	 */
	public int peekP( int x, int y )
	{
		
		if ( x >= 0 && y >= 0 && x<= width && y <= height )
		{
			int seg = x + ( this.width * y );
			return iceBuffer[seg];
		}
		else
		{
			return 0;
		}
	}

	/**
	 * returns current palette
	 * @return
	 */
	public Palette getPalette()
	{
		return this.palette;
	}
}
