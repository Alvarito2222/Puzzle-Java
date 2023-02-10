import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;




public class MainClass {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Starting application...");
		
		new MyFrame();				// Creates a new instance of the class 
	
		System.out.println("Done.");
	}
}
class MyFrame extends JFrame 
				implements ActionListener , Runnable ,LabelInterface
{
	Toolkit				                   tk;
	Dimension							    d;
	
	
	MyBoard 						  myBoard;
	
	JPanel  						mainPanel;
	JPanel   					 buttonsPanel;
	JPanel    					  fieldsPanel;
	
	JLabel         				   timerLabel;
	JLabel     					 counterLabel;
	JLabel		 numberofpiecesIncorrectLabel;
	JLabel  					  timerTField;
	JLabel 						counterTField;
	JTextField  numberofpiecesIncorrectTField;
	
	public static int level_of_difficulty =0;
	
	JButton            exitB;
	JButton        newImageB;
	JButton    	 play_pauseB;
	Thread 				hilo;
    boolean 	 activeTimer;
    boolean 		   pause;
    boolean      init = true;
    Boolean       load=false;
    Integer min =0 , sec=0 , mil=0 ;
    String[] difficulty={"Easy","Intermediate","Difficult"};
  
    
    int  moves = 0;
	int   left = 0;
    int    height ;
	int     width ;
 

	
	MyFrame() throws IOException{
		
		
		
		
		exitB =new JButton("Exit");
		exitB.setActionCommand("Exit");
		exitB.addActionListener(this);
		exitB.setToolTipText("Exit the Program");
		
		newImageB =new JButton("New Image");
		newImageB.setActionCommand("Load");
		newImageB.addActionListener(this);
		
		play_pauseB =new JButton("Pause/Play");
		play_pauseB.setActionCommand("Play/Pause");
		play_pauseB.addActionListener(this);
		
		
      		
   		myBoard = new MyBoard(this);
      	
      	
      	buttonsPanel = new JPanel(new FlowLayout());
      	
      	fieldsPanel= new JPanel();
      	
      	timerLabel=new JLabel("Timer");
      	timerTField=new JLabel("00:00:000");
      	timerTField.setFont(new Font(Font.SERIF,Font.BOLD,20 ));
      	//timerTField.setForeground( Color.BLUE );
      	//timerTField.setBackground( Color.WHITE );
      	timerTField.setOpaque( true );
      	
		
      	counterLabel=new JLabel("Number of moves: ");
      	counterTField=new JLabel();
      	
      	
		
      	numberofpiecesIncorrectLabel=new JLabel("Pieces Incorrect: ");
      	numberofpiecesIncorrectTField=new JTextField(2);
      	numberofpiecesIncorrectTField.setEditable(false);
      	
      	
      	buttonsPanel.add(exitB);
      	buttonsPanel.add(newImageB);
      	buttonsPanel.add(play_pauseB);
      	
      	GroupLayout grouplayout= new GroupLayout(fieldsPanel);
      	fieldsPanel.setLayout(grouplayout);
		grouplayout.setAutoCreateGaps(true);
		grouplayout.setAutoCreateContainerGaps(true);
		add(fieldsPanel,BorderLayout.EAST);
      	
      	
      	GroupLayout.SequentialGroup hGroup= grouplayout.createSequentialGroup();
		
		hGroup.addGroup(grouplayout.createParallelGroup().addComponent(timerLabel)
														 .addComponent(counterLabel)
														 .addComponent(numberofpiecesIncorrectLabel));
		
		hGroup.addGroup(grouplayout.createParallelGroup().addComponent(timerTField)
				 					.addComponent(counterTField)
				 					.addComponent(numberofpiecesIncorrectTField));

		grouplayout.setHorizontalGroup(hGroup);
		
		GroupLayout.SequentialGroup vGroup = grouplayout.createSequentialGroup();
		
		vGroup.addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(timerLabel).addComponent(timerTField));
		vGroup.addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(counterLabel).addComponent(counterTField));
		vGroup.addGroup(grouplayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(numberofpiecesIncorrectLabel).addComponent(numberofpiecesIncorrectTField));
		

		grouplayout.setVerticalGroup(vGroup);
	
      		
        
		
		
		add(buttonsPanel,BorderLayout.SOUTH);
		add(myBoard,BorderLayout.CENTER);
		myBoard.setBackground(Color.cyan);
		
    

        

        
       
        
        
       String[] options = new String[] {"Easy", "Intermediate","Hard"};
       int response = JOptionPane.showOptionDialog(this, null, "Game Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
       if(response == 0) {
    	   level_of_difficulty = 50;
       }
       else if ( response == 1) {
    	   level_of_difficulty =150;
       }
       else {
    	   level_of_difficulty =300;
       }
		
		setupMainFrame();
		
		height =  myBoard.getHeight();
		 width =  myBoard.getWidth();
	  	
	}
	

	
	void setupMainFrame()
	{
		
		
		
		
		tk=Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();				// Get screen resolution.
		setSize(d.width/2, d.height/2);		// Set size and location based on the resolution.
		setLocation(d.width/4, d.height/4);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// Kill the program is the user closes the window.
		setTitle("Project3AlvaroLeon");
	
		
		setVisible(true);
		
			
	}
	
	public void run(){
       
        String minu="", seco="", mile="";
        try
        {
        	
        
            while( activeTimer )
            {
            	if(!pause) 
            	{
                Thread.sleep( 4 );
                
                mil += 4;

                
                if( mil == 1000 )
                {
                    mil = 0;
                    sec += 1;
                   
                    if( sec == 60 )
                    {
                        sec = 0;
                        min++;
                    }
                }

              
                if( min < 10 ) minu = "0" + min;
                else minu = min.toString();
                if( sec < 10 ) seco = "0" + sec;
                else seco = sec.toString();

                if( mil < 10 ) mile = "00" + mil;
                else if( mil < 100 ) mile = "0" + mil;
                else mile = mil.toString();

               
                timerTField.setText( minu + ":" + seco + ":" + mile );
              
            }
        	}
            timerTField.setText( minu + ":" + seco + ":" + mile );
        }catch(Exception e){}
       
       //timerTField.setText( "00:00:000" );
    }
	
	 public void iniTimer()
	 {
		 if(init) 
		 {
		 	activeTimer = true;
	        pause = false;
			hilo = new Thread(this);
	        hilo.start();
	        init=false;
		 }
	     
	 }
	 
	  
	 public void stopTimer()
	 {    
		 pause = true;
	     init = true;
	 }
	
	
	public void actionPerformed(ActionEvent e)
	{
		
         if( e.getActionCommand().equals("Play/Pause") ){
        	 if(load == false)
 				{
 				return;
 				}
               if(!pause ){
                   
            	   myBoard.pause();
               	stopTimer();    
                }
                else if(pause)
                {
                	myBoard.play();
        			iniTimer();

                } 
            
           }
    
		
		if(e.getActionCommand().equals("Load")) //LOAD
		{
			
			try {
				myBoard.loadImage(height,width);
				load = true;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		if (e.getActionCommand().equals("Exit"))
		{
			System.exit(0);
		}
	
	}


	public void numberCorrectLabel() 
	{	
		moves++;
		counterTField.setText(moves+"");
		
	}
	
	
	public void updateTime() 
	{
		if(!activeTimer) 
		{
			iniTimer();
		}
		else {
			
		}
	}


	public void numberOfMoves(int a) 
	{
		
		
		numberofpiecesIncorrectTField.setText(a+"");
		if(a== 0) 
		{
			myBoard.pause();
           	stopTimer(); 
		}
		else {
			myBoard.play();
			iniTimer();
		}
	}
}



