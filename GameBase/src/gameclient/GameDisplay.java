package gameclient;

import gamemodel.Board;
import gamemodel.GameCharacter;
import gamemodel.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class Takes information from a GameView and displays it on the javafx canvas
 */
public class GameDisplay {
	GameView gameView;
	Canvas canvas;
	
	public GameDisplay() {}
	
	public GameDisplay(GameView gameView, Canvas canvas) {
		this.gameView = gameView;
		this.canvas = canvas;
	}
	
	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	//TODO: should take in a deltaTime later or calculate it
	public void update() {
		double height = canvas.getHeight();
		double width = canvas.getWidth();
		Board board = gameView.getBoard();
		int numCols = board.numCols();
		int numRows = board.numRows();
		
		double cellWidth = width / numCols;
		double cellHeight = height / numRows;
			
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		graphics.clearRect(0, 0, width, height);
		
		Tile tiles[][] = board.getTiles();
		
		double cellX;
		double cellY;
		
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				cellX = cellWidth * x;
				cellY = cellHeight * y;
				Tile t = tiles[x][y];
				//draw green rectangle for background
				graphics.setFill(Color.GREEN);
				graphics.fillRect(cellX, cellY, cellWidth, cellHeight);
				if (t.hasCharacter()) {
					GameCharacter gc = t.getCharacter();
					if (gc.getOwnerName().equals(gameView.getPlayerName())) {
						graphics.setFill(Color.BLUE);
						graphics.fillOval(cellX, cellY, cellWidth, cellHeight);
					} else {
						graphics.setFill(Color.RED);
						graphics.fillOval(cellX, cellY, cellWidth, cellHeight);
					}
				} else if (t.hasPiece()) {
					graphics.setFill(Color.BROWN);
					graphics.fillRect(cellX, cellY, cellWidth, cellHeight);
				} else if (!t.isTraversable()) {
					graphics.setFill(Color.BLACK);
					graphics.fillRect(cellX, cellY, cellWidth, cellHeight);
				}
			}
		}
		
		graphics.setFill(new Color(1.0, 1.0, 0.0, 0.5));
		cellX = cellWidth * gameView.cursor.x;
		cellY = cellHeight * gameView.cursor.y;
		graphics.fillRect(cellX, cellY, cellWidth, cellHeight);
		
		if (gameView.selected != null) {
			graphics.setFill(new Color(0.0, 0.0, 1.0, 0.75));
			cellX = cellWidth * gameView.selected.x;
			cellY = cellHeight * gameView.selected.y;
			graphics.fillRect(cellX, cellY, cellWidth, cellHeight);
		}
	}
	
	public void startTurn() {
		System.out.println("Your turn!");
	}
	
	public void endTurn() {
		System.out.println("End of your turn.");
	}
	
	public void youWon() {
		System.out.println("You won!");
	}
	
	public void youLost() {
		System.out.println("You lost!");
	}
}
