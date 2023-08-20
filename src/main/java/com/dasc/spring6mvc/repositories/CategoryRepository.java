package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
