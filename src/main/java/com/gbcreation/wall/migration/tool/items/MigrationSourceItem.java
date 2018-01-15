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
public class MigrationSourceItem {
	//From File
	private String id_file;
	private String description;
	private String creationDate_ratio_ratioReverse_nbLike_type;
	private String tags_end;
	
	//After split
	private String id;
	private String file;
	private String creationDate;
	private String ratio;
	private String ratioReverse;
	private String nbLike;
	private String type;
	private String tags;
	
	public void populateFields() {
		//recrée les champs correspondant au bon format 'un string' ou id,
		String[] split = this.id_file.split(", ");
		this.id = split[0];
		this.file = split[1]+"'";
		
		//remplace les \' par ''
		this.description = this.description.replace("\\'", "''");
		
		split = this.creationDate_ratio_ratioReverse_nbLike_type.split(", ");
		this.creationDate = "'"+split[0];
		this.ratio = split[1];
		this.ratioReverse = split[2];
		this.nbLike = split[3];
		this.type = split[4]+"'";
		
		
		split = this.tags_end.split("\\)");
		this.tags = split[0];
		if("'".equals(tags)){
			this.tags = "''";
		}
			
	}
}
