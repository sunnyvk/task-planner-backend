package com.blogs.dto;

import java.time.LocalDate;

public class ApiResponce {
	private String message;
	private LocalDate timeStamp;
	
	public ApiResponce(String message)
	{
		this.message=message;
		this.timeStamp=LocalDate.now();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDate getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDate timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
