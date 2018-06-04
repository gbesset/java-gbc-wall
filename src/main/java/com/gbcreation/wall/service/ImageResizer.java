package com.gbcreation.wall.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageResizer {

	 
	 
	 public static void resize(BufferedImage inputImage, Path outputImagePath, int scaledWidth, int scaledHeight)
	            throws IOException {

		 int type = inputImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : inputImage.getType();
	        
	        // creates output image
	        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, type);
	 
	        // scales the input image to the output image
	        Graphics2D g2d = outputImage.createGraphics();
	        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
	        g2d.dispose();
	 
	        // extracts extension of output file
	        String formatName = outputImagePath.getFileName().toString().substring(outputImagePath.getFileName().toString()
	                .lastIndexOf(".") + 1);
	 
	        // writes to output file
	        log.info("Enregistrement{}",outputImagePath);
	        ImageIO.write(outputImage, formatName, outputImagePath.toFile());
	    }
	 
	 
	 public static BufferedImage rotate(BufferedImage inputImage, double angle) throws IOException {
	        // rotate image 
		    log.info("Rotation de l'image de "+angle);
		    BufferedImage newImage = new BufferedImage(inputImage.getHeight(), inputImage.getWidth(), inputImage.getType());
		    
	        Graphics2D g2d = newImage.createGraphics();
	        g2d.rotate(angle, newImage.getWidth()/2, newImage.getHeight()/2);
	        g2d.translate((newImage.getWidth() - inputImage.getWidth()) / 2, (newImage.getHeight() - inputImage.getHeight()) / 2);
	        g2d.drawImage(inputImage, 0, 0, inputImage.getWidth(), inputImage.getHeight(), null);
	        g2d.dispose();
	        
	        return newImage;
	    }

	 
	 public static void resize(BufferedImage inputImage, Path outputImagePath, double percent) throws IOException {
	        int scaledWidth = (int) (inputImage.getWidth() * percent);
	        int scaledHeight = (int) (inputImage.getHeight() * percent);
	        log.info("Image initiale en  {} x {}", inputImage.getWidth(), inputImage.getHeight());
	        log.info("Redimensionnement de l'image en {} x {}", scaledWidth, scaledHeight);
	        
	        resize(inputImage, outputImagePath, scaledWidth, scaledHeight);
	 }
	 
	 
	
	 
	
	 @Deprecated
	 public static void resize(InputStream inputStreamImage, Path outputImagePath, int scaledWidth, int scaledHeight)
	            throws IOException {
		 resize(ImageIO.read(inputStreamImage),outputImagePath, scaledWidth, scaledHeight);
		 
	  }

	 @Deprecated
	 public static void resize(InputStream inputStreamImage, Path outputImagePath, double percent) throws IOException {
	        BufferedImage inputImage = ImageIO.read(inputStreamImage);
	        int scaledWidth = (int) (inputImage.getWidth() * percent);
	        int scaledHeight = (int) (inputImage.getHeight() * percent);
	        log.info("Image initiale en  {} x {}", inputImage.getWidth(), inputImage.getHeight());
	        log.info("Redimensionnement de l'image en {} x {}", scaledWidth, scaledHeight);
	        
	        resize(inputStreamImage, outputImagePath, scaledWidth, scaledHeight);
	    }
	 
	 
}
