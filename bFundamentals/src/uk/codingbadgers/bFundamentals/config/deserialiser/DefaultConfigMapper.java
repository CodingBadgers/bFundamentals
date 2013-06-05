package uk.codingbadgers.bFundamentals.config.deserialiser;

public class DefaultConfigMapper implements ConfigMapper<Object> {

	@Override
	public Object deserialise(Object mapping) {
		return mapping;
	}

	@Override
	public Object serialise(Object object) {
		return object;
	}

}
