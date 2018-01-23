package com.gbcreation.wall.migration.tool.batch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.batch.item.ItemProcessor;

import com.gbcreation.wall.migration.tool.items.MigrationSourceComment;
import com.gbcreation.wall.migration.tool.items.MigrationSourceItem;
import com.gbcreation.wall.migration.tool.items.SqlComment;
import com.gbcreation.wall.migration.tool.items.SqlItem;
import com.gbcreation.wall.model.Item;
import com.gbcreation.wall.model.ItemType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentMigrationToolProcessor  implements ItemProcessor<MigrationSourceComment, SqlComment> {

	@Override
	public SqlComment process(MigrationSourceComment commentSource) throws Exception {
		log.info("CommentMigrationToolProcessor converting {}", commentSource.getId_itemId_author());
		
		SqlComment comment = convertComment(commentSource);
		
		log.info("CommentMigrationToolProcessor {} converted into {}", commentSource, comment);
		return comment;
		
	}
	
	private SqlComment convertComment(MigrationSourceComment source) throws ParseException {
		
		source.populateFields();
		
		log.debug("Source : id [{}] itemId [{}] author [{}] creationDate [{}] comment [{}]", source.getId(), source.getItemId(), source.getAuthor(), source.getCreationDate(),source.getComment());
		log.debug("Source CLEAN: iid [{}] itemId [{}] author [{}] creationDate [{}] comment [{}]", cleanData(source.getId()), cleanData(source.getItemId()), cleanData(source.getAuthor()), cleanData(source.getCreationDate()), cleanData(source.getComment()));
		
		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Item item = new Item();
		item.setId(new Long(cleanData(source.getItemId())));
		
		SqlComment comment = new SqlComment();
		comment.setId(new Long(cleanData(source.getId())));
		comment.setItemId(item);
		comment.setAuthor(cleanData(source.getAuthor()));
		comment.setComment(cleanData(source.getComment()));
		comment.setCreatedAt(formater.parse(cleanData(source.getCreationDate())));
		comment.setAppoved((Boolean.valueOf(cleanData(source.getIsApproved()))));
		
		log.debug("comment converted: id [{}] author [{}] comment [{}] creationDate [{}]", comment.getId(), comment.getAuthor(), comment.getComment(), comment.getCreatedAt());
		
		return comment;
	}

	private String cleanData(String field) {
		String cleanFile = field;

		if(cleanFile.equals("''") || cleanFile.equals("''),") || cleanFile.equals("'');")) {
			return null;
		}

		if(cleanFile.startsWith("(")) {
			cleanFile = cleanFile.substring(1);
		}

		if	(cleanFile.startsWith("'") && cleanFile.endsWith("'")) {
			cleanFile = cleanFile.substring(1, cleanFile.length()-1);
		}
		
		return cleanFile;
	}
}