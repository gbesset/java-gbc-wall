package com.gbcreation.wall.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gbcreation.wall.model.Item;

//This will be AUTO IMPLEMENTED by Spring into a Bean called itemRepository
public interface ItemRepository extends PagingAndSortingRepository<Item,Long>, JpaSpecificationExecutor{
	
	List<Item> findByFileOrderByCreatedAtDesc(String file);
	List<Item> findTop100ByFileContainingIgnoreCaseOrderByCreatedAtDesc(String file);
	
	//contains description <=> @Query("Select i from Item i where i.descriptio, like %:description%").
	//List<Item> findByDescriptionContaining(String description);
	
	
	//Pour info et voir rqt
	@Query("from Item it where it.file = ?1")
	@Deprecated
	List<Item> findByFileMyRqt(String file);

	
	Page findAll(Pageable pageable);
		
	// Renvoie un résultat paginé avec des méta-données sur la recherche (nombre de pages, etc.)
	List<Item> findByDescriptionContaining(String description, Pageable pageable);
}
