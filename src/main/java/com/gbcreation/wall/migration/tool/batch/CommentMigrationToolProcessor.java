package com.gbcreation.wall.migration.tool.batch;

import org.springframework.batch.item.ItemProcessor;

import com.gbcreation.wall.migration.tool.items.MigrationSourceComment;
import com.gbcreation.wall.migration.tool.items.SqlComment;
import com.gbcreation.wall.migration.tool.items.SqlItem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentMigrationToolProcessor  implements ItemProcessor<MigrationSourceComment, SqlComment> {

	@Override
	public SqlComment process(MigrationSourceComment commentSource) throws Exception {
	//	log.info("CommentMigrationToolProcessor converting {}", commentSource.getFile());
		
	//	SqlComment comment = convertComment(commentSource);
		
	//	log.info("CommentMigrationToolProcessor {} converted into {}", commentSource, commentSource);
	//	return commentSource;
		
		return new SqlComment();
	}

}
