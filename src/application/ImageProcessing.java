package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ImageProcessing {	
	private MainScreenController con;

	ImageProcessing(MainScreenController con) {
		this.con = con;
	}
	
	//Debug function which we used for getting RGB info of circles
	//Not used right now
	private void saveRGB(Image image)
	{
		BufferedWriter writer;
		//Logging
		try {
		    writer = new BufferedWriter(
                    new FileWriter("debug.txt", true)  //Set true for append mode
                ); 
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
		int baseHeight = 580;
		int heightTracker = 580;
		int baseWidth = 1421;
		while(heightTracker >234)
		{
			PixelReader pReader = image.getPixelReader();
			Color color = pReader.getColor(baseWidth,heightTracker);
			
			int red1 = (int) (color.getRed()*255);
			int red2 = (int) (color.getBlue()*255);
			int red3 = (int) (color.getGreen()*255);
			
			
			try {
				writer.write(String.valueOf(red1)+"\t"+String.valueOf(red2)+"\t"+String.valueOf(red3)+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			heightTracker--;
		}
		
		
		try {
			writer.write("\n\n\n\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Calculate circle height
	private int GetCircleHeight(int heightTracker,Image image, PixelReader pReader, int baseWidth)
	{
		int count =0;
		
		//While circle condition is true add circle height. Then return
		while(true) {
			heightTracker--;
			count++;
			Color color = pReader.getColor(baseWidth,heightTracker);
			Color nextColor = pReader.getColor(baseWidth,heightTracker-1);
			
			double blue = color.getBlue()*255 + nextColor.getBlue()*255;
			if(blue < 388)
			{
				return count;
			}
		}
	}
	
	public double GetOreDistance(Image image, boolean isWindowed)
	{
		//16:9
		//Calculating basic pixel ratio of start point on radar
		double ratioX = 2843.0/3840.0;
		double ratioY = 1163.0/2160.0;
		
		double imgHeight;
		int baseHeight,baseWidth,heightTracker;
		
		//Setting up img height, and finding start point of radar
		if(isWindowed)
		{
			//Top bar is 30px and there is 1px on top and bottom of game
			imgHeight = image.getHeight()-32;
			baseHeight = (int)(ratioY * imgHeight) +31; //31 because we dont care about 1px on bottom
			//There is 1px border from the left side
			baseWidth = (int)Math.round(ratioX * (image.getWidth()-2))+1; //1 because we dont care about 1px on the right side
		}
		else
		{
			imgHeight = image.getHeight();
			baseHeight = (int)(ratioY * imgHeight);
			baseWidth = (int)Math.round(ratioX * image.getWidth());
		}
		heightTracker = baseHeight;
		
		//Checking all pixel on Y axis on radar
		while(heightTracker >1)
		{	
			PixelReader pReader = image.getPixelReader();
			
			//Get color of two pixels
			Color color = pReader.getColor(baseWidth,heightTracker);
			Color nextColor = pReader.getColor(baseWidth,heightTracker-1);
			
			//Get blue part of two colors
			double totalBlue = color.getBlue()*255 + nextColor.getBlue()*255;
			
			//We found out that all circles satisty this condition. So this is true when circle is detected.
			if(totalBlue > 388)
			{
				//Calculate circle height.
				int circleHeight = GetCircleHeight(heightTracker,image,pReader,baseWidth);
				
				// >3 because we want to skip accidental errors. Circle has to be at least 3px 
				if(circleHeight >3) {		
					//We want the middle point of circle
					heightTracker -= circleHeight/2;
					break;
				}
			}
			
			heightTracker--;
		}
		
		//Final height from start point
		int finalHeight = baseHeight - heightTracker;
		
		//Convert from px to ingame ore distance
		double finalAmount = 0;
		double stepper = 172.0/2160.0 * imgHeight;
		finalAmount = (100.0/stepper) * finalHeight;
		
		//This means that circle is right at the bottom. Usually error because this shouldnt happen.
		if(finalAmount < 10)
		{
			con.showAlertBox("ERROR!","Scanner lines not found! Check instructions!");
			finalAmount = 0;
		}
		
		//return ingame distance
		return finalAmount;		
	}
		
	
}
