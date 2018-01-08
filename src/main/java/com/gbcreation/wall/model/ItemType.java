package com.gbcreation.wall.model;

public enum ItemType {

	PICTURE ("P"), VIDEO ("V"), VIDEO_YOUTUBE ("YT"), VIDEO_VIMEO ("VM");
	
	private String type_abreviation ;  
    
    private ItemType(String abreviation) {  
        this.type_abreviation = abreviation ;  
   }  
     
    public String getShortType() {  
        return  this.type_abreviation ;  
   }  
 }
