package com.blogs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogs.customexception.RailwayException;
import com.blogs.entities.Railway;
import com.blogs.repository.RailwayRepository;

@Service
public class RailServiceImpl implements RailService {

	@Autowired
	private RailwayRepository railwayDao;
	
	@Override
	public List<Railway> getAll() {
	return	railwayDao.findAll();
		
	}

	@Override
	public Railway delete(Long id) {
		Optional<Railway> rail = railwayDao.findById(id);
		railwayDao.delete(rail.get());
		return rail.orElseThrow();
	}

	@Override
	public String insert(Railway railway) {
		railwayDao.save(railway);
		System.out.println(railway);
		return "data inserted";
	}

	@Override
	public Railway update(Railway r) {
		railwayDao.save(r);
		return railwayDao.save(r);
	}

	@Override
	public Railway getById(Long id) {
		Optional<Railway> rail= railwayDao.findById(id);
		return rail.orElseThrow( ()-> new RailwayException("Not Found"));
	}

}
