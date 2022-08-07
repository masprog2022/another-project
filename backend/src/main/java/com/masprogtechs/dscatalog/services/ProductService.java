package com.masprogtechs.dscatalog.services;

import java.util.Optional;
import java.util.stream.Collectors;

//import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masprogtechs.dscatalog.dto.ProductDTO;
import com.masprogtechs.dscatalog.entities.Product;
import com.masprogtechs.dscatalog.exceptions.DatabaseException;
import com.masprogtechs.dscatalog.exceptions.ResourceNotFoundException;
import com.masprogtechs.dscatalog.repositories.ProductRepository;

@Service // registrar a class como componente que vai participar do sistema de injeccao
			// de dependencia
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);

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

	@Transactional(readOnly = true)
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		//entity.setName(dto.getName());

		entity = repository.save(entity);

		return new ProductDTO(entity);

	}

	public ProductDTO update(ProductDTO dto) {
	
			Product entity = repository.findById(dto.getId()).
					orElseThrow(() -> new ResourceNotFoundException("Product does not exist!"));
			
			BeanUtils.copyProperties(dto, entity);

			entity = repository.save(entity);

			BeanUtils.copyProperties(entity, dto);

			return dto;
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
	

}
