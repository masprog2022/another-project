package com.masprogtechs.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masprogtechs.dscatalog.entities.Category;
import com.masprogtechs.dscatalog.repositories.CategoryRepository;

@Service // registrar a class como componente que vai participar do sistema de injeccao de dependencia
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;

	public List<Category> findAll(){
		return repository.findAll();
	}
	
}
