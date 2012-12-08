package org.baraxial.engine;

import org.baraxial.engine.BaraxialEngineException.ExceptionType;
import org.ice.graphics.Graphics;
import org.ice.graphics.SpriteObject;

import java.awt.Rectangle;

public class Ball extends SpriteObject {

	public int color = 0;
	
	public int strength = 4;		// 4 indicates that the ball will not break when hit by the paddle no matter
									// how many times the ball makes contact with the paddle.  This does not mean
									// the ball is not susceptible to damage or modification from other objects,
									// such as drop objects, lasers, etc.
	
	public int min_x = 0;			// These 4 variables are used to control the physical x,y plane that the ball
	public int min_y = 0;			// object can traverse and collisions should be detected if the ball were to
	public int max_x = 0;			// touch or attempt to cross these values.
	public int max_y = 0;
	
	public int vx = 2;              // Velocity x and y. !TODO set these to 0 and let speed set them!
	public int vy = 3;              //
	
	public BallSpeed speed = BallSpeed.Normal;			// Hope like hell you don't get the slow speed paddle when the ball speed is set to Very Fast lol.	
	public BallStatus status = BallStatus.Normal;		// No advantages or disadvantages.
		
	public enum BallStatus
	{
		Normal,
		Glass,
		StickyBomb,
		FastBall,
		SlowBall,
		HyperBall
	}
	
	public enum BallValidationStatus
	{
		Normal,
		HorizontalCurrentPositionlow,
		VerticalCurrentPositionLow,
		HorizontalCurrentPositonHigh,
		VerticalCurrentPositionHigh,
		ColorValueLow,
		ColorValueHigh,
		WidthLow,
		WidthHigh,
		HeightLow,
		HeightHigh,
		StrengthLow,
		StrengthHigh		
	}
	
	public enum BallSpeed
	{
		Normal,
		Slow,
		Fast,
		Hyper
	}
	
	/**
	 * Ensures that the ball doesn't travel too fast.
	 */
	public void checkSpeed()
	{
		int maxX = 0;
		int maxY = 0;
		boolean isNegX = ( vx < 0 );
		boolean isNegY = ( vy < 0 );
		switch ( speed )
		{
		case Normal:
			maxX = 3;
			maxY = 2;
			break;
		case Slow:
			maxX = 2;
			maxY = 1;
			break;
		case Fast:
			maxX = 4;
			maxY = 3;
			break;
		case Hyper:
			maxX = 5;
			maxY = 4;
			break;
		}
		// Slow velocity (if needed)
		vx = Math.abs( vx );
		vy = Math.abs( vx );
		if ( vx > maxX )
		{
			vx = maxX;
			if ( isNegX )
				vx = -vx;
		}
		if ( vy > maxY )
		{
			vy = maxY;
			if ( isNegY )
				vy = -vy;
		}
	}
	

	public void DrawBall( Graphics graphics )
	{
		//I think screen_x and screen_y need to go away and us keep current_ [x|y]. No need for both.
		//screen_x = current_x;
		//screen_y = current_y;
/*		
		if(current_x + width >= 639)		// might not need this if we setup mouse bounds in FullScreen2d.java
		{
			current_x = 639 - width;
		}
*/		
		// Update ball here.
		//this.current_x += this.vx;
		//this.current_y += this.vy;
		
  	  this.current_x = Math.round( this.current_x + this.vx  );
  	  this.current_y = Math.round( this.current_y + this.vy  );
  	  // Wall Collision must come last!
      // !TODO move this elsewhere!
  	  if ( this.current_y + 1 > 470 )
  		  this.vy = -this.vy;
  	  else if ( this.current_y -1 < 1 )
  		  this.vy = -this.vy;

  	  if ( this.current_x + 1 > 600 )
  		  this.vx = -this.vx;
  	  else if ( this.current_x -1 < 1 )
  		  this.vx = -this.vx;
		
		
		// Update rectangle status
		this.rectangle.setLocation( current_x, current_y );
		
		graphics.circle(current_x, current_y, width, color, true);
		graphics.circle(current_x + 1, current_y + 1, width - 2, color + 8, false);
		graphics.circle(current_x, current_y, width, color + 8, false);
	}
		
