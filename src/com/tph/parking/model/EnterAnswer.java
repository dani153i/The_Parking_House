package com.tph.parking.model;

public class EnterAnswer extends ModelBase {
	private int receiptId;
	private boolean full;
	private boolean access;

	public int getReceiptId() { return receiptId; }
	public void setReceiptId(int receiptId) { this.receiptId = receiptId; }

	public boolean isFull() { return full; }
	public void setFull(boolean full) { this.full = full; }

	public void setAccess(boolean access) { this.access = access; }
	public boolean isAccess() {
		return this.access;
	}


	public EnterAnswer(int receiptId, boolean full, boolean access) {
		setReceiptId(receiptId);
		setFull(full);
		setAccess(access);
	}
}