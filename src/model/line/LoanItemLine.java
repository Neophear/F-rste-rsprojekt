package model.line;

import java.time.LocalDateTime;

import model.item.Item;

public class LoanItemLine extends AbstractItemLine<Item> {
	public LoanItemLine(Item item) {
		super(item);
	}
	public LoanItemLine(int id, Item item, LocalDateTime datePickedUp, LocalDateTime dateReturned) {
		super(id, item, datePickedUp, dateReturned);
	}
}