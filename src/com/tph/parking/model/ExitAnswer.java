package com.tph.parking.model;

public class ExitAnswer extends ModelBase {

	private int receiptId;
	private boolean access;

	public int getReceiptId() { return receiptId; }
	public void setReceiptId(int receiptId) { this.receiptId = receiptId; }

	public boolean isAccess() { return access; }
	public void setAccess(boolean access) { this.access = access; }

	public ExitAnswer(int receiptId, boolean access) {
		setReceiptId(receiptId);
		setAccess(access);
	}
}