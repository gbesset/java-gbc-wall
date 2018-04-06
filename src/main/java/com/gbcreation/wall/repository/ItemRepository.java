package com.gbcreation.wall.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;

//This will be AUTO IMPLEMENTED by Spring into a Bean called itemRepository
public interface ItemRepository extends PagingAndSortingRepository<Item,Long>, JpaSpecificationExecutor{
	
	//Attention au by  findAll BY  order....
	Page<Item> findAllByOrderByCreatedAtDesc(Pageable pageable);
	Page<Item> findAllByTypeInOrderByCreatedAtDesc(List<ItemType> types, Pageable pageable);
	Page<Item> findByFileContainingIgnoreCaseOrderByCreatedAtDesc(String file, Pageable pageable);
	Page<Item> findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(String file, Pageable pageable);
		
	List<Item> findByFileOrderByCreatedAtDesc(String file);
	
	//contains description <=> @Query("Select i from Item i where i.descriptio, like %:description%").
	//List<Item> findByDescriptionContaining(String description);
	
	//Pour info et voir rqt
	//@Query("from Item it where it.file = ?1")
	//List<Item> findByFileMyRqt(String file);
}
