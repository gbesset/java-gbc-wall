package com.gbcreation.wall.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "gbc_wall_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; 
	
	@Column(nullable=false)
	private String author;
	
	@Column(nullable=false, columnDefinition="TEXT")
	private String comment;
	
	@Column(nullable=false)
	boolean isApproved = true;
	

	@Column(name = "date_creation", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();
	
	@Column(name = "date_updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	

	//@JsonBackReference is the back part of reference – it will be omitted from serialization.
	//mais Pb deserialisation sur admin/add-> OK.  Aultre alternative :  avec @jsonIgnore ds comment...
	@NotNull
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item itemId;
	
	public Comment(String author, String comment, Item item) {
		super();
		this.author = author;
		this.comment = comment;
		this.itemId = item;
		createdAt = new Date();
	}
}
