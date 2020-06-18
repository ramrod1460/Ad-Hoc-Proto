package com.proto.solution;
import java.math.BigDecimal;

/**
 * Parse binary record data
 * 
 * 	Record
 *	1 byte record type enum | 4 byte (uint32) Unix timestamp | 8 byte (uint64) user ID 
 *	8 byte amount at end if debit or credit
 *
 * @author User
 *
 */
@SuppressWarnings({"PMD.CommentRequired","PMD.CommentSize","PMD.DataClass"})
public class RecordData {

	private byte typeOfRecord;
	private int timeStamp;
	private String usedId;
	private BigDecimal amount;
	
	public RecordData(final byte typeOfRecord, final int timeStamp, final String usedId, final BigDecimal amount) {
		super();
		this.typeOfRecord = typeOfRecord;
		this.timeStamp = timeStamp;
		this.usedId = usedId;
		this.amount = amount;
	}
	
	public byte getTypeOfRecord() {
		return typeOfRecord;
	}
	public void setTypeOfRecord(final byte typeOfRecord) {
		this.typeOfRecord = typeOfRecord;
	}
	public int getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(final int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUsedId() {
		return usedId;
	}

	public void setUsedId(final String usedId) {
		this.usedId = usedId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
