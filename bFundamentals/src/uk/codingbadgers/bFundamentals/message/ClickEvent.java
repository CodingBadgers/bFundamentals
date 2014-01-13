package uk.codingbadgers.bFundamentals.message;

public class ClickEvent implements Cloneable {

	private final ClickEventType action;
	private final String value;
	
	private int hashcode;
	
	public ClickEvent(ClickEventType type, String value) {
		this.action = type;
		this.value = value;
		
		generateHashCode();
	}
	
	public ClickEventType getAction() {
		return action;
	}
	
	public String getValue() {
		return value;
	}

	public void generateHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		hashcode = result;
	}
	
	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ClickEvent && hashCode() == obj.hashCode();
	}

	@Override
	public String toString() {
		return "ClickEvent [action=" + action + ", value=" + value + "]";
	}
	
	@Override
	public ClickEvent clone() {
		return new ClickEvent(this.action, this.value);
	}

}