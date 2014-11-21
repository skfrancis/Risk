package Game;

public class HumanPlayer extends Player {

	private static final long serialVersionUID = -6072820011009460396L;
	private boolean isReady;

	public HumanPlayer(String name) {
		super(name);
		isReady = false;
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	public void resetReady () {
		isReady = false;
	}
	
	public void setReady () {
		isReady = true;
	}
}
