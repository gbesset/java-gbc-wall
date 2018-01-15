package com.gbcreation.wall.migration.tool.batch;

import org.springframework.batch.item.file.transform.LineAggregator;

public class SqlLineAggregator <T> implements LineAggregator<T> {
 
    @Override
    public String aggregate(T item) {
	    		String insert = "(";
	    		insert+=item.toString();
	    		insert += ")";
	      return insert;
	      
    		
    }
}
