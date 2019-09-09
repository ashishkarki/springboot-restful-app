package com.karki.ashish.app.ui.model.response;

import java.util.Date;

/**
 * Just a POJO to represent Custom error messages
 * 
 * @author Ashish Karki
 *
 */
public class CustomErrorMessage {

	private Date timestamp;
	private String message;

	public CustomErrorMessage() {
	}

	public CustomErrorMessage(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
