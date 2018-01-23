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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	boolean isAppoved = true;
	

	@Column(name = "date_creation", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();
	
	@Column(name = "date_updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	@JsonIgnore
	private Item itemId;
	
	public Comment(String author, String comment, Item item) {
		super();
		this.author = author;
		this.comment = comment;
		this.itemId = item;
		createdAt = new Date();
	}
}
