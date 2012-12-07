package org.ice.graphics.io_old;

/**
 * Sprite class for storing multiple images, image sequences and other data.
 * 
 * We may want to consider tying in the draw object with this class. 
 * 
 * @author home
 *
 */
public class Sprite {
  private int[] frames;              // Unique image frames.
  public int size;                   // Unique frame count;
  public int width;
  public int height;
  
  private Draw draw;
  private AnimationSequence[] animationSequence; 
  
  public Sprite( Draw draw )
  {	 
	  this.draw = draw;
  }
  
  public void drawSprite( int frame, int x, int y )
  {
	  draw.drawImage( frames[frame], x, y);
  }
  
  public void drawSpriteSequence( int id, int x, int y )
  {
	  drawSprite( animationSequence[id].render(), x, y );
  }
  
  /**
   * Disposes of a sprite object, and frees up any image handles that are being used by
   * the Draw framework. 
   */
  public void dispose()
  {
	  for ( int i = 0; i < frames.length; i++ )
	  {
		  draw.freeImage( frames[i] );
		  frames[i] = 0;
	  }
	  animationSequence = null;
  }
}

class AnimationSequence
{
	Frame[] frames;
	private int index;
	long timer;
	
	public AnimationSequence( int frameCount )
	{		
		frames = new Frame[frameCount];
		reset();
	}
	
	public void reset()
	{
		index = 0;
		timer = 0;
	}
	
	public int render()
	{
		int retVar = 0;
		if ( index == 0 && timer == 0 )
		{
			timer = System.currentTimeMillis() + frames[index].delay;
			retVar = frames[index].frame;			
		}
		else
		{
			if ( System.currentTimeMillis() >= timer )
			{
				if ( index+1 < frames.length )
				{
					index++;
					retVar = frames[index].frame;			
				}
			}
			else
			{
				retVar = frames[index].frame;
			}			
		}
		return retVar;
	}
	
	public void addFrame( int frame, int delay )
	{		
		frames[index] = new Frame( frame, delay );
		index++;
	}
	
	public class Frame
	{
		int frame;
		int delay;
		public Frame( int frame, int delay )
		{
			this.frame = frame;
			this.delay = delay;
		}
	}
}