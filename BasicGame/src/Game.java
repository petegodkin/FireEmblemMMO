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
import java.util.Scanner;
import static java.lang.Math.abs;


public class Game implements Runnable{
   
   final int gridSize = 50;
   int mapSizex = 0;
   int mapSizey = 0;
   
   //int player[] = {0, 0};
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
   boolean gameover = false;
   
   JFrame frame;
   Canvas canvas;
   BufferStrategy bufferStrategy;
   
   private class Menu{
      String string;
      int i;
      Menu next;
      Menu(String string, int i){
	     this.string = string;
	     this.i = i;
	  }
   }
   
   Menu menu = null;
   Menu combatmenu = null;
   int items;
   
	private class MoveRange {
		int x;
		int y;
		int distance;
		MoveRange next;
		MoveRange(int x, int y, int distance){
			this.x = x;
			this.y = y;
			this.distance = distance;
			next = null;
		}
	}
	
	MoveRange moverange = null;
	
	private class AiSearchRange {
		int x;
		int y;
		int distance;
		AiSearchRange next;
		AiSearchRange back;
		AiSearchRange(int x, int y, int distance, AiSearchRange back){
			this.x = x;
			this.y = y;
			this.distance = distance;
			this.back = back;
			next = null;
		}
	}
	
	AiSearchRange aisearchrange = null;
	
	private class Obstacle {
		int x;
		int y;
		Obstacle next;
		Obstacle(int x, int y){
			this.x = x;
			this.y = y;
			next = null;
		}
	}
	
	Obstacle obstacle = null;
	
	private class Unit {
		int x;
		int y;
		int maxHP;
		int HP;
		int damage;
		double accuracy;
		int movespeed;
		boolean action;
		Unit next;
		Unit(int x, int y, int HP, int damage, double accuracy, int movespeed){
			this.x = x;
			this.y = y;
			this.maxHP = HP;
			this.HP = HP;
			this.damage = damage;
			this.accuracy = accuracy;
			this.movespeed = movespeed;
			action = true;
		}
	}
	
    Unit player = null;
    Unit selected = null;
    
	private class AIUnit {
		int x;
		int y;
		int maxHP;
		int HP;
		int damage;
		double accuracy;
		int movespeed;
		boolean action;
		int aisearchrange;
		AIUnit next;
		AIUnit(int x, int y, int HP, int damage, double accuracy, int movespeed, int aisearchrange){
			this.x = x;
			this.y = y;
			this.maxHP = HP;
			this.HP = HP;
			this.damage = damage;
			this.accuracy = accuracy;
			this.movespeed = movespeed;
			this.aisearchrange = aisearchrange;
			action = true;
		}
	}
	

	AIUnit enemy = null;
	AIUnit aiselected = null;
	
