package org.baraxial.engine;

import org.baraxial.engine.Ball.BallSpeed;
import org.baraxial.engine.Ball.BallStatus;
import org.baraxial.engine.Ball.BallValidationStatus;
import org.baraxial.engine.BaraxialEngineException.ExceptionType;
import org.ice.graphics.Graphics;
import org.ice.graphics.SpriteObject;

import java.awt.Rectangle;

public class Drop extends SpriteObject {
	
	public int color = 0;
	
	public int strength = 4;		// 4 indicates the item will not break if hit by the ball.
	
	public int vx = 2;              // Velocity x and y. !TODO set these to 0 and let speed set them!
	public int vy = 0;              // y is included for bizarr items but I doubt we will ever use it.
	

	public DropSpeed speed = DropSpeed.Normal;			// Hope like hell you don't get the slow speed paddle when the ball speed is set to Very Fast lol.	
	public DropStatus status = DropStatus.Normal;		// No advantages or disadvantages.
		
	public enum DropStatus
	{
		Normal,
		Glass,
		StickyBomb,
		FastBall,
		SlowBall,
		HyperBall
	}
	
	public enum DropValidationStatus
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
	
	public enum DropSpeed
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

	// TODO:  Right now the drop object is a ball, I'd rather it be a small rectangle with rounded edges.
	// Perhaps this can wait until we develop the images/gfx ourselfs with Gimp or some other tool.
	public void DrawDrop( Graphics graphics )
	{		
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
		// Paddle 
		if ( collisionObject instanceof Paddle )
		{
			Paddle paddle = (Paddle)collisionObject;

			return;
		}
	}
	
	public Drop()
	{
	}
	
	public Drop(int current_x, int current_y, int width, int height, int color, int strength, DropSpeed speed, DropStatus status)
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
	
		DropValidationStatus valid = Validate();		

		if(valid != DropValidationStatus.Normal)
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
			
			throw new BaraxialEngineException(ExceptionType.Drop, message);
		}

		// Moved Rectangle so it is created AFTER the validation.
		// +1 to allocate for the black space between bricks.
		rectangle = new Rectangle(current_x, current_y, width+1, height+1);		
	}

	public DropValidationStatus Validate()
	{
		DropValidationStatus valid = DropValidationStatus.Normal;
		
		if(this.current_x < 0)
		{
			valid = DropValidationStatus.HorizontalCurrentPositionlow;
		}
			
		if(this.current_y < 0)
		{
			valid = DropValidationStatus.VerticalCurrentPositionLow;
		}
		
		if(this.width  < 4)
		{
			valid = DropValidationStatus.WidthLow;
		}
		
		if(this.color < 0)
		{
			valid = DropValidationStatus.ColorValueLow;
		}
		
		if(this.strength < 0)
		{
			valid = DropValidationStatus.StrengthLow;
		}
			
		//////////////////////////////////////////////////////////
		
		int absoluteMaxScreenX = 639 - this.width;
		int absoluteMaxScreenY = 479 - this.height;
		int absoluteMaxWidth = 679 - this.current_x;
		int absoluteMaxHeight = 479 - this.current_y;
		
		if(this.current_x > absoluteMaxScreenX)
		{
			valid = DropValidationStatus.HorizontalCurrentPositonHigh;
		}
			
		if(this.current_y > absoluteMaxScreenY)
		{
			valid = DropValidationStatus.VerticalCurrentPositionHigh;
		}
		
		if(this.width > absoluteMaxWidth)
		{
			valid = DropValidationStatus.WidthHigh;
		}
		
		if(this.color > 255)
		{
			valid = DropValidationStatus.ColorValueHigh;
		}
		
		if(this.strength < 100)
		{
			valid = DropValidationStatus.StrengthHigh;
		}		
		
		return valid;
	}	
}
