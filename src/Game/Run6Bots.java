package Game;

public class Run6Bots {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int startingArmies;
		int bonusArmies;
		int attackDice;
		int defendDice;
		boolean hasWinner = false;
		Player defendingPlayer;
		String chosenCountry = new String();
		String attackCountry = new String();
		String defendCountry = new String();
		String fortifyCountry = new String();
		GameBoard board = new GameBoard();
		board.loadCardDeck("riskcards.txt");
		board.loadMap("riskmap.txt");
		board.addPlayer(new ComputerPlayer("Comp 1", new IntermediateStrategy()));
		board.addPlayer(new ComputerPlayer("Comp 2", new IntermediateStrategy()));
		board.addPlayer(new ComputerPlayer("Comp 3", new IntermediateStrategy()));
		board.addPlayer(new ComputerPlayer("Comp 4", new IntermediateStrategy()));
		board.addPlayer(new ComputerPlayer("Comp 5", new IntermediateStrategy()));
		board.addPlayer(new ComputerPlayer("Comp 6", new IntermediateStrategy()));
		startingArmies = board.startingArmies();		
		
		while(board.isInitialized() == false){
			chosenCountry =((ComputerPlayer)board.currentPlayer()).chooseCountry(board.getMap());
			board.setCountryOwner(chosenCountry, board.currentPlayer());
			board.placeArmies(chosenCountry, 1);
			System.out.println(chosenCountry + " was chosen by " + board.currentPlayer().getName());
			startingArmies --;
			board.nextPlayer();
		}
		System.out.println("+++ Initialization Phase Done +++");
		
		while(startingArmies != 0){
			chosenCountry = ((ComputerPlayer)board.currentPlayer()).placeArmy(board.getMap());
			board.placeArmies(chosenCountry, 1);
			System.out.println("Army placed on " + chosenCountry + " by " + board.currentPlayer().getName());
			startingArmies--;
			board.nextPlayer();
		}
		System.out.println("+++ Placing Armies Phase Done +++");
			    
		while(hasWinner == false){
			bonusArmies = 0;
			bonusArmies = board.calculateArmies(board.currentPlayer());
			if(((ComputerPlayer)board.currentPlayer()).checkForSet() == true){
				board.turnInSet(((ComputerPlayer)board.currentPlayer()).turnInCards());
				bonusArmies += board.armiesForTurnIn();
			}
			
			while(bonusArmies != 0){
				chosenCountry = ((ComputerPlayer)board.currentPlayer()).placeArmy(board.getMap());
				board.placeArmies(chosenCountry, 1);
				System.out.println("Army placed on " + chosenCountry + " by " + board.currentPlayer().getName());
				bonusArmies--;
			}
			System.out.println("+++ Attack Test Phase +++");
			
			while(((ComputerPlayer) board.currentPlayer()).playAgain(board.currentPlayer(), board.getMap())){
				attackCountry = new String();
				defendCountry = new String();
				attackCountry = ((ComputerPlayer)board.currentPlayer()).selectOwnedCountry(board.getMap());
				if(attackCountry != null){
					System.out.print(board.getMap().getCountryOwner(attackCountry).getName() + " selects " + attackCountry);
					System.out.println(" with " + board.getMap().getCountryArmy(attackCountry) + " armies");
					defendCountry = ((ComputerPlayer)board.currentPlayer()).attack(attackCountry, board.getMap());
					if(defendCountry != null){
						System.out.print(board.getMap().getCountryOwner(attackCountry).getName() + " attacks ");
						System.out.println(board.getMap().getCountryOwner(defendCountry).getName() + "'s "+ defendCountry
								          + " defending with " + board.getMap().getCountryArmy(defendCountry) + " armies");
						attackDice = ((ComputerPlayer)board.currentPlayer()).chooseAttackDice(attackCountry, board.getMap());
						System.out.println(board.getMap().getCountryOwner(attackCountry).getName() + " rolls " + attackDice + " dice");
						defendingPlayer = board.getMap().getCountryOwner(defendCountry);
						defendDice = ((ComputerPlayer)defendingPlayer).chooseDefendDice(defendCountry, board.getMap());
						System.out.println(board.getMap().getCountryOwner(defendCountry).getName() + " rolls " + defendDice + " dice");
						board.rollDice(attackCountry, attackDice, defendCountry, defendDice);
						System.out.println(attackCountry + " now has " + board.getMap().getCountryArmy(attackCountry) + " armies");
						System.out.println(defendCountry + " now has " + board.getMap().getCountryArmy(defendCountry) + " armies");
					}
				}
			}
			
			System.out.println("+++ Attack Test Phase Done +++");
			
			System.out.println("+++ Fortify Test Phase +++");
			chosenCountry = ((ComputerPlayer)board.currentPlayer()).selectOwnedCountry(board.getMap());
			System.out.println(chosenCountry);
			if(chosenCountry != null){
				fortifyCountry = ((ComputerPlayer)board.currentPlayer()).fortify(chosenCountry, board.getMap());
				System.out.println(fortifyCountry);
				if(fortifyCountry != null){
					board.fortifyCountry(chosenCountry, fortifyCountry,
							            ((ComputerPlayer)board.currentPlayer()).moveArmies(chosenCountry, board.getMap()));
				}
			}
			System.out.println("+++ Fortify Test Phase Done +++");

			if(board.hasDefeatedCountry() == true){
				board.dealCard(board.currentPlayer());
			}
			
			if(board.isWinner(board.currentPlayer()) == false){
				board.nextPlayer();
			}else{
				hasWinner = true;
			}
		}
		System.out.println(board.currentPlayer().getName() + " wins!");
		System.out.println("Done!");
	}
}



