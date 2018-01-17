package com.gbcreation.wall.repository;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;

public class ItemFilterSpecifications {

	 
	 public static Specification<Item> isItemPicture() {
		 return new Specification<Item>() {
			 @Override
			 public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				 return builder.equal(root.get("type"), ItemType.PICTURE);
			 }
		 };
	 }
	 public static Specification<Item> isItemVideo() {
		 return new Specification<Item>() {
			 @Override
			 public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				 return builder.or(builder.equal(root.get("type"), ItemType.VIDEO), builder.equal(root.get("type"), ItemType.VIDEO_YOUTUBE), builder.equal(root.get("type"), ItemType.VIDEO_VIMEO));
			 }
		 };
	 }
	 
	 public static Specification<Item> periodFromTo(Date from, Date to) {
		 return new Specification<Item>() {
			 @Override
			 public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				 return builder.between(root.get("createdAt"), from, to);
			 }
		 };
	 }
	 
	 public static Specification<Item> descriptionLike(String description) {
		 return new Specification<Item>() {
			 @Override
			 public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				 return builder.like(builder.lower(root.get("description")), "%"+description.toLowerCase()+"%");
			 }
		 };
	 }
}
