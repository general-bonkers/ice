package org.baraxial.engine;

public class Playa {

	private long score = 0;
	private int lives = 5;
	private String name = "";
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Get or Set the players current score.
	public long getScore() 
	{
		return score;
	}
	
	public void setScore(long score) 
	{
		this.score = score;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Get or Set the number of lives the player has left.
	public int getLives() 
	{
		return lives;
	}
	
	public void setLives(int lives)
	{
		if(lives > 99)
		{
			this.lives = lives;
		}
		else
		{
			this.lives = lives;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Get or Set the name of the Player.
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name)
	{
		if(name.length() > 40)
		{
			this.name = name.substring(0, 39);
		}
		else
		{
			this.name = name;
		}
	}
	
}
