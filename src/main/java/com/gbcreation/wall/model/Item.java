package com.gbcreation.wall.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private Integer id;
	
	private String file;
	
	private String path;
	
	private String description;
	
	//@Column(name = "firstname")
	private Instant date;
	
	private Float ratio;
	
	private Float reverseRatio;  //A virer ?
	
	private Integer nbLike;
	
	private String type;

	public Item(String file, String path, String description) {
		super();
		this.file = file;
		this.path = path;
		this.description = description;
	}
	
	
}
