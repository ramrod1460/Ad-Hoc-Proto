package com.proto.solution.exception;

@SuppressWarnings({"PMD.CommentRequired"})
public class BadFileException extends Exception {

	private static final long serialVersionUID = 1L;
	 
	public BadFileException(final String errorMessage) {
		super(errorMessage);
	}

}
