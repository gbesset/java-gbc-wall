package com.gbcreation.wall.migration.tool.items;

import java.sql.Timestamp;
import java.util.Date;

import com.gbcreation.wall.model.Comment;
import com.gbcreation.wall.model.Item;

public class SqlComment extends Comment{

	@Override
	public String toString() {
		return new String (getId()+", "+sql(getItemId())+", "+sql(getAuthor())+", "+isAppoved()+", "+sql(getCreatedAt())+", "+sql(getComment())+", "+sql(getUpdatedAt()));
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
	private String sql(Item field) {
		
		return "'"+field.getId().toString()+"'";
		
	}
}
