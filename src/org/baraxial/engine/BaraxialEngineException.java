package org.baraxial.engine;

public class BaraxialEngineException extends Exception
{
	// Added this to suppress a compiler warning.
	private static final long serialVersionUID = -1254066344165572922L;

	public enum ExceptionType
	{
		None,
		Ball,
		Brick,
		Paddle,
		Drop,
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
