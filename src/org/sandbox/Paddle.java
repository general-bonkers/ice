package org.sandbox;

import org.sandbox.Brick.Status;
import org.sandbox.IceEngineException.ExceptionType;
import org.ice.graphics.io.Draw;

public class Paddle {

	public int screen_x = 0;		// starting x,y of the paddle after level start, or ball drop.
	public int screen_y = 0;
	public int height = 0;
	public int width = 0;
	public int color = 0;
	
	public int strength = 100;		// 100 indicates that the paddle will not break when hit by the ball no matter
									// how many times the ball makes contact with the paddle.  This does not mean
									// the paddle is not susceptible to damage or modification from other objects,
									// such as drop objects, lasers, etc.
	
	public int min_x = 0;			// These 4 variables are used to control the physical x,y plane that the paddle
	public int min_y = 0;			// object can traverse and collisions should be detected if the paddle were to
	public int max_x = 0;			// touch or attempt to cross these values.
	public int max_y = 0;
	
	public int current_x = 0;		// The current x, y location of the upper left corner of the paddle.
	public int current_y = 0;		// Useful for collision detection.
	
	public PaddleSpeed speed = PaddleSpeed.Normal;			// Hope like hell you don't get the slow speed one when the ball speed is set to Very Fast lol.	
	public PaddleStatus status = PaddleStatus.Normal;		// No advantages or disadvantages.
			
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
		HorizontalScreenPositionlow,
		VerticalScreenPositionLow,
		HorizontalScreenPositonHigh,
		VerticalScreenPositionHigh,
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
		
		if(this.screen_x < 0)
		{
			valid = PaddleValidationStatus.HorizontalScreenPositionlow;
		}
			
		if(this.screen_y < 0)
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
		int absoluteMaxWidth = 679 - this.screen_x;
		int absoluteMaxHeight = 479 - this.screen_y;
		
		if(this.screen_x > absoluteMaxScreenX)
		{
			valid = PaddleValidationStatus.HorizontalScreenPositonHigh;
		}
			
		if(this.screen_y > absoluteMaxScreenY)
		{
			valid = PaddleValidationStatus.VerticalScreenPositionHigh;
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
	
	public void DrawPaddle(Draw draw)
	{
		//I think screen_x and screen_y need to go away and us keep current_ [x|y]. No need for both.
		//screen_x = current_x;
		//screen_y = current_y;
		
		if(screen_x + width >= 639)		// might not need this if we setup mouse bounds in FullScreen2d.java
		{
			screen_x = 639 - width;
		}
		
		draw.box(screen_x, screen_y, width, height, color, true);
  	  	draw.box(screen_x + 1, screen_y + 1, width - 2, height - 2, color + 8, false);
  	  	draw.box(screen_x, screen_y, width, height, color + 8, false);		
	}
	
	public void Destroy()
	{
		// Implementation needed.
		// Should set all variables to their default values.
	}
	
	public void Hit(int damage)
	{
		this.strength = this.strength - damage;
		
		if(this.strength <= 0)
		{
			Destroy();
		}
		
	}
	
	public Paddle()
	{
		// Do Nothing
	}
	
	public Paddle(int screen_x, int screen_y, int width, int height, int color, int strength, PaddleSpeed speed, PaddleStatus status)
		throws Exception
	{
		this.screen_x = screen_x;
		this.screen_y = screen_y;
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
				case HorizontalScreenPositionlow:
					message = "Horizontal screen position to low.";
					break;
					
				case VerticalScreenPositionLow:
					message = "Vertical screen position to low.";
					break;
					
				case HorizontalScreenPositonHigh:
					message = "Horizontal screen position to high.";
					break;
					
				case VerticalScreenPositionHigh:
					message = "Vertical screen position to high.";
					break;
					
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
			
			throw new IceEngineException(ExceptionType.paddle, message);
		}
		
	}
}
