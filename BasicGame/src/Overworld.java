import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import static java.lang.Math.abs;


public class Overworld implements Runnable{
   
   final int gridSize = 50;
   int mapSizex = 0;
   int mapSizey = 0;
   
   int movestart[] = {0, 0};
   int X = 0;
   int Y = 0;
   int x = 0;
   int y = 0;
   boolean esc = false;
   String action = "";
   String turn = "player";
   Random rand = new Random();
   boolean click = false;
   boolean execute = false;
   int timer = 0;
   boolean pause = false;
   
   Socket socket;
   PrintStream outStream = null;
   DataInputStream inStream = null;
   ServerSocket port = null;
   
   JFrame frame;
   Canvas canvas;
   BufferStrategy bufferStrategy;
  
	
   public Overworld(){
      frame = new JFrame("Basic Game");
      JPanel panel = (JPanel) frame.getContentPane();
      panel.setPreferredSize(new Dimension(1000, 1000));
      panel.setLayout(null);
      
      canvas = new Canvas();
      canvas.setBounds(0, 0, 1020, 1020);
      canvas.setIgnoreRepaint(true);
      
      panel.add(canvas);
      
      canvas.addMouseListener(new MouseControl());
      canvas.addMouseMotionListener(new MyClass());
      canvas.addKeyListener(new KeyControl());
      
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setResizable(false);
      frame.setVisible(true);
      
      canvas.createBufferStrategy(2);
      bufferStrategy = canvas.getBufferStrategy();
      
      canvas.requestFocus();
   }
   
        
   private class MouseControl extends MouseAdapter{
      public void mousePressed(MouseEvent event){
    	  if(click == false && SwingUtilities.isLeftMouseButton(event)){
    		  X = event.getX();
    		  Y = event.getY();
    	      click = true;
    	  }
    	  if(SwingUtilities.isRightMouseButton(event)){
    		  esc = true;
    	  }

      }
      public void mouseReleased(MouseEvent event){
    	  click = false;
    	  execute = false;
      }
   }
   
   public class MyClass implements MouseMotionListener {

	    public void mouseMoved(MouseEvent e) {
	       x = e.getX();
	       y = e.getY();
	    }

	    public void mouseDragged(MouseEvent e) {
	       //do something
	    }
	}
   
   public class KeyControl extends JFrame implements KeyListener {
	    public void keyPressed(KeyEvent e) {
	       if (e.getKeyCode() == KeyEvent.VK_ESCAPE && esc == false) {
	          esc = true;
	       }
	    }

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}
   }

   
   long desiredFPS = 60;
    long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
    
   boolean running = true;
   
   public void run(){
      
      long beginLoopTime;
      long endLoopTime;
      long currentUpdateTime = System.nanoTime();
      long lastUpdateTime;
      long deltaLoop;
      Scanner input = new Scanner("Pause.txt");
      
      Socket clientSocket;
  	   while (true) {
  	     if (port == null) {
  	        System.out.println("Sorry, the port disappeared.");
  	        System.exit(1);
  	     }
  	     try {
  	        clientSocket = port.accept();
  	        new Connect4Player(this, clientSocket).start();
  	     }
  	     catch (IOException e) {
  	        System.out.println("Couldn’t connect player:" + e);
  	        System.exit(1);
  	     }
  	  }
      
      while(running){
         beginLoopTime = System.nanoTime();
         
         
         
         lastUpdateTime = currentUpdateTime;
         currentUpdateTime = System.nanoTime();
         update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));
         render();
         endLoopTime = System.nanoTime();
         deltaLoop = endLoopTime - beginLoopTime;
           
           if(deltaLoop > desiredDeltaLoop){
               //Do nothing. We are already late.
           }else{
               try{
                   Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
               }catch(InterruptedException e){
                   //Do nothing
               }
           }
           while(pause == true){
        	   File file = new File("Pause.txt");
        	   String line = null;
        	   try {
				input = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   if(input.hasNextLine() == false){
        		   pause = false;
        		   frame.setVisible(true);
        	   }
           }
      }
   }
   
   private void render() {
      Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
      g.clearRect(0, 0, 1000, 1000);
      render(g);
      g.dispose();
      bufferStrategy.show();
   }
   
   //TESTING
   //private double x = 0;
   
   /**
    * Rewrite this method for your game
    */
   protected void update(int deltaTime){
	  if(execute == false && click == true){
		 if(X > 400 && X < 600 && Y > 800 && Y < 900){
	        String[] args = {};
            Game.main(args);
            execute = true;
            pause = true;
            frame.setVisible(false);
		    PrintWriter writer;
		    try {
		       writer = new PrintWriter("Pause.txt", "UTF-8");
	           writer.println("0");
		       writer.close();
		    } catch (FileNotFoundException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		    } catch (UnsupportedEncodingException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		    }
		 }

	  }
   }
   
   /**
    * Rewrite this method for your game
    */
   protected void render(Graphics2D g){
	  g.setColor(Color.blue);
      g.fillRect(400, 800, 200, 100);
	  g.setColor(Color.white);
      g.fillRect(405, 805, 190, 90);
   }
   
   public SocketAction(Socket sock) {
	   super("SocketAction");
	   try {
	      inStream = new DataInputStream(new BufferedInputStream(sock.getInputStream(), 1024));
	      outStream = new PrintStream(new BufferedOutputStream(sock.getOutputStream(), 1024), true);
	      socket = sock;
	   }
	   catch (IOException e) {
	      System.out.println("Couldn’t initialize SocketAction:" + e);
	      System.exit(1);
	   }
   }
   
   public void send(String s) {
	   outStream.println(s);
   }
   @SuppressWarnings("deprecation")
   public String receive() throws IOException {
	   return inStream.readLine();
   }
   
   public boolean isConnected() {
	   return ((inStream != null) && (outStream != null) && (socket != null));
	   }
   
   public static void main(String [] args){
      Overworld ex = new Overworld();
      new Thread(ex).start();
   }

}