package org.baraxial.engine;

public class BaraxialEngineException extends Exception
{
	public enum ExceptionType
	{
		none,
		ball,
		brick,
		paddle,
		unexpected;
	}
	
	public ExceptionType ExceptionReason;
	
	public BaraxialEngineException(ExceptionType type, String message)
	{
		super(message);
		ExceptionReason = type;
	}
}
