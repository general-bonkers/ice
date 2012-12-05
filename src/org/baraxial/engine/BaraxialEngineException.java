package org.baraxial.engine;

public class BaraxialEngineException extends Exception
{
	public enum ExceptionType
	{
		None,
		Ball,
		Brick,
		Paddle,
		PowerUp,
		Rocket,
		AlienCraft,
		Boss,
		Turret,
		Unexpected;
	}
	
	public ExceptionType ExceptionReason;
	
	public BaraxialEngineException(ExceptionType type, String message)
	{
		super(message);
		ExceptionReason = type;
	}
}
