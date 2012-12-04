package org.sandbox;

public class IceEngineException extends Exception
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
	
	public IceEngineException(ExceptionType type, String message)
	{
		super(message);
		ExceptionReason = type;
	}
}