   public Game(String a){
      frame = new JFrame("Basic Game");
      combatmenu = new Menu("HP", 0);
      combatmenu.next = new Menu("Mt", 1);
      combatmenu.next.next = new Menu("Hit", 2);
      File file = new File(a);
  	  Scanner input = null;
  	  String line = null;
      try {
    	  input = new Scanner(file);
	  } catch (FileNotFoundException e) {
		  e.printStackTrace();
	  }
  	  int linecount = 0;
  	  while( input.hasNextLine() ){
   		  line = input.nextLine();
   		  linecount++;
   	  }
      try {
    	  input = new Scanner(file);
	  } catch (FileNotFoundException e) {
		  e.printStackTrace();
	  }
  	  String[] part;
  	  for(int i = 0; i < linecount; i++){
  		  part = new String[3];
  	 	  line = input.nextLine();
  	 	  part = line.split(" ");
  	 	  for(int j = 0; j < part.length; j++){
  	 		  if(i == 0){
  	 			  if(j == 0){
  	 				  mapSizex = Integer.parseInt(part[j]);
  	 			  }else{
  	 				  mapSizey = Integer.parseInt(part[j]);
  	 			  }
  	 		  }else{
  	 			  if(part[j].compareTo("x") == 0){
                      if(obstacle == null){
                    	  obstacle = new Obstacle(j, i-1);
                      }else{
                    	  Obstacle read = obstacle;
                          while(read.next != null){
                        	  read = read.next;
                          }
                          read.next = new Obstacle(j, i-1);
                      }
  	 			  }else if(part[j].compareTo("E") == 0){
  	 				  if(enemy == null){
  	 					  enemy = new AIUnit(j, i-1, 10, 5, .60, 4, 10);
  	 				  }else{
  	 					  AIUnit read = enemy;
  	 					  while(read.next != null){
  	 						  read = read.next;
  	 					  }
  	 					  read.next = new AIUnit(j, i-1, 10, 5, .60, 4, 10);
  	 				  }
  	 			  }else if(part[j].compareTo("P") == 0){
  	 				  if(player == null){
  	 					player = new Unit(j, i-1, 20, 8, .8, 5);
  	 				  }else{
  	 					  Unit read = player;
  	 					  while(read.next != null){
  	 						  read = read.next;
  	 					  }
  	 					  read.next = new Unit(j, i-1, 20, 8, .8, 5);
  	 				  }
  	 			  }
  	 		  }
  	 	  }
  	  }
      JPanel panel = (JPanel) frame.getContentPane();
      panel.setPreferredSize(new Dimension(mapSizex*gridSize, mapSizey*gridSize));
      panel.setLayout(null);
      
      canvas = new Canvas();
      canvas.setBounds(0, 0, mapSizex*gridSize+1, mapSizey*gridSize+1);
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
           if(gameover == true){
         	  frame.setVisible(false); //you can't see me!
         	  frame.dispose(); //Destroy the JFrame object
         	  return;
           }
      }
   }
   
   private void render() {
      Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
      g.clearRect(0, 0, mapSizex*gridSize, mapSizey*gridSize);
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
      if(turn == "enemy"){
    	  AIUnit unit = enemy;
    	  while(unit != null){
    		  aiselected = unit;
    		  aisearch();
    		  Unit saftey = null;
    		  Unit search = player;
    		  while(search != null){
    			  if(abs(search.x - unit.x) + abs(search.y - unit.y) == 1){
			          if(Math.random() <= aiselected.accuracy){
				         search.HP = search.HP - aiselected.damage;
				         if(search.HP <= 0){
			    		    if(search == player){
			    			   player = search.next;
			    			}else{
			    			   saftey.next = search.next;
			    			}
				         }
				     }
			         break;
    			  }
    			  saftey = search;
    			  search = search.next;
    		  }
    		  unit = unit.next;
    		  aiselected = null;
    		  aisearchrange = null;
    	  }
    	  turn = "player";
    	  action = "";
      }
      if(esc == true && turn == "player"){
    	  if(action == "selected"){
    		  selected = null;
    		  moverange = null;
    		  action = "";
    	  }else if(action == "confirm"){
		      selected.x = movestart[0];
		      selected.y = movestart[1];
		      action = "selected";
    	  }else if(action == "target"){
    		  action = "confirm";
    	  }
      }
      if(execute == false && click == true && turn == "player"){
	  //if(action != "execute" && click == true){
    	  execute = true;
		  //action = "execute";
	      //if(selected == true && confirm == false){
	      Unit unit = player;
	      while(unit != null){
	    	  if(X/gridSize == unit.x && Y/gridSize == unit.y){
	    		  break;
	    	  }
	    	  unit = unit.next;
	      }
		  if(action == "selected"){
	    	  MoveRange node = moverange;
	    	  while(node != null){
	    		  if(X/gridSize == node.x && Y/gridSize == node.y){
	    			  break;
	    		  }
	    		  node = node.next;
	    	  }
	    	  Unit collide = player;
	    	  while(collide != null){
	    		  if(X/gridSize == collide.x && Y/gridSize == collide.y && collide != selected){
	    			  node = null;
	    		  }
	    		  collide = collide.next;
	    	  }
	    	  if(node != null){
    		      movestart[0] = selected.x;
    		      movestart[1] = selected.y;
    		      selected.x = X/gridSize;
    		      selected.y = Y/gridSize;
                  //selected = false;
                  //confirm = true;
                  action = "confirm";
                  //moverange = null;
            	  items = 0;
            	  menu = new Menu("Attack", 0);
            	  items++;
            	  menu.next = new Menu("Move", 1);
            	  items++;
            	  menu.next.next = new Menu("Cancel", 2);
            	  items++;
	    	  }
	    	  
	      }else if(unit != null && action == ""){
	    	  if(unit.action == true){
		    	  action = "selected";
		    	  selected = unit;
                  moverange();
	    	  }
	      }else if(action == "confirm"){
	    	  Menu search = menu;
	    	  while(search != null){
				  if(X > (selected.x+1)*gridSize+10 && X < (selected.x+1)*gridSize+110 && Y > (int)((selected.y+.5)*gridSize-(5+15*items/2-15*search.i)) && Y < (int)((selected.y+.5)*gridSize-(5+15*items/2-15*search.i))+15){
                     if(search.string == "Move"){
				        action = "";
				        endmove();
			         }else if(search.string == "Cancel"){
				        selected.x = movestart[0];
				        selected.y = movestart[1];
				        action = "";
			            moverange = null;
			            selected = null;
			         }else{
				        AIUnit loop = enemy;
				        while(loop != null){
				           if(search.string == "Attack" && ((selected.x == loop.x && selected.y == loop.y + 1) || (selected.x == loop.x && selected.y == loop.y - 1) || (selected.x == loop.x + 1 && selected.y == loop.y)
				            || (selected.x == loop.x - 1 && selected.y == loop.y))){
				    	      action = "target";
				           }
				           loop = loop.next;
				        }
			         }
                     break;
				 }
	    	     search = search.next;
	    	  }
	      }else if(action == "target"){
			  AIUnit loop = enemy;
			  AIUnit saftey = null;
			  while(loop != null){
				  if(X/gridSize == loop.x && Y/gridSize == loop.y){
					  action = "";
			          if(Math.random() <= selected.accuracy){
			             loop.HP = loop.HP - selected.damage;
			        	 if(loop.HP <= 0){
			        	    if(enemy == loop){
			        		    enemy = enemy.next;
			        		}else{
			        		   saftey.next = loop.next;
			        		}
			             }
			          }
			          endmove();
			      }
				  saftey = loop;
				  loop = loop.next;
			  }
			  if(enemy == null){
				  gameover = true;
				  PrintWriter writer;
				try {
				   writer = new PrintWriter("Pause.txt", "UTF-8");
			       writer.println("1");
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
      if(esc == true){
         esc = false;
      }
   }
   
   /**
    * Rewrite this method for your game
    */
   protected void render(Graphics2D g){
	  g.setColor(Color.black);
	  for(int x = 0; x <= mapSizex; x++){
		  g.drawLine(x*gridSize, 0, x*gridSize, mapSizey*gridSize);
	  }
      for(int y = 0; y <= mapSizey; y++){
	      g.drawLine(0, y*gridSize, mapSizex*gridSize, y*gridSize);
      }
      g.setColor(Color.black);
      Obstacle read = obstacle;
      while(read != null){
    	  g.fillRect(read.x*gridSize, read.y*gridSize, gridSize, gridSize);
    	  read = read.next;
      }
      g.setColor(Color.cyan);
      if(moverange!=null && action != "target"){
          if(moverange.next!=null){
    	      MoveRange node = moverange.next;
              while(node != null){
    	          g.fillRect(node.x*gridSize+1, node.y*gridSize+1, gridSize-1, gridSize-1);
    	          node = node.next;
              }
          }
      }
      Unit unit = player;
      while(unit != null){
    	  if(unit != selected || action == "confirm" || action == "target"){
    		  g.setColor(Color.blue);
              g.fillOval(unit.x*gridSize, unit.y*gridSize, gridSize, gridSize);
              if(unit.action == false){
            	  g.setColor(Color.gray);
                  g.fillOval(unit.x*gridSize+3, unit.y*gridSize+3, gridSize-6, gridSize-6);
              }
              g.setColor(Color.green);
              g.fillRect(unit.x*gridSize+1, (unit.y+1)*gridSize-gridSize/10, gridSize*unit.HP/unit.maxHP-1, gridSize/10);
              
          }
          if(action == "confirm"){
        	  g.setColor(Color.blue);
              g.drawOval(movestart[0]*gridSize, movestart[1]*gridSize, gridSize, gridSize);
          }
    	  unit = unit.next;
      }
      if(action == "confirm" || action == "target"){
          g.setColor(Color.yellow);
          g.fillRect((selected.x+0)*gridSize+1, (selected.y+1)*gridSize+1, gridSize-1, gridSize-1);
          g.fillRect((selected.x+0)*gridSize+1, (selected.y-1)*gridSize+1, gridSize-1, gridSize-1);
          g.fillRect((selected.x+1)*gridSize+1, (selected.y+0)*gridSize+1, gridSize-1, gridSize-1);
          g.fillRect((selected.x-1)*gridSize+1, (selected.y+0)*gridSize+1, gridSize-1, gridSize-1);
      }
      g.setColor(Color.blue);
      if(timer < 10 && selected != null){
    	  g.fillOval(selected.x*gridSize, selected.y*gridSize, gridSize, gridSize);
      }
      if(enemy != null){
    	  AIUnit loop = enemy;
    	  while(loop != null){
              g.setColor(Color.red);
              g.fillOval((loop.x*gridSize), loop.y*gridSize, gridSize, gridSize);
              g.setColor(Color.green);
              g.fillRect(loop.x*gridSize+1, (loop.y+1)*gridSize-gridSize/10, gridSize*loop.HP/loop.maxHP-1, gridSize/10);
              loop = loop.next;
    	  }
      }
      //if(selected == true){
      if(action == "selected"){
    	  timer++;
    	  if(timer > 20){
    		  timer = 0;
    	  }
      }
      //if(confirm == true){
      if(action == "confirm"){
          g.setColor(Color.gray);
          g.fillRect((selected.x+1)*gridSize+10, (int)((selected.y+.5)*gridSize-(10+15*items)/2), 100, 10+15*items);
          g.setColor(Color.white);
          g.fillRect((selected.x+1)*gridSize+10+1, (int)((selected.y+.5)*gridSize-(10+15*items)/2)+1, 100-2, 10+15*items-2);
          g.setColor(Color.gray);
          g.drawString("Attack", (selected.x+1)*gridSize+15, (int)((selected.y+.5)*gridSize-(-10+15*items/2-15*menu.i)));
          g.drawString("Move", (selected.x+1)*gridSize+15, (int)((selected.y+.5)*gridSize-(-10+15*items/2-15*menu.next.i)));
          g.drawString("Cancel", (selected.x+1)*gridSize+15, (int)((selected.y+.5)*gridSize-(-10+15*items/2-15*menu.next.next.i)));
      }
      if(click == false && turn == "player"){
    	  if(action == "target"){
			  AIUnit loop = enemy;
			  AIUnit saftey = null;
			  while(loop != null){
				  if(x/gridSize == loop.x && y/gridSize == loop.y){
					  int combat = 3;
			          g.setColor(Color.gray);
			          g.fillRect((loop.x+1)*gridSize+10, (int)((loop.y+.5)*gridSize-(10+15*combat)/2), 100, 10+15*combat);
			          g.setColor(Color.white);
			          g.fillRect((loop.x+1)*gridSize+10+1, (int)((loop.y+.5)*gridSize-(10+15*combat)/2)+1, 100-2, 10+15*combat-2);
			          g.setColor(Color.gray);
			          g.drawString(combatmenu.string, (loop.x+1)*gridSize+50, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.i)));
			          g.drawString(combatmenu.next.string, (loop.x+1)*gridSize+50, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.next.i)));
			          g.drawString(combatmenu.next.next.string, (loop.x+1)*gridSize+50, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.next.next.i)));
			          g.setColor(Color.red);
			          g.drawString(Integer.toString(loop.HP), (loop.x+1)*gridSize+15, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.i)));
			          g.drawString(Integer.toString(loop.damage), (loop.x+1)*gridSize+15, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.next.i)));
			          g.drawString(Double.toString(loop.accuracy), (loop.x+1)*gridSize+15, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.next.next.i)));
			          g.setColor(Color.blue);
			          g.drawString(Integer.toString(selected.HP), (loop.x+1)*gridSize+90, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.i)));
			          g.drawString(Integer.toString(selected.damage), (loop.x+1)*gridSize+90, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.next.i)));
			          g.drawString(Double.toString(selected.accuracy), (loop.x+1)*gridSize+90, (int)((loop.y+.5)*gridSize-(-10+15*combat/2-15*combatmenu.next.next.i)));
			          break;
			      }
				  saftey = loop;
				  loop = loop.next;
			  }
    	  }
      }
   }
   
   private void endmove(){
       moverange = null;
	   selected.action = false;
	   selected = null;
	   boolean end = true;
	   Unit check = player;
	   while(check != null){
		   if(check.action == true){
			   end = false;
			   break;
		   }
		   check = check.next;
	   }
	   if(end == true){
		   check = player;
		   while(check != null){
               check.action = true;
			   check = check.next;
		   }
		   action = "notyourturn";
		   turn = "enemy";
	   }
   }
   
   private void moverange(){
       moverange = new MoveRange(selected.x, selected.y, 0);
       MoveRange current = moverange;
       MoveRange end = moverange;
       boolean right;
       boolean left;
       boolean up;
       boolean down;
       while(current != null && current.distance < selected.movespeed){
     	  MoveRange check = moverange;
     	  right = true;
     	  if(current.x >= mapSizex - 1){
     		  right = false;
     	  }
     	  left = true;
     	  if(current.x <= 0){
     		  left = false;
     	  }
     	  up = true;
     	  if(current.y >= mapSizey - 1){
     		  up = false;
     	  }
     	  down = true;
     	  if(current.y <= 0){
     		  down = false;
     	  }
     	  while(check != null){
     		  if(current.x+1 == check.x && current.y == check.y && right == true && current.x <= mapSizex){
     			  right = false;
     		  }
     		  if(current.x-1 == check.x && current.y == check.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check.x && current.y+1 == check.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check.x && current.y-1 == check.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check = check.next;
     	  }
     	  Obstacle check2 = obstacle;
     	  while(check2 != null){
     		  if(current.x+1 == check2.x && current.y == check2.y && right == true && current.x <= mapSizex){
     			  right = false;
     		  }
     		  if(current.x-1 == check2.x && current.y == check2.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check2.x && current.y+1 == check2.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check2.x && current.y-1 == check2.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check2 = check2.next;
     	  }
     	  AIUnit check3 = enemy;
     	  while(check3 != null){
     		  if(current.x+1 == check3.x && current.y == check3.y && right == true && current.x <= mapSizex){
     			  right = false;
     		  }
     		  if(current.x-1 == check3.x && current.y == check3.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check3.x && current.y+1 == check3.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check3.x && current.y-1 == check3.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check3 = check3.next;
     	  }
     	  if(right == true){
     		  end.next = new MoveRange(current.x+1, current.y, current.distance+1);
     		  end = end.next;
     	  }
     	  if(left == true){
     		  end.next = new MoveRange(current.x-1, current.y, current.distance+1);
     		  end = end.next;
     	  }
     	  if(up == true){
     		  end.next = new MoveRange(current.x, current.y+1, current.distance+1);
     		  end = end.next;
     	  }
     	  if(down == true){
     		  end.next = new MoveRange(current.x, current.y-1, current.distance+1);
     		  end = end.next;
     	  }
     	  current = current.next;
       }
   }
   
   private void aisearch(){
	   aisearchrange = new AiSearchRange(aiselected.x, aiselected.y, 0, null);
	   AiSearchRange choke = null;
       AiSearchRange current = aisearchrange;
       AiSearchRange end = aisearchrange;
       boolean right;
       boolean left;
       boolean up;
       boolean down;
       boolean exit = false;
       while(current != null && current.distance <= aiselected.aisearchrange && exit != true){
     	  right = true;
     	  if(current.x >= mapSizex - 1){
     		  right = false;
     	  }
     	  left = true;
     	  if(current.x <= 0){
     		  left = false;
     	  }
     	  up = true;
     	  if(current.y >= mapSizey - 1){
     		  up = false;
     	  }
     	  down = true;
     	  if(current.y <= 0){
     		  down = false;
     	  }
     	  AiSearchRange check = aisearchrange;
     	  while(check != null){
     		  if(current.x+1 == check.x && current.y == check.y && right == true && current.x <= mapSizex){
     			  right = false;
     		  }
     		  if(current.x-1 == check.x && current.y == check.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check.x && current.y+1 == check.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check.x && current.y-1 == check.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check = check.next;
     	  }
     	  Obstacle check2 = obstacle;
     	  while(check2 != null){
     		  if(current.x+1 == check2.x && current.y == check2.y && right == true && current.x <= mapSizex){
     			  right = false;
     		  }
     		  if(current.x-1 == check2.x && current.y == check2.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check2.x && current.y+1 == check2.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check2.x && current.y-1 == check2.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check2 = check2.next;
     	  }
     	  Unit check4 = player;
     	  while(check4 != null){
     		  if(current.x+1 == check4.x && current.y == check4.y && right == true && current.x <= mapSizex){
     			  right = false;
     			  AiSearchRange spot = current;
     			  while(spot.distance > aiselected.movespeed){
     				  spot = spot.back;
     			  }
     			  aiselected.x = spot.x;
     			  aiselected.y = spot.y;
     			  AIUnit collision = enemy;
     			  while(collision != null){
     				  if(collision.x == spot.x && collision.y == spot.y && collision != aiselected){
      				     choke = new AiSearchRange(aiselected.x, aiselected.y, 0, null);
      				     break;
     				  }
     				  collision = collision.next;
     			  }
     			  if(collision == null){
      			     exit = true;
      			     break;
      			  }
     		  }
     		  if(current.x-1 == check4.x && current.y == check4.y && left == true && current.x >= 0){
     			  left = false;
     			  AiSearchRange spot = current;
     			  while(spot.distance > aiselected.movespeed){
     				  spot = spot.back;
     			  }
     			  aiselected.x = spot.x;
     			  aiselected.y = spot.y;
     			  AIUnit collision = enemy;
     			  while(collision != null){
     				  if(collision.x == spot.x && collision.y == spot.y && collision != aiselected){
      				     choke = new AiSearchRange(aiselected.x, aiselected.y, 0, null);
      				     break;
     				  }
     				  collision = collision.next;
     			  }
     			  if(collision == null){
      			     exit = true;
      			     break;
      			  }
     		  }
     		  if(current.x == check4.x && current.y+1 == check4.y && up == true && current.y <= mapSizey){
     			  up = false;
     			  AiSearchRange spot = current;
     			  while(spot.distance > aiselected.movespeed){
     				  spot = spot.back;
     			  }
     			  aiselected.x = spot.x;
     			  aiselected.y = spot.y;
     			  AIUnit collision = enemy;
     			  while(collision != null){
     				  if(collision.x == spot.x && collision.y == spot.y && collision != aiselected){
      				     choke = new AiSearchRange(aiselected.x, aiselected.y, 0, null);
      				     break;
     				  }
     				  collision = collision.next;
     			  }
     			  if(collision == null){
      			     exit = true;
      			     break;
      			  }
     		  }
     		  if(current.x == check4.x && current.y-1 == check4.y && down == true && current.y >= 0){
     			  down = false;
     			  AiSearchRange spot = current;
     			  while(spot.distance > aiselected.movespeed){
     				  spot = spot.back;
     			  }
     			  aiselected.x = spot.x;
     			  aiselected.y = spot.y;
     			  AIUnit collision = enemy;
     			  while(collision != null){
     				  if(collision.x == spot.x && collision.y == spot.y && collision != aiselected){
     				     choke = new AiSearchRange(aiselected.x, aiselected.y, 0, null);
     				     break;
     				  }
     				  collision = collision.next;
     			  }
     			  if(collision == null){
     			     exit = true;
     			     break;
     			  }

     		  }
     		  check4 = check4.next;
     	  }
     	  if(right == true){
     		  end.next = new AiSearchRange(current.x+1, current.y, current.distance+1, current);
     		  end = end.next;
     	  }
     	  if(left == true){
     		  end.next = new AiSearchRange(current.x-1, current.y, current.distance+1, current);
     		  end = end.next;
     	  }
     	  if(up == true){
     		  end.next = new AiSearchRange(current.x, current.y+1, current.distance+1, current);
     		  end = end.next;
     	  }
     	  if(down == true){
     		  end.next = new AiSearchRange(current.x, current.y-1, current.distance+1, current);
     		  end = end.next;
     	  }
     	  current = current.next;
       }
       if(exit != true && choke != null){
    	   chokepoint();
       }
   }
   
   private void chokepoint(){
	   AiSearchRange aisearchrange2 = new AiSearchRange(aiselected.x, aiselected.y, 0, null);
       AiSearchRange current = aisearchrange2;
       AiSearchRange end = aisearchrange2;
       boolean right;
       boolean left;
       boolean up;
       boolean down;
       boolean exit = false;
       while(current != null && exit != true){
     	  right = true;
     	  if(current.x >= mapSizex - 1){
     		  right = false;
     	  }
     	  left = true;
     	  if(current.x <= 0){
     		  left = false;
     	  }
     	  up = true;
     	  if(current.y >= mapSizey - 1){
     		  up = false;
     	  }
     	  down = true;
     	  if(current.y <= 0){
     		  down = false;
     	  }
     	  AiSearchRange check = aisearchrange;
     	  while(check != null){
     		  if(current.x+1 == check.x && current.y == check.y && right == true && current.x <= mapSizex){
     			  AIUnit overlap = enemy;
     			  while(overlap != null){
     				  if(overlap.x == check.x && overlap.y == check.y){
     				     break;
     				  }
     				  overlap = overlap.next;
     			  }
     			  if(overlap == null){
     				 aiselected.x = check.x;
     				 aiselected.y = check.y;
     			     exit = true;
     			     check = null;
     			     break;
     			  }
     		  }
     		  if(current.x-1 == check.x && current.y == check.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check.x && current.y+1 == check.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check.x && current.y-1 == check.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check = check.next;
     	  }
     	  Obstacle check2 = obstacle;
     	  while(check2 != null){
     		  if(current.x+1 == check2.x && current.y == check2.y && right == true && current.x <= mapSizex){
     			  right = false;
     		  }
     		  if(current.x-1 == check2.x && current.y == check2.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check2.x && current.y+1 == check2.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check2.x && current.y-1 == check2.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check2 = check2.next;
     	  }
     	  Unit check4 = player;
     	  while(check4 != null){
     		  if(current.x+1 == check4.x && current.y == check4.y && right == true && current.x <= mapSizex){
     			  right = false;
     		  }
     		  if(current.x-1 == check4.x && current.y == check4.y && left == true && current.x >= 0){
     			  left = false;
     		  }
     		  if(current.x == check4.x && current.y+1 == check4.y && up == true && current.y <= mapSizey){
     			  up = false;
     		  }
     		  if(current.x == check4.x && current.y-1 == check4.y && down == true && current.y >= 0){
     			  down = false;
     		  }
     		  check4 = check4.next;
     	  }
     	  if(right == true){
     		  end.next = new AiSearchRange(current.x+1, current.y, current.distance+1, current);
     		  end = end.next;
     	  }
     	  if(left == true){
     		  end.next = new AiSearchRange(current.x-1, current.y, current.distance+1, current);
     		  end = end.next;
     	  }
     	  if(up == true){
     		  end.next = new AiSearchRange(current.x, current.y+1, current.distance+1, current);
     		  end = end.next;
     	  }
     	  if(down == true){
     		  end.next = new AiSearchRange(current.x, current.y-1, current.distance+1, current);
     		  end = end.next;
     	  }
     	  current = current.next;
       }
   }
   
   public static boolean main(String [] args){
      Game ex = new Game("Map1.txt");
      new Thread(ex).start();
	return true;
   }

}