import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyBoard extends JPanel implements ActionListener
{

	
	final int NUMBER_OF_BUTTONS = 16;
	int rows = (int) Math.sqrt(NUMBER_OF_BUTTONS);
    int columns = (int) Math.sqrt(NUMBER_OF_BUTTONS);
    
    public MyButton[][] myBoard;
   // public MyButton[][] realBoard;
    BufferedImage img;
    BufferedImage buttonImg;
    BufferedImage output;
    int holeR;
    int holeC;
    int moves;
    
    
    static Random random=new Random();
    
    LabelInterface lt;
    
    MyBoard(LabelInterface lt)
    {
    	this.lt=lt;
    	myBoard = new MyButton[rows][columns];
    	//realBoard = new MyButton[rows][columns];
    	holeR = rows -1;
    	holeC = columns -1;
    	moves = 0;
    	
    	GridLayout gridLayout = new GridLayout(rows,columns);
    	setLayout(gridLayout);
    	
    	for (int r = 0; r < rows;r++)
    	{
    		for(int c=0; c < columns ; c++)
    		{
    			myBoard[r][c]= new MyButton();
    		//	realBoard[r][c]= new MyButton();
    			add(myBoard[r][c]);
    			myBoard[r][c].setActionCommand("UPDATE");
    			myBoard[r][c].addActionListener(this);
    		}
    	}
    	
    	
    }
    public void doClickButton(int r ,int c)
    {
    	buttonImg = myBoard[r][c].realImg;
    	myBoard[holeR][holeC].swapImageWithAgain(buttonImg);
    	myBoard[r][c].makeAHole();
    	
    }
    
 /*   public void gameFinish() {
    	
    	String Icon1= myBoard[0][0].getIcon().toString();
    	String Icon2= realBoard[0][0].getIcon().toString();
    	
    	String Icon3= myBoard[0][1].getIcon().toString();
    	String Icon4= realBoard[0][1].getIcon().toString();
    	
    	
    	if(Icon1.equals(Icon2)) {
    		incorrect++;
    	}
    	else if ( Icon1.equals(null) || Icon2.equals(null) || Icon3.equals(null) || Icon4.equals(null))
    	{
    		
    	}
    	else if(Icon3.equals(Icon4)) {
    		incorrect++;
    	}
    	
    	
    	
    }*/
    
    public void shuffleBoard()
    {
    	int z=random.nextInt(4);
    	
    	if(z ==0 && holeC != 0)
    	{
    		doClickButton(holeR,holeC -1 );
    		holeC --;
    		
    	}
    	else if(z ==1 && holeR != 0)
    	{
    		doClickButton(holeR-1 ,holeC );
    		holeR --;
    		
    	}
    	else if(z ==2 && holeC != columns-1)
    	{
    		doClickButton(holeR,holeC +1 );
    		holeC ++;
    		
    	}
    	else if(z ==3 && holeR != rows-1)
    	{
    		doClickButton(holeR+1,holeC );
    		holeR ++;
    		
    	}
    	else 
    	{
    		
    	}
    	
    }
    
    public void createButtons(int r, int c)
    {
    	Dimension buttonSize = myBoard[1][1].getSize();
    	int w = buttonSize.width;
    	int h =buttonSize.height;
    	
    	int subimage_Width = (output.getWidth() /columns) *c;
        int subimage_Height = (output.getHeight() / rows )*r;
        
        buttonImg = output.getSubimage(subimage_Width, subimage_Height,w,h);
    	
    }
    
    public void loadImage(int height , int width) throws IOException
    {
    	JFileChooser        fc;
		File file = null;
		fc=new JFileChooser();	
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

		int selection =fc.showOpenDialog(this);
		if(selection == JFileChooser.APPROVE_OPTION)
		{
				
			file =fc.getSelectedFile();
		}
		
		else
		{
			JOptionPane.showMessageDialog(this, "Unable to load Image.", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
			try {
				img = ImageIO.read(file);
				
				
				
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
				return;
			}
			int h = img.getHeight();
			int w = img.getWidth();
			
			
			
			if (img == null)
			{
	
				JOptionPane.showMessageDialog(this,"Null Image",null,JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			

			if(w > h)
			{
				img = img.getSubimage((w-h)/2, 0, h, h);
			}
			else if(h > w)
			{
				img = img.getSubimage(0, (h-w)/2, w, w);
			}
			else 
			{
				img = img.getSubimage(0, 0, h, h);
			}
			
			Image imgscaled = img.getScaledInstance(width, height, img.SCALE_DEFAULT);
			output=new BufferedImage(imgscaled.getWidth(null),imgscaled.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
			output.getGraphics().drawImage(imgscaled, 0, 0, null);
			
			
			
			
			for(int r = 0; r < rows; r++)
			{
				for(int c = 0; c < columns; c++)
				{
					createButtons(r, c);
					myBoard[r][c].swapImageWith(buttonImg);
					//realBoard[r][c].swapImageWith(buttonImg);
					
					if(r == rows-1 && c == columns-1)
					{
						myBoard[r][c].makeAHole();
						//realBoard[r][c].makeAHole();
					}
				}
			}
			
			
			int difficulty = MyFrame.level_of_difficulty;
			
			for(int i = 0; i < (difficulty); i++)
				shuffleBoard();
	
    }
	
    
    public int numberofPiecesLeft()
	{
    	int incorrect=0;
    	for(int row = 0; row < rows; row++)
		{
    		for(int col = 0; col < columns; col++)
			{
				if(output != null)
				{
					if(!myBoard[row][col].isAdjacentTo())
					{
						incorrect ++;
					}
					if(row == rows-1 && col == columns-1 && holeC == columns-1 && holeR == rows-1)
					{
						incorrect --;
					}
				}
			}
		}
    	return incorrect;
		
	}
    
    public void pause()
	{
		
		
		for(int row = 0; row < rows; row++)
		{
			for(int col = 0; col < columns; col++)
			{
				myBoard[row][col].pause();
			}
		}
	}
    public void play()
	{
		for(int row = 0; row < rows; row++)
		{
			for(int col = 0; col < columns; col++)
			{
				myBoard[row][col].play();
			}
		}
		myBoard[holeR][holeC].makeAHole();
	
	}
		
	
	
	public void actionPerformed(ActionEvent e) 
	{	
		if(output != null)
		{
			for(int row = 0; row < rows; row++)
			{
				for(int col = 0; col < columns; col++)
				{
					if(e.getSource() == myBoard[row][col])
					{
						if((col-1) != (-1) && myBoard[row][col-1] == myBoard[holeR][holeC])
						{
							
							doClickButton(holeR, holeC+1);
							holeC++;
							lt.updateTime();
							lt.numberCorrectLabel();
							
						}
						else if((row-1) != (-1) && myBoard[row-1][col] == myBoard[holeR][holeC])
						{
							
							doClickButton(holeR+1, holeC);
							holeR++;
							
							lt.updateTime();

							lt.numberCorrectLabel();
						
						}
						else if((col+1) != columns && myBoard[row][col+1] == myBoard[holeR][holeC])
						{
							doClickButton(holeR, holeC-1);
							holeC--;
							lt.updateTime();

							
							lt.numberCorrectLabel();
							
						}
						else if((row+1) != rows && myBoard[row+1][col] == myBoard[holeR][holeC])
						{ 
							doClickButton(holeR-1, holeC);
							holeR--;
							lt.updateTime();

							
							lt.numberCorrectLabel();
							
						}
						else
						{
							
						}
						
					
					}
				}
			}
			
		int incorrect = numberofPiecesLeft();
		//System.out.println(incorrect);
		lt.numberOfMoves(incorrect);
		
		if(incorrect == 0)
		{
			JOptionPane.showMessageDialog(this, "Congratulations!", null, JOptionPane.INFORMATION_MESSAGE);
		}
			
		}
	}
}
	
	
	
	
	
	
	
	

