package com.masprogtechs.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masprogtechs.dscatalog.dto.CategoryDTO;
import com.masprogtechs.dscatalog.entities.Category;
import com.masprogtechs.dscatalog.exceptions.EntityNotFoundException;
import com.masprogtechs.dscatalog.repositories.CategoryRepository;

@Service // registrar a class como componente que vai participar do sistema de injeccao
			// de dependencia
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();

		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

		/*
		 * List<CategoryDTO> listDTO = new ArrayList<>();
		 * 
		 * for(Category cat: list) { listDTO.add(new CategoryDTO(cat)); }
		 */
		// return listDTO;
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id); 
		//Category entity = obj.get();
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found!"));
		return new CategoryDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		
		entity = repository.save(entity);
		
		return new CategoryDTO(entity);
		
		
	}
	
	

	

}
