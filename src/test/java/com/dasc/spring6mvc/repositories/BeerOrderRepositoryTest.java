package com.dasc.spring6mvc.repositories;

import com.dasc.spring6mvc.entities.Beer;
import com.dasc.spring6mvc.entities.BeerOrder;
import com.dasc.spring6mvc.entities.BeerOrderShipment;
import com.dasc.spring6mvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerOrderRepositoryTest {

  @Autowired
  BeerOrderRepository beerOrderRepository;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  BeerRepository beerRepository;
  Customer testCustomer;
  Beer testBeer;

  @BeforeEach
  void setUp() {
    testCustomer = customerRepository.findAll().get(0);
    testBeer = beerRepository.findAll().get(0);
  }

  @Transactional
  @Test
  void testBeerOrders(){
    BeerOrder beerOrder = BeerOrder.builder()
        .customerRef("Test order")
        .customer(testCustomer)
        .beerOrderShipment(BeerOrderShipment.builder()
            .trackingNumber("1235r")
            .build())
        .build();

    BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

    System.out.println(savedBeerOrder.getCustomerRef());
  }
}