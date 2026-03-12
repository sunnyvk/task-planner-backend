package com.blogs.customexception;

@SuppressWarnings("serial")
public class CredentialException extends RuntimeException {

	public CredentialException(String msg)
	{
		super(msg);
	}
}
