package uk.codingbadgers.bFundamentals.message;

public class HoverEvent {

	private HoverEventType action;
	private String value;
	
	public HoverEvent(HoverEventType type, String value) {
		this.action = type;
		this.value = value;
	}
	
	public HoverEventType getAction() {
		return action;
	}
	
	public String getValue() {
		return value;
	}
}
