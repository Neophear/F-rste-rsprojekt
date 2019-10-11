package model.line;

import java.time.LocalDateTime;

public abstract class AbstractItemLine<T> {
	private int id;
	private T item;
	private LocalDateTime datePickedUp,
						  dateReturned;
	
	public AbstractItemLine(T item) {
		this.item = item;
	}
	
	public AbstractItemLine(int id, T item, LocalDateTime datePickedUp, LocalDateTime dateReturned) {
		this.id = id;
		this.item = item;
		this.datePickedUp = datePickedUp;
		this.dateReturned = dateReturned;
	}

	// Getters
	public int getId() {
		return id;
	}
	public T getItem() {
		return item;
	}
	public LocalDateTime getDatePickedUp() {
		return datePickedUp;
	}
	public LocalDateTime getDateReturned() {
		return dateReturned;
	}

	// Setters
	public void setId(int id) { // DO NOT TOUCH
		this.id = id;
	}
	
	public void setItem(T item) {
		this.item = item;
	}
	
	public void setDatePickedUp(LocalDateTime datePickedUp) {
		if (datePickedUp == null && this.dateReturned != null)
			throw new IllegalStateException("While returned date has been set, picked up date can't be removed");
		
		this.datePickedUp = datePickedUp;
	}
	public void setDateReturned(LocalDateTime dateReturned) {
		if (this.datePickedUp == null)
			throw new IllegalStateException("Picked up date has not been set");
		
		this.dateReturned = dateReturned;
	}
}
