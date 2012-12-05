package org.baraxial.engine;

import java.awt.Rectangle;

import org.baraxial.engine.BaraxialEngineException.ExceptionType;
import org.ice.graphics.SpriteObject;
import org.ice.graphics.io.Draw;

public class Paddle extends SpriteObject {

	public int color = 0;
	
	public int strength = 100;		// 100 indicates that the paddle will not break when hit by the ball no matter
									// how many times the ball makes contact with the paddle.  This does not mean
									// the paddle is not susceptible to damage or modification from other objects,
									// such as drop objects, lasers, etc.
	
	public int min_x = 0;			// These 4 variables are used to control the physical x,y plane that the paddle
	public int min_y = 0;			// object can traverse and collisions should be detected if the paddle were to
	public int max_x = 0;			// touch or attempt to cross these values.
	public int max_y = 0;
	
	public PaddleSpeed speed = PaddleSpeed.Normal;			// Hope like hell you don't get the slow speed one when the ball speed is set to Very Fast lol.	
	public PaddleStatus status = PaddleStatus.Normal;		// No advantages or disadvantages.

	private Rectangle rectangle;                            // Used for collision detection.
	
	public enum PaddleStatus
	{
		Normal,
		Glass,
		Fat,
		Skinny,
		StickyPaddle,
		RocketsInstalled, 
		FastPaddle,
		SlowPaddle
	}
	
	public enum PaddleValidationStatus
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
	
	public enum PaddleSpeed
	{
		Normal,
		Slow,
		Fast
	}

	public void DrawPaddle(Draw draw)
	{
		//I think screen_x and screen_y need to go away and us keep current_ [x|y]. No need for both.
		//screen_x = current_x;
		//screen_y = current_y;
		
		if(current_x + width >= 639)		// might not need this if we setup mouse bounds in FullScreen2d.java
		{
			current_x = 639 - width;
		}
		
		// Update rectangle status
		this.rectangle.setLocation( current_x, current_y );
		
		draw.box(current_x, current_y, width, height, color, true);
  	  	draw.box(current_x + 1, current_y + 1, width - 2, height - 2, color + 8, false);
  	  	draw.box(current_x, current_y, width, height, color + 8, false);		
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
			// TODO:  Play sound to indicate the paddle is damaged but not destroyed.
		}		
	}

	public void Destroy()
	{
		// TODO: Implementation needed.
		// Should set all variables to their default values.
		// TODO: Play sound to indicate paddle is destroyed.
		// Reduce player ball count by one.
	}

	public Paddle()
	{
		// Do Nothing
	}
	
	// Paddle's switch(status) block statement was complaining about the missing Normal enum value missing
	// from the case statement list.  So I added this SuppressWarnings - incomplete-switch to shut it up.
	@SuppressWarnings("incomplete-switch")
	public Paddle(int current_x, int current_y, int width, int height, int color, int strength, PaddleSpeed speed, PaddleStatus status)
		throws Exception
	{
		this.current_x = current_x;
		this.current_y = current_y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.strength = strength;
		this.speed = speed;
		this.status = status;
	
		PaddleValidationStatus valid = Validate();
		
		SetMinMaxXY();		// Auto-Initialize the Min/Max XY values.
							// The developer can call the overloaded SetMinMaxXY(X, Y, X2, Y2) routine to fine tune values.
		
		if(valid != PaddleValidationStatus.Normal)
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
					
				case HeightLow:
					message = "Height to low.";
					break;
					
				case HeightHigh:
					message = "Height to high.";
					break;
					
				case StrengthLow:
					message = "Strength to low.";
					break;
					
				case StrengthHigh:
					message = "Strength to high.";
					
				break;
			}
			
			throw new BaraxialEngineException(ExceptionType.Paddle, message);
		}

		// Moved Rectangle so it is created AFTER the validation.
		rectangle = new Rectangle(current_x, current_y, width, height);		
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// The following three routines are paddle variable attribute validation routines.
	// Validate and then set if needed the min/max xy of the paddle.
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
	
	public PaddleValidationStatus Validate()
	{
		PaddleValidationStatus valid = PaddleValidationStatus.Normal;
		
		if(this.current_x < 0)
		{
			valid = PaddleValidationStatus.HorizontalCurrentPositionlow;
		}
			
		if(this.current_y < 0)
		{
			valid = PaddleValidationStatus.VerticalCurrentPositionLow;
		}
		
		if(this.width  < 10)
		{
			valid = PaddleValidationStatus.WidthLow;
		}
		
		if(this.height < 5)
		{
			valid = PaddleValidationStatus.HeightLow;
		}
		
		if(this.color < 0)
		{
			valid = PaddleValidationStatus.ColorValueLow;
		}
		
		if(this.strength < 0)
		{
			valid = PaddleValidationStatus.StrengthLow;
		}
		
		
		//////////////////////////////////////////////////////////
		
		int absoluteMaxScreenX = 639 - this.width;
		int absoluteMaxScreenY = 479 - this.height;
		int absoluteMaxWidth = 679 - this.current_x;
		int absoluteMaxHeight = 479 - this.current_y;
		
		if(this.current_x > absoluteMaxScreenX)
		{
			valid = PaddleValidationStatus.HorizontalCurrentPositonHigh;
		}
			
		if(this.current_y > absoluteMaxScreenY)
		{
			valid = PaddleValidationStatus.VerticalCurrentPositionHigh;
		}
		
		if(this.width > absoluteMaxWidth)
		{
			valid = PaddleValidationStatus.WidthHigh;
		}
		
		if(this.height > absoluteMaxHeight)
		{
			valid = PaddleValidationStatus.HeightHigh;
		}
		
		if(this.color > 255)
		{
			valid = PaddleValidationStatus.ColorValueHigh;
		}
		
		if(this.strength < 100)
		{
			valid = PaddleValidationStatus.StrengthHigh;
		}		
		
		return valid;
	}
	
}