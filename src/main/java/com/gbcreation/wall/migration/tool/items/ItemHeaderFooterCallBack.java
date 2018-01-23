package com.gbcreation.wall.migration.tool.items;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class ItemHeaderFooterCallBack implements FlatFileHeaderCallback, FlatFileFooterCallback{
    
   private static final String EOF = System.getProperty("line.separator");
   
   private static final String OUTPUT_HEADER = "INSERT INTO demo_wall_item(id, file, path, description, date_creation, date_updated, ratio, nb_like, item_type) VALUES";	
   private static final String OUTPUT_FOOTER = EOF+EOF
		   										+"--// Change name of DB, delete , 1st line and replace , last line by ;"+EOF
		   										+"--// Update Serial id...."+EOF+"ALTER SEQUENCE gbc_wall_item_id_seq RESTART WITH 234;";
    
   @Override
   public void writeHeader(Writer writer) throws IOException {
       writer.write(OUTPUT_HEADER);      
   }

   @Override
   public void writeFooter(Writer writer) throws IOException {
       writer.write(OUTPUT_FOOTER);
   }

}
