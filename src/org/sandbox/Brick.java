package org.sandbox;

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

	private Boolean isStone = false;	// true = if ball hits this it will make the ball fragile.
	private Boolean isGlass = false;	// true = the brick will break as the ball goes straight through it without breaking.
	private Boolean isSteel = false;	// true = the ball will bounce off and this brick will never break.

	public enum Status
	{
		StatusNotCurrentlySet,
		HorizontalScreenPositionBelowZero,
		VertivalScreenPositionBelowZero,
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
			status = Status.VertivalScreenPositionBelowZero;
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
		
		// if strength is 0 then isGlass must be set to true.
		if(strength == 0 && isGlass == false)
		{
			status = Status.StrengthEqualsZeroAndIsGlassIsFalse;
		}
		
		// if strength is 10 then isStone or isSteel must be set to true.
		if((strength == 10 && isStone == false) || (strength == 10 && isSteel == false))
		{
			status = Status.StrengthEquals10AndEitherIsStoneOrIsSteelMustBeSetTo10;
		}
		
		Draw();
		
		return status;
	}
	
	public void Destroy()
	{
		// Implementation Needed.
		// Should set all variables to their default values and call Erase.
		Erase();
	}
	
	public void Draw()
	{
		// Implementation Needed.
		// Should draw the brick on the screen.
	}
	
	public void Erase()
	{
		// Implementation Needed.
		// Should erase the brick from the screen.
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

	public Boolean getIsStone() {
		return isStone;
	}

	public void setIsStone(Boolean isStone) {
		this.isStone = isStone;
	}

	public Boolean getIsGlass() {
		return isGlass;
	}

	public void setIsGlass(Boolean isGlass) {
		this.isGlass = isGlass;
	}

	public Boolean getIsSteel() {
		return isSteel;
	}

	public void setIsSteel(Boolean isSteel) {
		this.isSteel = isSteel;
	}
}
