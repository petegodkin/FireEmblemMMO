package gameclient.control;

import java.awt.Point;
import java.util.UUID;

import gameclient.GameClient;
import gameclient.GameView;
import gamemodel.Board;
import gamemodel.Tile;
import gamemodel.impl.MoveAction;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameController {
	GameClient client;
	
	
	@FXML
	Canvas mainCanvas;
	
	@FXML
	protected void handleClick(MouseEvent event) {
		select();
	}
	
	@FXML
	protected void handleMouseMove(MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		if (x > 0 && y > 0 && x < mainCanvas.getWidth() && y < mainCanvas.getHeight()) {
			client.getGameView().cursor = getCell(x, y);
		}
		client.getGameDisplay().update();
	}
	
	@FXML
	protected void handleKeyPress(KeyEvent event) {
		KeyCode key = event.getCode();
		if (key.equals(KeyCode.W) || key.equals(KeyCode.UP)) {
			cursorUp();
		} else if (key.equals(KeyCode.A) || key.equals(KeyCode.LEFT)) {
			cursorLeft();
		} else if (key.equals(KeyCode.S) || key.equals(KeyCode.DOWN)) {
			cursorDown();
		} else if (key.equals(KeyCode.D) || key.equals(KeyCode.RIGHT)) {
			cursorRight();
		} else if (key.equals(KeyCode.ENTER)) {
			select();
		}
		client.getGameDisplay().update();
	}
	
	@FXML
	protected void handleKeyRelease(KeyEvent event) {
	}
	
	private void select() {
			client.getGameView().select();
	}
	
	private void cursorUp() {
		if (client.getGameView().cursor.y > 0) {
			client.getGameView().cursor.y--;
		}
	}
	private void cursorLeft() {
		if (client.getGameView().cursor.x > 0) {
			client.getGameView().cursor.x--;
		}
	}
	private void cursorDown() {
		Board b = client.getGameView().getBoard();
		if (client.getGameView().cursor.y < b.numRows() - 1) {
			client.getGameView().cursor.y++;
		}
	}
	private void cursorRight() {
		Board b = client.getGameView().getBoard();
		if (client.getGameView().cursor.x < b.numCols() - 1) {
			client.getGameView().cursor.x++;
		}
	}
	
	public void init(GameClient client) {
		this.client = client;
		client.getGameDisplay().setCanvas(mainCanvas);
	}
	
	private Point getCell(double x, double y) {
		double w = mainCanvas.getWidth();
		double h = mainCanvas.getHeight();
		Board b = client.getGameView().getBoard();
		return new Point((int)(x / w * b.numCols()), (int)(y / h * b.numRows()));
	}
}


