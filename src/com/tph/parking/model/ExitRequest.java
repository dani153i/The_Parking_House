package com.tph.parking.model;

public class ExitRequest extends ModelBase {

	private int receiptId;

	public int getReceiptId() { return receiptId; }
	public void setReceiptId(int receiptId) { this.receiptId = receiptId; }

	public ExitRequest(int receiptId) {
		setReceiptId(receiptId);
	}
}