package com.gbcreation.wall.service;

import java.util.Date;
import java.util.List;

import com.gbcreation.wall.model.Item;

public interface IWallService {
	Iterable<Item> retrieveAllItems();
	List<WallItem> retrieveAllPictures();
	List<WallItem> retrieveAllVideos();
	List<WallItem> retrieveAllFromTo(Date from, Date to) ;
}
