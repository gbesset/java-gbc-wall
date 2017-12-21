package com.gbcreation.wall.repository;

import org.springframework.data.repository.CrudRepository;

import com.gbcreation.wall.model.Item;

//This will be AUTO IMPLEMENTED by Spring into a Bean called itemRepository
public interface ItemRepository extends CrudRepository<Item,Long>{

}
