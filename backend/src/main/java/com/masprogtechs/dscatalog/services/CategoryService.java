package com.masprogtechs.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masprogtechs.dscatalog.dto.CategoryDTO;
import com.masprogtechs.dscatalog.entities.Category;
import com.masprogtechs.dscatalog.exceptions.DatabaseException;
import com.masprogtechs.dscatalog.exceptions.ResourceNotFoundException;
import com.masprogtechs.dscatalog.repositories.CategoryRepository;

@Service // registrar a class como componente que vai participar do sistema de injeccao
			// de dependencia
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		Page<Category> list = repository.findAll(pageable);

		return list.map(x -> new CategoryDTO(x));

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
		// Category entity = obj.get();
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Category does not exist!"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());

		entity = repository.save(entity);

		return new CategoryDTO(entity);

	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {

			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);

		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID " + id + "nao encontrado");
		}
	}

	/*
	public CategoryDTO update(CategoryDTO dto) {

			Category entity = repository.findById(dto.getId()).
					orElseThrow(() -> new ResourceNotFoundException("Category does not exist!"));

			BeanUtils.copyProperties(dto, entity);

			entity = repository.save(entity);

			BeanUtils.copyProperties(entity, dto);

			return dto;
	}*/

	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found! " + id);
	    }catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integtation Vailation");
		}

	}


}
