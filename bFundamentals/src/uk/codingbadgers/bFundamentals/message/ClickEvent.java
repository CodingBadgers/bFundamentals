package uk.codingbadgers.bFundamentals.message;

public class ClickEvent {

	private ClickEventType action;
	private String value;
	
	public ClickEvent(ClickEventType type, String value) {
		this.action = type;
		this.value = value;
	}
	
	public ClickEventType getAction() {
		return action;
	}
	
	public String getValue() {
		return value;
	}
}