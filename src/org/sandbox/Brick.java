package org.sandbox;

import org.sandbox.IceEngineException.ExceptionType;
import org.ice.graphics.io.Draw;
import java.awt.Frame;
import java.awt.image.BufferStrategy;

public class Brick {

	/*
	 * The Draw, Erase and Destroy brick routines need to be implemented.
	 */
		
	private int screen_x = 0;			// indicates the horizontal start of the left corner of the brick.
	private int screen_y = 0;			// indicates the vertical start of the left corner of the brick.
	private int height = 0;				// indicates the height of the brick.
	private int width = 0;				// indicates the width of the brick.
	private int color = 0;				// Must be a value in the existing palette. 
	private int strength = 1;			// 1 means the ball has to hit it just once to break this brick.  A value of 3 means the brick must be hit 3 times before this brick will break.
	private Type brickType = Type.Standard;		// Initial brick set to standard type.
	
	public enum Type
	{
		Standard,		// standard break out style brick.
		Stone,			// stone brick - will make your ball fragile and prone to breakage if hit with the ball.
		Glass,			// glass brick - the brick will break and the ball will continue on its original course.
		Steel;			// steel brick - this brick will never break no matter how many times it is hit by the ball.
	}
	
	public enum Status
	{
		StatusNotCurrentlySet,
		HorizontalScreenPositionBelowZero,
		VerticalScreenPositionBelowZero,
		ColorValueBelowZero,
		ColorValueAbove255,
		StrengthBelowZero,
		StrengthAbove10,
		StrengthEqualsZeroAndIsGlassIsFalse,
		StrengthEquals10AndEitherIsStoneOrIsSteelMustBeSetTo10;
	}
	
	// This function should ensure game rules are followed at all times
	// and prevent errors from occurring later due to improper brick setup.
	public Status Create()
	{
		Status status = Status.StatusNotCurrentlySet;
		
		if(screen_x < 0 )
		{
			status = Status.HorizontalScreenPositionBelowZero;
		}
		
		if(screen_y < 0 )
		{
			status = Status.VerticalScreenPositionBelowZero;
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
	
	public void Destroy()
	{
		// Implementation Needed.
		// Should set all variables to their default values and call Erase.
	}
	
	public void DrawBrick( Draw draw )
	{
		
		draw.box(screen_x, screen_y, width, height, color, true);
  	  	draw.box(screen_x + 1, screen_y + 1, width - 2, height - 2, color + 8, false);
  	  	draw.box(screen_x, screen_y, width, height, color + 8, false);
	}
	
	public void Hit()
	{
		strength = strength - 1;
		
		if(strength <= 0)
		{
			Destroy();
		}
	}
	
	public Brick()
	{
		// do nothing for now.
	}
	
	public Brick(int screen_x, int screen_y, int width, int height, int color, int strength, Type brickType) throws Exception
	{
		this.screen_x = screen_x;
		this.screen_y = screen_y;
		this.height = height;
		this.width = width;
		this.color = color; 
		this.strength = strength;
		this.brickType = brickType;	
		
		Status status = Create();
		
		if(status != Status.StatusNotCurrentlySet)
		{
			String message = "";
			
		    switch(status) 
			{
		    	case HorizontalScreenPositionBelowZero:
		    		message = "Horizontal screen position below zero.";
		    		break;
		    		
		    	case VerticalScreenPositionBelowZero:
		    		message = "Vertical screen position below zero.";
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
		    
			throw new IceEngineException(ExceptionType.brick, message);
		}
	}
	
	public int getScreen_x() {
		return screen_x;
	}

	public void setScreen_x(int screen_x) {
		this.screen_x = screen_x;
	}

	public int getScreen_y() {
		return screen_y;
	}

	public void setScreen_y(int screen_y) {
		this.screen_y = screen_y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}
}
