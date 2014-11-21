package Game;

import java.util.ArrayList;

public class ComputerPlayer extends Player {

	private static final long serialVersionUID = 5731598477435966344L;
	private ComputerStrategy currentStrategy;

	/**
	 * This constructor creates a computer player with the passed
	 * in strategy.
	 * @param name
	 * @param strategy
	 */
	public ComputerPlayer(String name, ComputerStrategy strategy) {
		super(name);
		currentStrategy = strategy;
	}

	/**
	 * This method calls the computer's strategy to
	 * choose a country during the initialization phase.
	 * @param gameMap
	 * @return
	 */
	public String chooseCountry(Map gameMap) {
		return currentStrategy.chooseCountry(this, gameMap);
	}
	
	/**
	 * This method calls the computer's strategy to
	 * choose a country it owns for usage during attack
	 * and fortify stages.
	 * @param gameMap
	 * @return
	 */
	public String selectOwnedCountry(Map gameMap){
		return currentStrategy.selectOwnedCountry(this, gameMap);
	}
	
	/**
	 * This method calls the computer's strategy to
	 * place bonus armies on its owned countries. 
	 * @param gameMap
	 * @return
	 */
	public String placeArmy(Map gameMap){
		return currentStrategy.placeArmy(this, gameMap);
	}

	/**
	 * This method calls the computer's strategy to choose
	 * the country it wants to attack.
	 * @param selectedCountry
	 * @param gameMap
	 * @return
	 */
	public String attack(String selectedCountry, Map gameMap){
		return currentStrategy.attack(this, selectedCountry, gameMap);
	}
	
	/**
	 * This method calls the computer's strategy to choose
	 * the country it wants to fortify.
	 * @param selectedCountry
	 * @param gameMap
	 * @return
	 */
	public String fortify(String selectedCountry, Map gameMap) {
		return currentStrategy.fortify(this, selectedCountry, gameMap);
	}
	
	/**
	 * This method calls the computer's strategy to choose
	 * the number of attack dice it will roll.
	 * @param selectedCountry
	 * @param gameMap
	 * @return
	 */
	public int chooseAttackDice(String selectedCountry, Map gameMap){
		return currentStrategy.chooseAttackDice(selectedCountry, gameMap);
	}
	
	/**
	 * This method calls the computer's strategy to choose
	 * the number of defending dice it will roll.
	 * @param selectedCountry
	 * @param gameMap
	 * @return
	 */
	public int chooseDefendDice(String selectedCountry, Map gameMap){
		return currentStrategy.chooseDefendDice(selectedCountry, gameMap);
	}

	/**
	 * This method calls the computer's strategy to decide
	 * which cards it will turn in.
	 * @return
	 */
	public ArrayList<Card> turnInCards() {
		return currentStrategy.turnInCards(this.myHand);
	}
	
	/**
	 * This method calls the computer's strategy to decide
	 * how many armies it will move into a defeated country.
	 * @param selectedCountry
	 * @param gameMap
	 * @return
	 */
	public int moveArmies(String selectedCountry, Map gameMap){
		return currentStrategy.moveArmies(selectedCountry, gameMap);
	}
	
	/**
	 * This method calls the computer's strategy to decide if
	 * it will continue to attack or not.
	 * @param player
	 * @param gameMap
	 * @return
	 */
	public boolean playAgain(Player player, Map gameMap) {
		return currentStrategy.playAgain(player, gameMap);
	}
	
}
