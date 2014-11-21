package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class IntermediateStrategy implements ComputerStrategy, Serializable {

	private static final long serialVersionUID = -2056491860083340692L;
	private Random strategyGenerator = new Random();
	
	/**
	 * This method will choose a continent with an occupied country it owns
	 * based on the highest owned country ratio first then choose a random available
	 * country from that continent. If there is no country available to fill
	 * a continent it will randomly choose another continent and country.
	 */
	public String chooseCountry(Player player, Map gameMap) {
		int index;
		double countriesOwned;
		double totalCountries;
		double countryRatio;
		double largestRatio = 0.0;
		boolean countryAvailable;
		boolean countrySelected = false;
		Continent currentContinent;
		Country currentCountry = null;
		
		while (countrySelected == false) {
			currentContinent = null;
			for(int i = 0; i < gameMap.getSize(); i++){
				countryAvailable = false;
				for(int j = 0; j < gameMap.getContinent(j).getSize(); j++){
					if(gameMap.getContinent(i).checkCountriesOwned(null) > 0){
						countryAvailable = true;
					}
				}
				if(countryAvailable == true){
					countriesOwned = gameMap.getContinent(i).checkCountriesOwned(player);
					totalCountries = gameMap.getContinent(i).getSize();
					countryRatio = countriesOwned / totalCountries;
					if(countryRatio != 0.0 && countryRatio != 1.0){
						if(countryRatio >= largestRatio){
							currentContinent = gameMap.getContinent(i);
							largestRatio = countryRatio;
						}
						
					}
				}
			}
			
			if(currentContinent == null){
				index = strategyGenerator.nextInt(gameMap.getSize());
				currentContinent = gameMap.getContinent(index);
			}
			
			index = strategyGenerator.nextInt(currentContinent.getSize());
			currentCountry = currentContinent.getCountry(index);
			if (currentCountry.getOwner() == null) {
				countrySelected = true;
			}
		}
		return currentCountry.getName();
	}

	
	public String selectOwnedCountry(Player player, Map gameMap) {
		String selectedCountry = null;
		for(int i = 0; i < gameMap.getSize(); i++){
			for(int j = 0; j < gameMap.getContinent(i).getSize(); j++){
				if(gameMap.getContinent(i).getCountry(j).getOwner().equals(player)){
					ArrayList<String> borders = gameMap.getContinent(i).getCountry(j).getBorders();
					for(int k = 0; k < borders.size(); k++){
						if ((!(gameMap.getCountryOwner(borders.get(k)).equals(player)))
						   && (gameMap.getContinent(i).getCountry(j).getArmy() > 1)) {
							selectedCountry = gameMap.getContinent(i).getCountryName(j);
							break;
						}
					}
				}
			}
		}
		return selectedCountry;
	}
	
	public int moveArmies(String selectedCountry, Map gameMap){
		return gameMap.getCountryArmy(selectedCountry) - 1;
	}

	public String placeArmy(Player player, Map gameMap) {
		int index;
		boolean countrySelected = false;
		Continent currentContinent = null;
		Country currentCountry = null;
		while (countrySelected == false) {
			index = strategyGenerator.nextInt(gameMap.getSize());
			currentContinent = gameMap.getContinent(index);
			index = strategyGenerator.nextInt(currentContinent.getSize());
			currentCountry = currentContinent.getCountry(index);
			boolean bordersEnemy = false;
			ArrayList<String> borders = currentCountry.getBorders();
			for (int i = 0; i < borders.size(); ++i) {
				if (!(gameMap.getCountryOwner(borders.get(i))).equals(player)) {
					bordersEnemy = true;
				}
			}
			if ((currentCountry.getOwner() == player) && (bordersEnemy)) {
				countrySelected = true;
			}
		}
		return currentCountry.getName();
	}

	public String fortify(Player player, String selectedCountry, Map gameMap) {
		ArrayList<String> borderList;
		String fortifyCountry = null;
		borderList = gameMap.getCountryBorders(selectedCountry);
		for (int i = 0; i < borderList.size(); i++) {
			if (gameMap.getCountryOwner(borderList.get(i)) == player) {
				if (gameMap.getCountryArmy(borderList.get(i)) <= gameMap
						.getCountryArmy(selectedCountry)) {
					fortifyCountry = borderList.get(i);
				}
			}
		}
		return fortifyCountry;
	}

	public String attack(Player player, String selectedCountry, Map gameMap) {
		ArrayList<String> borderList;
		String attackCountry = null;
		borderList = gameMap.getCountryBorders(selectedCountry);
		for (int i = 0; i < borderList.size(); i++) {
			if (gameMap.getCountryOwner(borderList.get(i)) != player) {
				attackCountry = borderList.get(i);
				break;
			}
		}
		return attackCountry;
	}

	/**
	 * This method will return the computer's chosen set of cards
	 * to turn in for army bonuses.
	 */
	public ArrayList<Card> turnInCards(ArrayList<Card> playerHand) {
		int turnInCount = 0;
		int infantryCards = 0;
		int cavalryCards = 0;
		int artilleryCards = 0;
		int troopType = 0;
		int index = 0;
		ArrayList<Card> turnInCards = new ArrayList<Card>();

		for (int i = 0; i < playerHand.size(); i++) {
			if (playerHand.get(i).getTroopType() == 3) {
				turnInCards.add(playerHand.get(i));
				playerHand.remove(i);
				turnInCount++;
			}
		}

		for (int i = 0; i < playerHand.size(); i++) {
			if (playerHand.get(i).getTroopType() == 0) {
				infantryCards++;
			}
			if (playerHand.get(i).getTroopType() == 1) {
				cavalryCards++;
			}
			if (playerHand.get(i).getTroopType() == 2) {
				artilleryCards++;
			}
		}

		if (infantryCards >= cavalryCards && infantryCards >= artilleryCards) {
			troopType = 0;
		} else if (cavalryCards >= infantryCards
				&& cavalryCards >= artilleryCards) {
			troopType = 1;
		} else if (artilleryCards > infantryCards
				&& artilleryCards >= cavalryCards) {
			troopType = 2;
		}

		while (turnInCount < 3 && index < playerHand.size()) {
			if (playerHand.get(index).getTroopType() == troopType) {
				turnInCards.add(playerHand.get(index));
				playerHand.remove(index);
				turnInCount++;
			}
			index++;
		}
		return turnInCards;
	}

	/**
	 * This method will select the number of attack dice the
	 * computer will use based on it's selected attacking country's
	 * attacking army.
	 */
	public int chooseAttackDice(String selectedCountry, Map gameMap) {
		int selectedDice = 0;
		if (gameMap.getCountryArmy(selectedCountry) == 2) {
			selectedDice = 1;
		} else if (gameMap.getCountryArmy(selectedCountry) == 3) {
			selectedDice = 2;
		} else if (gameMap.getCountryArmy(selectedCountry) > 3) {
			selectedDice = 3;
		}
		return selectedDice;
	}

	/**
	 * This method will select the number of defending dice
	 * the computer will use based on it's number of defending
	 * armies.
	 */
	public int chooseDefendDice(String selectedCountry, Map gameMap) {
		int selectedDice = 0;
		if (gameMap.getCountryArmy(selectedCountry) == 1) {
			selectedDice = 1;
		} else if (gameMap.getCountryArmy(selectedCountry) > 1) {
			selectedDice = 2;
		}
		return selectedDice;
	}

	public boolean playAgain(Player player, Map gameMap) {
		boolean playAgain = false;
		for(int i = 0; i < gameMap.getSize(); i++){
			for(int j = 0; j < gameMap.getContinent(i).getSize(); j++){
				if(gameMap.getContinent(i).getCountry(j).getOwner().equals(player)){
					ArrayList<String> borders = gameMap.getContinent(i).getCountry(j).getBorders();
					for(int k = 0; k < borders.size(); k++){
						if ((!(gameMap.getCountryOwner(borders.get(k)).equals(player)))
						   && (gameMap.getContinent(i).getCountry(j).getArmy() > 1)) {
							playAgain = true;
							break;
						}
					}
				}
			}
		}
		return playAgain;
	}
}
