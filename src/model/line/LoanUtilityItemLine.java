package model.line;

import java.time.LocalDateTime;

import model.item.UtilityItem;

public class LoanUtilityItemLine extends AbstractItemLine<UtilityItem> {
	private boolean lost; // Has the item been lost?
	public LoanUtilityItemLine(UtilityItem item) {
		super(item);
	}
	public LoanUtilityItemLine(int id, UtilityItem item, LocalDateTime datePickedUp, LocalDateTime dateReturned, boolean lost) {
		super(id, item, datePickedUp, dateReturned);
		this.lost = lost;
	}
	public boolean isLost() {
		return lost;
	}
	public void setLost(boolean lost) {
		this.lost = lost;
	}
}