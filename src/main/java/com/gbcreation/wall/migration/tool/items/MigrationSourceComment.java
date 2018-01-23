package com.gbcreation.wall.migration.tool.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MigrationSourceComment {
	
	//From File
	private String id_itemId_author;
	private String comment_isApproved_creationDate;
	private String update_eof;

	//After split
	private String id;
	private String itemId;
	private String author;
	private String comment;
	private String isApproved;
	private String creationDate;
	private String updateDate;
	
	public void populateFields() {
		//recrée les champs correspondant au bon format 'un string' ou id,
		String[] split = this.id_itemId_author.split(", ");
		this.id = split[0];
		this.itemId = split[1];
		this.author = split[2]+"'";
		if(this.author.equals("''")) {
			this.author = "UNKNOWN";
		}
		
		split = this.comment_isApproved_creationDate.split(", '");
		this.comment = split[0].substring(0, split[0].length()-4);
		this.isApproved = split[0].substring(split[0].length()-3, split[0].length());
		this.creationDate = split[1];
		
		//remplace les \' par ''  et \" par "
		this.comment = this.comment.replace("\\'", "''");
		this.author = this.author.replace("\\'", "''");
		this.comment = this.comment.replace("\\\"", "\"");
	}
}
