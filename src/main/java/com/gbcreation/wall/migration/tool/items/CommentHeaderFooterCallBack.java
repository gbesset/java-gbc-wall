package com.gbcreation.wall.migration.tool.items;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class CommentHeaderFooterCallBack implements FlatFileHeaderCallback, FlatFileFooterCallback{
    
   private static final String EOF = System.getProperty("line.separator");
   
   private static final String OUTPUT_HEADER = "INSERT INTO demo_wall_comment(id, item_id, author, is_appoved, date_creation, comment, date_updated) VALUES";
   private static final String OUTPUT_FOOTER = EOF+EOF
				+"--// Change name of DB, delete , 1st line and replace , last line by ;"+EOF
				+"--// Update Serial id...."+EOF+"ALTER SEQUENCE demo_wall_comment_id_seq RESTART WITH 5;";
    
   @Override
   public void writeHeader(Writer writer) throws IOException {
       writer.write(OUTPUT_HEADER);      
   }

   @Override
   public void writeFooter(Writer writer) throws IOException {
       writer.write(OUTPUT_FOOTER);
   }

}
