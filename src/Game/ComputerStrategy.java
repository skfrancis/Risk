package Game;

import java.util.ArrayList;


public interface ComputerStrategy {
	public String chooseCountry(Player player, Map gameMap);
	public String selectOwnedCountry(Player player, Map gameMap);
	public String placeArmy(Player player, Map gameMap);
	public int moveArmies(String selectedCountry, Map gameMap);
	public String attack(Player player, String selectedCountry, Map gameMap);
	public String fortify(Player player, String selectedCountry, Map gameMap);
	public ArrayList<Card> turnInCards(ArrayList<Card> playerHand);
	public int chooseAttackDice(String selectedCountry, Map gameMap);
	public int chooseDefendDice(String selectedCountry, Map gameMap);
	public boolean playAgain(Player player, Map gameMap);
}
