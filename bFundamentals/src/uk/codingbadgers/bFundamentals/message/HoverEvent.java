package uk.codingbadgers.bFundamentals.message;

import org.bukkit.Achievement;
import org.bukkit.inventory.ItemStack;

public class HoverEvent implements Cloneable {

	private final HoverEventType action;
	private final Object value;
	
	public HoverEvent(HoverEventType type, Object value) {
		if (type == HoverEventType.SHOW_TOOLTIP && !(value instanceof String)) {
			throw new IllegalArgumentException("Value for a tooltip must be a string");
		} else if (type == HoverEventType.SHOW_ITEM && !(value instanceof ItemStack)) {
			throw new IllegalArgumentException("Value for a item tooltip must be a item");
		} else if (type == HoverEventType.SHOW_ACHIEVEMENT && !(value instanceof Achievement)) {
			throw new IllegalArgumentException("Value for a achievement tooltip must be a achievement");
		}
		
		this.action = type;
		this.value = value;
	}
	
	public HoverEventType getAction() {
		return action;
	}
	
	public Object getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
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
	public HoverEvent clone() {
		return new HoverEvent(this.action, this.value);
	}

}
