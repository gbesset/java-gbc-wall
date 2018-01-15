package com.gbcreation.wall.migration.tool.batch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.batch.item.ItemProcessor;

import com.gbcreation.wall.migration.tool.items.MigrationSourceItem;
import com.gbcreation.wall.migration.tool.items.SqlItem;
import com.gbcreation.wall.model.ItemType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemMigrationToolProcessor  implements ItemProcessor<MigrationSourceItem, SqlItem> {
	
	@Override
	public SqlItem process(MigrationSourceItem itemSource) throws Exception {
		log.info("ItemMigrationToolProcessor converting {}", itemSource.getId_file());
		
		SqlItem item = convertItem(itemSource);
		
		log.info("ItemMigrationToolProcessor {} converted into {}", itemSource, item);
		return item;
	}

	private SqlItem convertItem(MigrationSourceItem source) throws ParseException {
		
		source.populateFields();
		
		log.debug("Source : id [{}] file [{}] description [{}] tags [{}]", source.getId(), source.getFile(), source.getDescription(), source.getTags());
		log.debug("Source CLEAN: id [{}] file [{}] description [{}] tags [{}]", cleanData(source.getId()), cleanData(source.getFile()), cleanData(source.getDescription()), cleanData(source.getTags()));
		
		DateFormat formater = new SimpleDateFormat("YYY-MM-DD hh:mm:ss");
		
		SqlItem item = new SqlItem();
		item.setId(new Long(cleanData(source.getId())));
		item.setPath("/web/images/wall");
		item.setFile(cleanData(source.getFile()));
		item.setDescription(cleanData(source.getDescription()));
		item.setCreatedAt(formater.parse(cleanData(source.getCreationDate())));
		item.setRatio(Float.valueOf(cleanData(source.getRatio())));
		item.setReverseRatio(Float.valueOf(cleanData(source.getRatioReverse())));
		item.setNbLike(Integer.valueOf(cleanData(source.getNbLike())));
		item.setType(ItemType.valueOf(cleanData(source.getType()).toUpperCase()));
		
		log.debug("Item converted: id [{}] file [{}] description [{}] type [{}]", item.getId(), item.getFile(), item.getDescription(), item.getType());
		
		return item;
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