	public void Hit(int damage)
	{
		this.strength = this.strength - damage;
		
		if(this.strength <= 0)
		{
			Destroy();
		}
		else
		{
			// TODO:  Play sound to indicate the ball is damaged but not destroyed.
		}		
	}

	public void Destroy()
	{
		// TODO: Implementation needed.
		// Should set all variables to their default values.
		// TODO: Play sound to indicate ball is destroyed.
		// Reduce player ball count by one.
	}

	public void handleCollision(SpriteObject collisionObject )
	{ 
		
		// Paddle physics.
		if ( collisionObject instanceof Paddle )
		{
			Paddle paddle = (Paddle)collisionObject;
			//Ball.angle = MathHelper.PiOver2 * ((Ball.position.X - midplayer) / 75);
			vx = (int)Math.round( vx + ( current_x - paddle.current_x ) * .08 );
			//iX:=iX+(X-PaddleCenterX)*0.1
			vy = -vy;
			return;
		}
		
		
		//graphics.box( collisionObject.current_x, collisionObject.current_y, collisionObject.width, collisionObject.height, 1, false );
		  if ( collisionObject.current_y >= this.current_y && collisionObject.current_x >= this.current_x )
		  {
			  if ( collisionObject.current_y - this.current_y > collisionObject.current_x - this.current_x && this.vx > 0 )
			  {
				  // This doesn't appear to do anything.
				  this.vy = -this.vy;    					  
			  }
			  else
			  {
				  this.vx = - this.vx;
			  }
			  
		  }
	    
		  // headed left
		  
		  else if ( collisionObject.current_y >= this.current_y && collisionObject.current_x <= this.current_x )
		  {
			  if ( collisionObject.current_y - this.current_y > collisionObject.current_x - this.current_x && this.vx > 0 )
			  {
				  this.vy = -this.vy;    					  
			  }
			  else
			  {
				  this.vx = - this.vx;
			  }
			  
		  }

		  else if ( collisionObject.current_y <= this.current_y && collisionObject.current_x <= this.current_x )
		  {
			  if ( collisionObject.current_y - this.current_y > collisionObject.current_x - this.current_x && this.vx < 0 )
			  {
				  this.vy = -this.vy;    					  
			  }
			  else
			  {
				  this.vx = - this.vx;
			  }
			  
		  }
		  

		  // headed right?
		  else if ( collisionObject.current_y <= this.current_y && collisionObject.current_x >= this.current_x )
	      {
			  if ( collisionObject.current_y - this.current_y > collisionObject.current_x - this.current_x && this.vx < 0 )
			  {
				  this.vy = -this.vy;    					  
			  }
			  else
			  {
				  this.vx = - this.vx;
			  }
			  return;
	      }

		  if ( collisionObject instanceof Brick )
		  {
			  Brick brick = (Brick)collisionObject;
			  brick.Hit(1);
		  }
		  
	}
	
	public Ball()
	{
		// Do Nothing
	}
	
