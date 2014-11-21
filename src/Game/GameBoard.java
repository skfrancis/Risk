package Game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class GameBoard extends Observable implements Serializable {

	private static final long serialVersionUID = -5997134410439833889L;
	private CardDeck gameDeck;
	private Map gameMap;
	private int currentPlayer, turnedInSets;
	private static Random boardGenerator = new Random();
	private ArrayList<Player> gamePlayers;
	private int totalPlayers;
	private boolean defeatedCountry;

	public GameBoard() {
		totalPlayers = 0;
		turnedInSets = 0;
		currentPlayer = 0;
		gamePlayers = new ArrayList<Player>();
		gameMap = new Map("riskmap.txt");
		gameDeck = new CardDeck("riskcards.txt");
		defeatedCountry = false;
	}

	/**
	 * This method adds a player to the game.
	 * 
	 * @param player
	 */
	public void addPlayer(Player player) {
		if (totalPlayers == 0) {
			player.setColor(Color.RED);
		}
		if (totalPlayers == 1) {
			player.setColor(Color.WHITE);
		}
		if (totalPlayers == 2) {
			player.setColor(Color.GREEN);
		}
		if (totalPlayers == 3) {
			player.setColor(Color.YELLOW);
		}
		if (totalPlayers == 4) {
			player.setColor(Color.CYAN);
		}
		if (totalPlayers == 5) {
			player.setColor(Color.MAGENTA);
		}
		gamePlayers.add(player);
		++totalPlayers;
	}

	public void removePlayer(int index) {
		gamePlayers.remove(index);
	}

	public Player getPlayer(int index) {
		return gamePlayers.get(index);
	}

	/**
	 * This method returns the current player
	 * 
	 * @return
	 */
	public Player currentPlayer() {
		return gamePlayers.get(currentPlayer);
	}

	/**
	 * This method advances to the next player
	 */
	public void nextPlayer() {
		++currentPlayer;
		if (currentPlayer == gamePlayers.size()) {
			currentPlayer = 0;
		}
		if(this.isInitialized() == true){
			while(gameMap.getCountriesOwned(gamePlayers.get(currentPlayer)) == 0){
				++currentPlayer;
				if (currentPlayer == gamePlayers.size()) {
					currentPlayer = 0;
				}
			}
		}
		
	}

	public int getPlayerSize() {
		return gamePlayers.size();
	}

	public Map getMap() {
		return gameMap;
	}
	
	public void setDefeatedCountry(boolean set) {
		defeatedCountry = set;
	}

	/**
	 * This method checks to see if the map has been initialized. i.e. checks to
	 * see if all countries have been occupied.
	 * 
	 * @return
	 */
	public boolean isInitialized() {
		return gameMap.checkInitialization();
	}

	/**
	 * This method checks the map to see if the passed in player has won the
	 * game
	 * 
	 * @param player
	 * @return
	 */
	public boolean isWinner(Player player) {
		return gameMap.checkWinner(player);
	}

	/**
	 * This method returns the the total starting armies for players based on
	 * the number of players present.
	 * 
	 * @return
	 */
	public int startingArmies() {
		if (gamePlayers.size() == 2) {
			return 100;
		}
		if (gamePlayers.size() == 3) {
			return 105;
		}
		if (gamePlayers.size() == 4) {
			return 120;
		}
		if (gamePlayers.size() == 5) {
			return 125;
		}

		// for 6 players
		return 120;
	}

	/**
	 * This method calculates the number of armies a player gets to place at the
	 * beginning of their turn.
	 * 
	 * @param player
	 * @return
	 */
	public int calculateArmies(Player player) {
		int totalArmies = 0;
		totalArmies += gameMap.getContinentBonus(player);
		if (gameMap.getCountriesOwned(player) < 9) {
			totalArmies += 3;
		} else {
			totalArmies += (gameMap.getCountriesOwned(player) / 3);
		}
		return totalArmies;
	}

	/**
	 * This method deals a card to the passed in player.
	 * 
	 * @param player
	 */
	public void dealCard(Player player) {
		player.addCard(gameDeck.deal());
	}
	
	public boolean hasDefeatedCountry(){
		return defeatedCountry;
	}
	
	/**
	 * This method returns a set a player turned in to the card deck. If the
	 * player owned the territory turned in an extra two armies will be added to
	 * that territory. It will increment the number of sets that has been turned
	 * in as well.
	 * 
	 * @param cardSet
	 */
	public void turnInSet(ArrayList<Card> cardSet) {
		for (int i = 0; i < cardSet.size(); i++) {
			if (gameMap.getCountryOwner(cardSet.get(i).getTerritory()) == currentPlayer()) {
				gameMap.addArmies(cardSet.get(i).getTerritory(), 2);
			}
			gameDeck.add(cardSet.get(i));
			turnedInSets++;
		}
	}

	/**
	 * This method returns the total amount of armies a player gets if they turn
	 * in a card set.
	 * 
	 * @return
	 */
	public int armiesForTurnIn() {
		if (turnedInSets < 6) {
			return ((2 * turnedInSets) + 2);
		} else {
			return ((turnedInSets - 6) * 5) + 15;
		}
	}

	public void placeArmies(String countryName, int armies) {
		gameMap.addArmies(countryName, armies);
		this.notifyObservers();
	}

	public void setCountryOwner(String countryName, Player player) {
		gameMap.setCountryOwner(countryName, player);
		this.notifyObservers();
	}

	public void fortifyCountry(String oldCountry, String newCountry, int armiesMoved) {
		gameMap.addArmies(newCountry, armiesMoved);
		gameMap.removeArmies(oldCountry, armiesMoved);
		this.notifyObservers();
	}

	public int[] rollDice(String attacker, int attackDice, String defender,
			int defendDice) {
		int smallerDiceSet = 0;
		int[] attackersRoll = new int[attackDice];
		int[] defendersRoll = new int[defendDice];
		
		defeatedCountry = false;
		for (int i = 0; i < attackersRoll.length; i++) {
			attackersRoll[i] = boardGenerator.nextInt(5) + 1;
		}
		attackersRoll = bubbleSort(attackersRoll);

		for (int i = 0; i < defendersRoll.length; i++) {
			defendersRoll[i] = boardGenerator.nextInt(5) + 1;
		}
		defendersRoll = bubbleSort(defendersRoll);

		if (attackersRoll.length <= defendersRoll.length) {
			smallerDiceSet = attackersRoll.length;
		} else {
			smallerDiceSet = defendersRoll.length;
		}

		int defenderLoss = 0;
		int attackerLoss = 0;
		int countryDefeated = 0;
		for (int i = 0; i < smallerDiceSet; ++i) {
			if (defendersRoll[i] >= attackersRoll[i]) {
				gameMap.removeArmies(attacker, 1);
				++attackerLoss;
			} else {
				gameMap.removeArmies(defender, 1);
				++defenderLoss;
				if (gameMap.getCountryArmy(defender) == 0) {
					countryDefeated = 1;
					gameMap.setCountryOwner(defender, currentPlayer());
					gameMap.removeArmies(attacker, attackDice);
					gameMap.addArmies(defender, attackDice);
					defeatedCountry = true;
				}
			}
		}
		this.notifyObservers();
		int [] losses = {attackerLoss, defenderLoss, countryDefeated};
		return losses;
	}

	/**
	 * This method is used to load in a different card deck for use.
	 * 
	 * @param fileName
	 */
	public void loadCardDeck(String fileName) {
		gameDeck = new CardDeck(fileName);
	}

	/**
	 * This method is used to load in a different map for use.
	 * 
	 * @param fileName
	 */
	public void loadMap(String fileName) {
		gameMap = new Map(fileName);
	}

	private int[] bubbleSort(int[] dice) {
		boolean sorted = true;
		int temp;
		while (sorted) {
			sorted = false;
			for (int i = 0; i < dice.length - 1; i++) {
				if (dice[i] < dice[i + 1]) {
					temp = dice[i];
					dice[i] = dice[i + 1];
					dice[i + 1] = temp;
					sorted = true;
				}
			}
		}
		return dice;
	}
}
