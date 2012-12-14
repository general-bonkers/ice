package org.baraxial.engine;

import java.util.Vector;

import org.baraxial.engine.BaraxialEngineException.ExceptionType;

public class Level {

	public String levelName = "";
	
	public Vector<Brick> bricks;
	public Vector<Ball> balls;
	public Vector<Doodad> doodads;
	public Vector<Drop> drops;
	public Vector<Paddle> paddles;
	public Vector<Playa> players;
	
	public Level()
	{
	}
	
	public Level(String levelName)
	{
		bricks = new Vector<Brick>();
		balls = new Vector<Ball>();
		
	}
	
}
