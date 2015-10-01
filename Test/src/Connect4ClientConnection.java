import java.io.IOException;

public class Connect4ClientConnection {
	private static final int PLSWAIT = 0;
	private static final int THEIRTURN = 0;
	private static final int YOURTURN = 0;
	private static final int THEYWON = 0;
	private static final int THEYQUIT = 0;
	private static final int THEYTIED = 0;
	private static final int GAMEOVER = 0;
	private static final int ERROR = 0;

	Connect4ClientConnection(Applet a) throws IOException {
		super(new Socket(a.getCodeBase().getHost(), PORTNUM));
		}
	
	public int getTheirMove() {
		// Make sure we’re still connected
		if (!isConnected()) 
		throw new NullPointerException("Attempted to read closed socket!");
		try {
		String s = receive();
		System.out.println("Received:" + s);
		if (s == null)
		return GAMEOVER;
		s = s.trim();
		try {
		return (new Integer(s)).intValue();
		}
		catch (NumberFormatException e) {
		// It was probably a status report error
		return getStatus(s);
		}
		}
		catch (IOException e) {
		System.out.println("I/O Error:" + e);
		System.exit(1);
		return 0;
		}
		}
	
	private String receive() {
		// TODO Auto-generated method stub
		return null;
	}

	private int getStatus(String s) {
		s = s.trim();
		if (s.startsWith("PLSWAIT"))
		return PLSWAIT;
		if (s.startsWith("THEIRTURN"))
		return THEIRTURN;
		if (s.startsWith("YOURTURN"))
		return YOURTURN;
		if (s.startsWith("THEYWON"))
		return THEYWON;
		if (s.startsWith("THEYQUIT"))
		return THEYQUIT;
		if (s.startsWith("THEYTIED"))
		return THEYTIED;
		if (s.startsWith("GAMEOVER"))
		return GAMEOVER;
		// Something has gone horribly wrong!
		System.out.println("received invalid status from server:" + s);
		return ERROR;

}
