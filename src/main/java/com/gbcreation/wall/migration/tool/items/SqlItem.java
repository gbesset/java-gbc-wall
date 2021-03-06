package com.gbcreation.wall.migration.tool.items;

import java.sql.Timestamp;
import java.util.Date;

import com.gbcreation.wall.model.Item;

public class SqlItem extends Item{

	@Override
	public String toString() {
		return new String (getId()+", "+sql(getFile())+", "+sql(getPath())+", "+sql(getDescription())+", "+sql(getCreatedAt())+", "+sql(getUpdatedAt())+", "+getRatio()+", "+getNbLike()+", "+sql(getType().name()));
	}
	
	private String sql(String field) {
		if(field == null || field.equalsIgnoreCase("null") || field.length()==0) {
			return "null";
		}
		else {
			return "'"+field+"'";
		}
	}
	private String sql(Date field) {
		if(field == null) {
			return "null";
		}
		else {
			return "'"+new Timestamp(field.getTime())+"'";
		}
	}
}
