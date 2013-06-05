package uk.codingbadgers.bFundamentals.config.deserialiser;

public interface ConfigMapper<T> {

	public T deserialise(Object mapping);
	
	public Object serialise(T object);
	
}
