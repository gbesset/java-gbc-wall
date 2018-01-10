package com.gbcreation.wall.util;

import java.util.ArrayList;
import java.util.List;


public class WallUtils {
	
	public static <T> List<T> convertIterableToList(Iterable<T> iterable){
		List<T> converted = new ArrayList<T>();
		iterable.forEach(converted::add);
		return converted;
	}
}