	// Ball's switch(status) block statement was complaining about the missing Normal enum value missing
	// from the case statement list.  So I added this SuppressWarnings - incomplete-switch to shut it up.
	@SuppressWarnings("incomplete-switch")
	public Ball(int current_x, int current_y, int width, int height, int color, int strength, BallSpeed speed, BallStatus status)
		throws Exception
	{
		this.isActive = true;
		this.current_x = current_x;
		this.current_y = current_y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.strength = strength;
		this.speed = speed;
		this.status = status;
	
		BallValidationStatus valid = Validate();
		
		SetMinMaxXY();		// Auto-Initialize the Min/Max XY values.
							// The developer can call the overloaded SetMinMaxXY(X, Y, X2, Y2) routine to 
							// fine tune values.
		
		if(valid != BallValidationStatus.Normal)
		{
			String message = "";
			
			switch(valid)
			{
				case HorizontalCurrentPositionlow:
					message = "Horizontal current position to low.";
					break;
					
				case VerticalCurrentPositionLow:
					message = "Vertical current position to low.";
					break;
					
				case HorizontalCurrentPositonHigh:
					message = "Horizontal current position to high.";
					break;
					
				case VerticalCurrentPositionHigh:
					message = "Vertical current positioin to high.";
					break;
					
				case ColorValueLow:
					message = "Color value to low.";
					break;
					
				case ColorValueHigh:
					message = "Color value to high.";
					break;
					
				case WidthLow:
					message = "Width to low.";
					break;
					
				case WidthHigh:
					message = "Width to high.";
					break;
					
				case StrengthLow:
					message = "Strength to low.";
					break;
					
				case StrengthHigh:
					message = "Strength to high.";
					
				break;
			}
			
			throw new BaraxialEngineException(ExceptionType.Ball, message);
		}

		// Moved Rectangle so it is created AFTER the validation.
		// +1 to allocate for the black space between bricks.
		rectangle = new Rectangle(current_x, current_y, width+1, height+1);		
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// The following three routines are ball variable attribute validation routines.
	// Validate and then set if needed the min/max xy of the ball.
	public void SetMinMaxXY()
	{
		int absoluteMaxX = 639 - this.width;
		int absoluteMaxY = 479 - this.height;
		
		if(min_x <= 0 || min_x >= absoluteMaxX)
		{
			this.min_x = 0;
		}
		
		if(min_y <= 0 || min_y >= absoluteMaxY)
		{
			this.min_y = 0;
		}
		
		if(max_x <= 0 || max_x >= absoluteMaxX)
		{
			this.max_x = absoluteMaxX;
		}
		
		if(max_y <= 0 || max_y >= absoluteMaxY)
		{
			this.max_y = absoluteMaxY;
		}
	}
	
	// Validate and/or then set the min/max xy of the paddle.
	public void SetMinMaxXY(int min_x, int min_y, int max_x, int max_y)
	{
		int absoluteMaxX = 639 - this.width;
		int absoluteMaxY = 479 - this.height;
		
		if(min_x <= 0 || min_x >= absoluteMaxX)
		{
			this.min_x = 0;
		}
		else
		{
			this.min_x = min_x;
		}
					
		if(min_y <= 0 || min_y >= absoluteMaxY)
		{
			this.min_y = 0;
		}
		else
		{
			this.min_y = min_y;
		}
		
		if(max_x <= 0 || max_x >= absoluteMaxX)
		{
			this.max_x = absoluteMaxX;
		}
		else
		{
			this.max_x = max_x;
		}
		
		if(max_y <= 0 || max_y >= absoluteMaxY)
		{
			this.max_y = absoluteMaxY;
		}
		else
		{
			this.max_y = max_y;
		}
	}
	
	public BallValidationStatus Validate()
	{
		BallValidationStatus valid = BallValidationStatus.Normal;
		
		if(this.current_x < 0)
		{
			valid = BallValidationStatus.HorizontalCurrentPositionlow;
		}
			
		if(this.current_y < 0)
		{
			valid = BallValidationStatus.VerticalCurrentPositionLow;
		}
		
		if(this.width  < 4)
		{
			valid = BallValidationStatus.WidthLow;
		}
		
		if(this.color < 0)
		{
			valid = BallValidationStatus.ColorValueLow;
		}
		
		if(this.strength < 0)
		{
			valid = BallValidationStatus.StrengthLow;
		}
			
		//////////////////////////////////////////////////////////
		
		int absoluteMaxScreenX = 639 - this.width;
		int absoluteMaxScreenY = 479 - this.height;
		int absoluteMaxWidth = 679 - this.current_x;
		int absoluteMaxHeight = 479 - this.current_y;
		
		if(this.current_x > absoluteMaxScreenX)
		{
			valid = BallValidationStatus.HorizontalCurrentPositonHigh;
		}
			
		if(this.current_y > absoluteMaxScreenY)
		{
			valid = BallValidationStatus.VerticalCurrentPositionHigh;
		}
		
		if(this.width > absoluteMaxWidth)
		{
			valid = BallValidationStatus.WidthHigh;
		}
		
		if(this.color > 255)
		{
			valid = BallValidationStatus.ColorValueHigh;
		}
		
		if(this.strength < 100)
		{
			valid = BallValidationStatus.StrengthHigh;
		}		
		
		return valid;
	}
}