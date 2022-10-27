package com.masprogtechs.dscatalog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import com.masprogtechs.dscatalog.entities.Category;
import com.masprogtechs.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Override
	public Optional<Product> findById(Long id);


}
