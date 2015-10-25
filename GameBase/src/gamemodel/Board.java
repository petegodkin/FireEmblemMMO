package gamemodel;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Board {
	private Tile tiles[][];
	private int cols;
	private int rows;
	
	// TODO: make this able to handle more
	public Board(File file, List<String> playerNames) {
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = input.nextLine();
		String[] part = line.split(" ");
		cols = Integer.parseInt(part[0]);
		rows = Integer.parseInt(part[1]);
		tiles = new Tile[cols][rows];
		
		for (int y = 0; y < rows; y++) {
			line = input.nextLine();
			part = line.split(" ");
			assert(part.length == cols);
			for (int x = 0; x < cols; x++) {
				tiles[x][y] = new Tile();
				tiles[x][y].traversable = true;
				if(part[x].equals("x")){
					tiles[x][y].traversable = false;
				}else if(part[x].equals("1")){
					tiles[x][y].piece = tempMakeCharacter(playerNames.get(0));
				}else if(part[x].equals("2")){
					tiles[x][y].piece = tempMakeCharacter(playerNames.get(1));
				}
			}
		}
	}
	
	// obviously very temporary
	public GameCharacter tempMakeCharacter(String playerName) {
		String owner = playerName;
		CharacterStats stats = new CharacterStats(5, 3, 2);
		List<ActionType> actions = Arrays.asList(ActionType.MOVE);
		return new GameCharacter(owner, stats, actions);
	}
	
	public boolean handleAction(GameAction action) {
		if (action.isValid(this)) {
			action.doAction(this);
			return true;
		} else {
			System.out.println("Invalid action!");
			return false;
		}
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public Tile getTile(Point p) {
		return tiles[p.x][p.y];
	}
	
	// Not really meant to be called over and over
	public List<GameCharacter> getCharactersForPlayer(String playerName) {
		List<GameCharacter> characters = new ArrayList<>();
		for (Tile col[] : tiles) {
			for (Tile t : col) {
				if (t.isTraversable() && t.hasCharacter() && 
						playerName.equals(t.getCharacter().getOwnerName()))
				characters.add(t.getCharacter());
			}
		}
		return characters;
	}
	
	public int numCols() {
		return cols;
	}
	
	public int numRows() {
		return rows;
	}
	
	public boolean onBoard(int x, int y) {
		return x >= 0 && x < numCols() && y >= 0 && y < numRows();
	}
	
	public boolean isOccupied(int x, int y) {
		return tiles[x][y].hasPiece();
	}
	
	public boolean isTraversable(int x, int y) {
		return tiles[x][y].isTraversable();
	}
}
