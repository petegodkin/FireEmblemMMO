package gameclient.control;

import java.awt.Point;

import gameclient.GameClient;
import gameclient.GameView;
import gamemodel.Board;
import gamemodel.Tile;
import gamemodel.impl.MoveAction;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameController {
	GameClient client;
	
	
	@FXML
	Canvas mainCanvas;
	
	@FXML
	protected void handleClick(MouseEvent event) {
		if (client.isMyTurn()) {
			GameView view = client.getGameView();
			Board board = view.getBoard();
			
			double x = event.getX();
			double y = event.getY();
			
			double w = mainCanvas.getWidth();
			double h = mainCanvas.getHeight();
			
			Point p = new Point((int)(x / w * board.numCols()), (int)(y / h * board.numRows()));
			System.out.println("Clieck tile at x: " + p.x + " y: " + p.y);
			Tile t = board.getTile(p.x, p.y);
			if (t.hasCharacter() && t.getCharacter().getOwnerName().equals(view.getPlayerName())) {
				view.selected = p;
			} else if (view.selected != null && !t.hasPiece() && t.isTraversable()) {
				doMoveAction(p.x, p.y);
			} else {
				view.selected = null;
			}
			client.getGameDisplay().update();
		}
	}
	
	@FXML
	protected void handleKeyPress(KeyEvent event) {
		
	}
	
	private void doMoveAction(int x, int y) {
		GameView view = client.getGameView();
		MoveAction action = new MoveAction(view.selected.x, view.selected.y, x, y);
		if (view.getBoard().handleAction(action)) {		
			Object data[] = new Object[5];
			data[0] = MoveAction.class;
			data[1] = view.selected.x;
			data[2] = view.selected.y;
			data[3] = x;
			data[4] = y;
			client.sendAction(data);
		}
	}
	
	public void init(GameClient client) {
		this.client = client;
		client.getGameDisplay().setCanvas(mainCanvas);
	}
}
