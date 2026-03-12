package com.blogs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blogs.entities.Railway;
import com.blogs.service.RailService;

@RestController
public class RailwayController {

	@Autowired
	RailService service;
	@GetMapping("/")
	public String home()
	{
		return "this is railway backend";
	}
	
	@GetMapping("/railway")
	public ResponseEntity< List<Railway>> getAll()
	{
		List<Railway> list = service.getAll();
		if(!list.isEmpty())
		{
			return new ResponseEntity<List<Railway>>(list, HttpStatus.OK);
		}
		return  new ResponseEntity<List<Railway>>(list, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/railway/{id}")
	public ResponseEntity<Railway> getById(@PathVariable Long id)
	{
		Railway rail = service.getById(id);
		if(rail != null)
		{
			return new ResponseEntity<Railway>(rail, HttpStatus.OK);
		}
		return  new ResponseEntity<Railway>(rail, HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/railway")
	public Railway update(@RequestBody Railway r)
	{
		return service.update( r);
	}
	
	@PostMapping("/railway")
	public String insert(@RequestBody Railway r)
	{
		
		return service.insert(r);
	}
	
	@DeleteMapping("/railway/{id}")
	public Railway delete(@PathVariable Long id)
	{
		
		return service.delete(id);
	}
}
