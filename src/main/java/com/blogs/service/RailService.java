package com.blogs.service;

import java.util.List;

import com.blogs.entities.Railway;

public interface RailService {

	public List<Railway> getAll();
	public Railway delete(Long id);
	public String insert(Railway railway);
	public Railway update(Railway r);
	public Railway getById(Long id);
}
