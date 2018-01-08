package com.gbcreation.wall.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gbc_wall_item")
@Getter
@Setter
@NoArgsConstructor
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String file;
	
	@Column(nullable = false)
	private String path;
	
	@Column(nullable = false)
	private String description;
	
	@Column(name = "date_creation", nullable = false, updatable = false)
	//@Temporal(TemporalType.TIMESTAMP) -> java.util.Date
	private Instant createdAt;
	
	@Column(name = "date_updaded")
	//@Temporal(TemporalType.TIMESTAMP) --> -> java.util.Date
	private Instant updatedAt;
	
	private Float ratio;
	
	private Float reverseRatio;  //A virer ?
	
	private Integer nbLike;
	
	private ItemType type;

	public Item(String file, String path, String description,ItemType type) {
		super();
		this.file = file;
		this.path = path;
		this.description = description;
		this.type = type;
		this.createdAt=Instant.now();
	}
	
	
}
