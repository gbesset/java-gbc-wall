package com.gbcreation.wall.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "gbc_wall_item")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true, nullable = false)
	private String file;
	
	@Column(nullable = false)
	private String path;
	
	@Column(nullable = false)
	private String description;
	
	@Column(name = "date_creation", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();;
	
	@Column(name = "date_updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	private Float ratio;
	
	private Float reverseRatio;  //A virer ?
	
	private Integer nbLike;
	
	@Column(name = "item_type")
	@Enumerated(EnumType.STRING)
	private ItemType type;
	
	//@JsonManagedReference is the forward part of reference – the one that gets serialized normally. 
	//mais Pb deserialisation sur admin/add-> OK.  Aultre alternative :  avec @jsonIgnore ds comment...
	@OneToMany(mappedBy="itemId")
	private Collection<Comment> comments;
	
	public Item(String file, String path, String description,ItemType type) {
		super();
		this.file = file;
		this.path = path;
		this.description = description;
		this.type = type;
	}
}
