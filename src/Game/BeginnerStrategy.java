package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BeginnerStrategy implements ComputerStrategy, Serializable {

	private static final long serialVersionUID = -5322553174403567250L;
	private Random strategyGenerator = new Random();

	public String chooseCountry(Player player, Map gameMap) {
		int index;
		boolean countrySelected = false;
		Continent currentContinent = null;
		Country currentCountry = null;
		while (countrySelected == false) {
			index = strategyGenerator.nextInt(gameMap.getSize());
			currentContinent = gameMap.getContinent(index);
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

	public int chooseAttackDice(String selectedCountry, Map gameMap) {
		int selectedDice = 0;
		if (gameMap.getCountryArmy(selectedCountry) == 2) {
			selectedDice = 1;
		} else if (gameMap.getCountryArmy(selectedCountry) == 3) {
			selectedDice = (strategyGenerator.nextInt(1) + 1);
		} else if (gameMap.getCountryArmy(selectedCountry) > 3) {
			selectedDice = (strategyGenerator.nextInt(2) + 1);
		}
		return selectedDice;
	}

	public int chooseDefendDice(String selectedCountry, Map gameMap) {
		int selectedDice = 0;
		if (gameMap.getCountryArmy(selectedCountry) == 1) {
			selectedDice = 1;
		} else if (gameMap.getCountryArmy(selectedCountry) > 1) {
			selectedDice = (strategyGenerator.nextInt(1) + 1);
		}
		return selectedDice;
	}

	public boolean playAgain(Player player, Map gameMap) {

		boolean playAgain = false;
		int choice = strategyGenerator.nextInt(1);
		if (choice == 0) {
			return playAgain;
		}
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
