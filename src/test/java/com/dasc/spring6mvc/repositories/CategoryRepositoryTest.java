package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CategoryRepositoryTest {

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  BeerRepository beerRepository;
  Beer testBeer;

  @BeforeEach
  void setUp() {
    testBeer = beerRepository.findAll().get(0);
  }

  @Transactional
  @Test
  void testAddCategory() {
    Category savedCategory = categoryRepository.save(Category.builder()
        .description("Ales")
        .build());

    testBeer.addCategory(savedCategory);
    Beer saveBeer = beerRepository.save(testBeer);

    System.out.println(saveBeer.getBeerName());
  }
}