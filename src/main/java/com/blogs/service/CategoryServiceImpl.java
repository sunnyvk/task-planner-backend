//package com.blogs.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.blogs.customexception.BlogsException;
//import com.blogs.dto.ApiResponce;
//import com.blogs.entities.Category;
//import com.blogs.reposotory.CategoryRepository;
//
//@Service //mandatory class level anno for sc that this is spring bean  B.L. 
//@Transactional // auto transactional management support
//public class CategoryServiceImpl implements CategoryService {
//
//	@Autowired //dependancy
//	private CategoryRepository categoryRepository;
//	
//	@Override
//	public List<Category> getAllCategory() {
//		
//		return categoryRepository.findAll();
//	}
//
//	@Override
//	public Category addNewCategory(Category category) {
//		// TODO Auto-generated method stub
//		return categoryRepository.save(category);
//	}
//
//	@Override
//	public ApiResponce deleteCategoryDetails(Long id) {
//		if(categoryRepository.existsById(id))
//		{
//		
//			categoryRepository.deleteById(id);
//			return new ApiResponce("success deleted");
//		}
//		return new ApiResponce("not deleted");
//	}
//
//	@Override
//	public Category getCategoryById(Long id) {
//		Optional<Category> optional = categoryRepository.findById(id);
//		return optional.orElseThrow(() -> new BlogsException("Resorce not found"));
//	}
//	
//	@Override
//	public Category updateCategoryDetails(Category category) {
//		
//		return categoryRepository.save(category);
//	}
//}
