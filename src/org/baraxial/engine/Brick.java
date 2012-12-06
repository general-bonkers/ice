package org.baraxial.engine;

import org.baraxial.engine.BaraxialEngineException.ExceptionType;
import org.ice.graphics.CollisionHandler;
import org.ice.graphics.SpriteObject;
import org.ice.graphics.io.Draw;
import java.awt.Rectangle;

public class Brick extends SpriteObject {

	/*
	 * The Destroy brick routine need to be implemented.
	 */
		
	public int color = 0;				// Must be a value in the existing palette. 
	public int strength = 1;			// 1 means the ball has to hit it just once to break this brick.  A value of 3 means the brick must be hit 3 times before this brick will break.
	
	public Type brickType = Type.Normal;		// Initial brick set to normal type.
		
	public enum Type
	{
		Normal,		    // normal break out style brick.
		Stone,			// stone brick - will make your ball fragile and prone to breakage if hit with the ball.
		Glass,			// glass brick - the brick will break and the ball will continue on its original course.
		Steel;			// steel brick - this brick will never break no matter how many times it is hit by the ball.
	}
	
	public enum Status
	{
		Normal,
		HorizontalCurrentPositionBelowZero,
		VerticalCurrentPositionBelowZero,
		HorizontalCurrentPositionHigh,
		VerticalCurrentPositionBeHigh,
		ColorValueBelowZero,
		ColorValueAbove255,
		StrengthBelowZero,
		StrengthAbove10,
		StrengthEqualsZeroAndIsGlassIsFalse,
		StrengthEquals10AndEitherIsStoneOrIsSteelMustBeSetTo10;
	}
	
	public void DrawBrick(Draw draw)
	{		
		draw.box(current_x, current_y, width, height, color, true);
  	  	draw.box(current_x + 1, current_y + 1, width - 2, height - 2, color + 8, false);
  	  	draw.box(current_x, current_y, width, height, color + 8, false);
	}
	
	public int Hit(int damage)
	{
		strength = strength - damage;
		
		return strength;
	}
	
	public void Destroy()
	{
		// TODO: Implementation Needed.
		// Should set all variables to their default values and call Erase.
		// TODO: Play sound to indicate block is destroyed.
	}

	public void handleCollision(SpriteObject spriteObject)
	{		
		//if(spriteObject instanceof Brick)
		//{
		//	if(Hit(1) <= 0)
		//	{
		//		CollisionHandler.removeSpriteObject(spriteObject);
		//	}
		//}		

		// do something here.
	}
	
	public Brick()
	{
		// do nothing for now.		
	}
	
	// Brick's switch(status) block statement was complaining about the missing Normal enum value missing
	// from the case statement list.  So I added this SuppressWarnings - incomplete-switch to shut it up.
	@SuppressWarnings("incomplete-switch")
	public Brick(int current_x, int current_y, int width, int height, int color, int strength, Type brickType) 
			throws Exception
	{
		this.isActive = false;
		this.current_x = current_x;
		this.current_y = current_y;
		this.height = height;
		this.width = width;
		this.color = color; 
		this.strength = strength;
		this.brickType = brickType;	
					
		Status status = Validate();
		
		if(status != Status.Normal)
		{
			String message = "";
			
		    switch(status) 
			{
		    	case HorizontalCurrentPositionBelowZero:
		    		message = "Horizontal screen position below zero.";
		    		break;
		    		
		    	case VerticalCurrentPositionBelowZero:
		    		message = "Vertical screen position below zero.";
		    		break;

		    	case HorizontalCurrentPositionHigh:
		    		message = "Horizontal screen position to high.";
		    		break;
		    		
		    	case VerticalCurrentPositionBeHigh:
		    		message = "Vertical screen position to high.";
		    		break;
		    		
		    	case ColorValueBelowZero:
		    		message = "Color value below zero.";
		    		break;
		    		
		    	case ColorValueAbove255:
		    		message = "Color value above 255.";
		    		break;
		    		
		    	case StrengthBelowZero:
		    		message = "Strength below zero.";
		    		break;
		    		
		    	case StrengthAbove10:
		    		message = "Strength above 10.";
		    		break;
		    		
		    	case StrengthEqualsZeroAndIsGlassIsFalse:
		    		message = "Strength equals zero and glass brick is not selected.";
		    		break;
		    		
		    	case StrengthEquals10AndEitherIsStoneOrIsSteelMustBeSetTo10:
		    		message = "Strength equals 10 and either stone or steel are not selected.";
		    		
		    	break;
			}
		    
			throw new BaraxialEngineException(ExceptionType.Brick, message);
		}
		// Moved Rectangle so it is created AFTER the validation.
		this.rectangle = new Rectangle(current_x, current_y, width, height);
	}
	
	// This function should ensure game rules are followed at all times
	// and prevent errors from occurring later due to improper brick setup.
	public Status Validate()
	{
		Status status = Status.Normal;
		
		if(current_x < 0 )
		{
			status = Status.HorizontalCurrentPositionBelowZero;
		}
		
		if(current_y < 0 )
		{
			status = Status.VerticalCurrentPositionBelowZero;
		}

		if(current_x > 639 - width)
		{
			status = Status.HorizontalCurrentPositionHigh;
		}
		
		if(current_y > 479 - height)
		{
			status = Status.VerticalCurrentPositionBeHigh;
		}
		
		if(color < 0)
		{
			status = Status.ColorValueBelowZero;
		}
		
		if(color > 255)
		{
			status = Status.ColorValueAbove255;
		}
		
		if(strength < 0)
		{
			status = Status.StrengthBelowZero;
		}
		
		if(strength > 10)
		{
			status = Status.StrengthAbove10;
		}		
		
		return status;
	}	
}