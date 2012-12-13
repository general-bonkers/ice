package org.baraxial.engine;

import org.baraxial.engine.BaraxialEngineException.ExceptionType;
import org.ice.graphics.Graphics;
import org.ice.graphics.SpriteObject;

import java.awt.Rectangle;

public class Doodad extends SpriteObject {

	public int color = 0;
	
	public int strength = 4;		// 4 indicates that the ball will not break when hit by the paddle no matter
									// how many times the ball makes contact with the paddle.  This does not mean
									// the ball is not susceptible to damage or modification from other objects,
									// such as drop objects, lasers, etc.
	
	public int min_x = 0;			// These 4 variables are used to control the physical x,y plane that the ball
	public int min_y = 0;			// object can traverse and collisions should be detected if the ball were to
	public int max_x = 0;			// touch or attempt to cross these values.
	public int max_y = 0;
	
	public int vx = 3;              // Velocity x and y. !TODO set these to 0 and let speed set them!
	public int vy = 2;              //
	public int vstep = 10;           //
	public int vclock = 0;
	
	public DoodadSpeed speed = DoodadSpeed.Normal;			// Hope like hell you don't get the slow speed paddle when the ball speed is set to Very Fast lol.	
	public DoodadStatus status = DoodadStatus.Normal;		// No advantages or disadvantages.
		
	public enum DoodadStatus
	{
		Normal,
		Dead,
	}
	
	public enum DoodadValidationStatus
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
	
	public enum DoodadSpeed
	{
		Normal,
		Slow,
		Fast,
		Hyper
	}
	

	public void DrawDoodad( Graphics graphics )
	{
		graphics.box( current_x, current_y, 10, 5, 2,  true);
		//graphics.circle(current_x, current_y, width, color, true);
		//graphics.circle(current_x + 1, current_y + 1, width - 2, color + 8, false);
		//graphics.circle(current_x, current_y, width, color + 8, false);		
	}
	
	public void updateDooDad()
	{
		vclock++;
		if ( vclock >= vstep )
		{
			vclock = 0;
			int change = (int)( Math.random() * 10 ) + 1;
			
			if ( change == 4 )
			{
				change = (int)( Math.random() * 3 );
				switch(change)
				{
					case 0:
						vx = -vx;
						vy = -vy;
						break;
					case 1:
						vx = vx;
						vy = -vy;
						break;
					case 2:
						vx = -vx;
						vy = vy;
						break;
				}
			}
			
			  if ( vx == 0 )
			  {
				  vx = getBallEntropy();
			  }
			
		  /* ---------------------------- */	
			
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
			
			// debug collision area
			//graphics.box( this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height, color, true );
	//		checkSpeed();
		}
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
		int oldVX = vx;
		int oldVY = vy;
		
		// Paddle physics.
		if ( collisionObject instanceof Paddle )
		{
			Paddle paddle = (Paddle)collisionObject;
			//Ball.angle = MathHelper.PiOver2 * ((Ball.position.X - midplayer) / 75);
			// Numbers get a bit nuts here. If you hit
//			vx = (int)Math.round( ( current_x - ( paddle.current_x + ( paddle.width / 2 ) ) ) * .08 );
			vx = (int)Math.round( ( current_x - ( paddle.current_x + paddle.current_x ) ) * .08 );
			
			// sorta better, but values are wayy off
			//vx = (int)Math.round( current_x - ( ( paddle.current_x + ( paddle.width / 2 ) ) ) );
			
			//!TODO shoddy math.. fix this!
			
			// invert !SHODDY FIX
			//if ( oldVX > 0 )
			//	vx = -vx;
			
			//!TODO don't invert VY if you bump the side.
			
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
				  this.vx = - this.vx ;
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
				  this.vx = - this.vx ;
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

		  /*
		   * Only little guy doodad needs to do this.
		  if ( collisionObject instanceof Brick )
		  {
			  Brick brick = (Brick)collisionObject;
			  brick.Hit(1);
		  }
		  */
		  
		  if ( vx == oldVX && vy == oldVY )
		  {
			  System.out.println ("Failed to reverse direction of ball!!" );
		  }
		  
	}
	public int getBallEntropy()
	{
		  int tmp = (int)( Math.random() + 1 ) * 2;
		  return ( Math.random() > 0 ) ? tmp : -tmp;
	}

	public int getBallEntropy( int velocity )
	{
		//!TODO FIX!! (this isn't working right now)
		//  int tmp = (int)( Math.random())  ;
		//  return ( velocity > 0 ) ? tmp : -tmp;
		return 0;
	}

	public Doodad()
	{
		// Do Nothing
	}
	
	// Ball's switch(status) block statement was complaining about the missing Normal enum value missing
	// from the case statement list.  So I added this SuppressWarnings - incomplete-switch to shut it up.
	@SuppressWarnings("incomplete-switch")
	public Doodad(int current_x, int current_y, int color, DoodadSpeed speed, DoodadStatus status)
		throws Exception
	{
		this.isActive = true;
		this.current_x = current_x;
		this.current_y = current_y;
		this.width = 20;
		this.height = 10;
		this.color = color;
		this.speed = speed;
		this.status = status;
	
		DoodadValidationStatus valid = Validate();
		
		SetMinMaxXY();		// Auto-Initialize the Min/Max XY values.
							// The developer can call the overloaded SetMinMaxXY(X, Y, X2, Y2) routine to 
							// fine tune values.
		
		if(valid != DoodadValidationStatus.Normal)
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
	
	public DoodadValidationStatus Validate()
	{
		DoodadValidationStatus valid = DoodadValidationStatus.Normal;
		
		if(this.current_x < 0)
		{
			valid = DoodadValidationStatus.HorizontalCurrentPositionlow;
		}
			
		if(this.current_y < 0)
		{
			valid = DoodadValidationStatus.VerticalCurrentPositionLow;
		}
		
		if(this.width  < 4)
		{
			valid = DoodadValidationStatus.WidthLow;
		}
		
		if(this.color < 0)
		{
			valid = DoodadValidationStatus.ColorValueLow;
		}
		
		//////////////////////////////////////////////////////////
		
		int absoluteMaxScreenX = 639 - this.width;
		int absoluteMaxScreenY = 479 - this.height;
		int absoluteMaxWidth = 679 - this.current_x;
		int absoluteMaxHeight = 479 - this.current_y;
		
		if(this.current_x > absoluteMaxScreenX)
		{
			valid = DoodadValidationStatus.HorizontalCurrentPositonHigh;
		}
			
		if(this.current_y > absoluteMaxScreenY)
		{
			valid = DoodadValidationStatus.VerticalCurrentPositionHigh;
		}
		
		if(this.width > absoluteMaxWidth)
		{
			valid = DoodadValidationStatus.WidthHigh;
		}
		
		if(this.color > 255)
		{
			valid = DoodadValidationStatus.ColorValueHigh;
		}
				
		return valid;
	}
}