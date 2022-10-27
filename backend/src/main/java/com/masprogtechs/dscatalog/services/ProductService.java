package com.masprogtechs.dscatalog.services;

import java.util.Optional;
//import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masprogtechs.dscatalog.dto.CategoryDTO;
import com.masprogtechs.dscatalog.dto.ProductDTO;
import com.masprogtechs.dscatalog.entities.Category;
import com.masprogtechs.dscatalog.entities.Product;
import com.masprogtechs.dscatalog.exceptions.DatabaseException;
import com.masprogtechs.dscatalog.exceptions.ResourceNotFoundException;
import com.masprogtechs.dscatalog.repositories.CategoryRepository;
import com.masprogtechs.dscatalog.repositories.ProductRepository;

@Service // registrar a class como componente que vai participar do sistema de injeccao
			// de dependencia
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);

		return list.map(x -> new ProductDTO(x));

		/*
		 * List<ProductDTO> listDTO = new ArrayList<>();
		 *
		 * for(Product cat: list) { listDTO.add(new ProductDTO(cat)); }
		 */
		// return listDTO;
	}

	/*
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		// Product entity = obj.get();
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Product does not exist!"));
		return new ProductDTO(entity);
	}

	*/

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		// Product entity = obj.get();
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Product does not exist!"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	/*
	public ProductDTO update(ProductDTO dto) {

			Product entity = repository.findById(dto.getId()).
					orElseThrow(() -> new ResourceNotFoundException("Product does not exist!"));

			BeanUtils.copyProperties(dto, entity);

			entity = repository.save(entity);

			BeanUtils.copyProperties(entity, dto);

			return dto;
	}*/

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID " + id + " não encontrado.");
		}
	}

	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found! " + id);
	    }catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integtation Vailation");
		}

	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());

		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}


}
