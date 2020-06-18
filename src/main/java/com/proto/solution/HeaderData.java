package com.proto.solution;

@SuppressWarnings({"PMD.CommentRequired","PMD.CommentSize","PMD.SystemPrintln","PMD.DataClass"})
public class HeaderData {

	private String magic;
	private byte version;
	private int recordCount;

	
	public HeaderData(final String magic, final byte version, final int recordCount) {
		super();
		this.magic = magic;
		this.version = version;
		this.recordCount = recordCount;
	}
	
	public String getMagic() {
		return magic;
	}
	public void setMagic(final String magic) {
		this.magic = magic;
	}
	public byte getVersion() {
		return version;
	}
	public void setVersion(final byte version) {
		this.version = version;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(final int recordCount) {
		this.recordCount = recordCount;
	}
	
	
}
