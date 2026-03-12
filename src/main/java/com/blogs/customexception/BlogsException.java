package com.blogs.customexception;

@SuppressWarnings("serial")
public class BlogsException extends RuntimeException {

	public BlogsException(String msg)
	{
		super(msg);
	}
}
