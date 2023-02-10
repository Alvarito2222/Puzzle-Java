import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MyButton extends JButton {
	
	
	
	BufferedImage realImg;
	int 		   hole=0;
    BufferedImage     img;
    
  
	
	public void makeAHole() 
	{
		setIcon(null);
		hole=1;
	}
	
	public boolean isAdjacentTo()
	{
		for(int z=0; z<img.getHeight() ;z++)
        {
			 for(int i=0;i < img.getWidth() ; i++) 
        	{
        		if(img.getRGB(i, z) != realImg.getRGB(i, z))
        		{
        			return false;
        		}
        		if(hole == 1)
        		{
        			return false;
        		}
        		else {
        			
        		}
        		
        	}
        }
		
		return true;
	}
	
	public void swapImageWith(BufferedImage otherImg) 
	{
		this.img = otherImg;
		this.realImg = otherImg;
		setIcon(new ImageIcon(img));
		hole= 0;
		
	}
	
	public void swapImageWithAgain(BufferedImage otherImg)
	{
		this.realImg = otherImg;
		setIcon(null);
		setIcon(new ImageIcon(realImg));
		hole = 0;
		
	}
	
	public void pause()
	{
		setIcon(new ImageIcon(img));
	}
	public void play()
	{
		setIcon(new ImageIcon(realImg));
	}

}
