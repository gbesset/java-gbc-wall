package com.gbcreation.wall.migration.tool.items;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class CommentHeaderFooterCallBack implements FlatFileHeaderCallback, FlatFileFooterCallback{
    
   private static final String EOF = System.getProperty("line.separator");
   
   private static final String OUTPUT_HEADER = "INSERT INTO demo_wall_comment(TODO.......";
   private static final String OUTPUT_FOOTER = EOF+EOF+"// Update Serial id...."+EOF+"setval('gbc_wall_comment_id_seq', 21, true)";
    
   @Override
   public void writeHeader(Writer writer) throws IOException {
       writer.write(OUTPUT_HEADER + EOF);      
   }

   @Override
   public void writeFooter(Writer writer) throws IOException {
       writer.write(OUTPUT_FOOTER);
   }

}
