package com.gbcreation.wall.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Test;

public class ImageResizerTest {
	
	  private String inputImagePath = "src/test/resources/img/Duke.png";
	  private String outputImagePath1 = "src/test/resources/img/DukeFixed.png";
	  private String outputImagePath2 = "src/test/resources/img/Duke_smaller.png";
	  private String outputImagePath3 = "src/test/resources/img/Duke_bigger.png";
      
	@Test
	public void testResizeImage() throws FileNotFoundException, IOException {
 
        BufferedImage originalImage = ImageIO.read(new File(inputImagePath));
        
        // resize to a fixed width (not proportional)
        int scaledWidth = 1024;
        int scaledHeight = 768;
        ImageResizer.resize(originalImage, Paths.get(outputImagePath1), scaledWidth, scaledHeight);
 
        // resize smaller by 50%
        double percent = 0.5;
        ImageResizer.resize(originalImage, Paths.get(outputImagePath2), percent);
 
        // resize bigger by 50%
        percent = 1.5;
        ImageResizer.resize(originalImage, Paths.get(outputImagePath3), percent);
 
    }
	
	@After
	public void clean() {
		new File(outputImagePath1).delete();
		new File(outputImagePath2).delete();
		new File(outputImagePath3).delete();
	}
}
