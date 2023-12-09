import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The PriceAtlas class is a Java program that allows users to interact with a
 * database to perform
 * various queries and editing operations related to game prices and items.
 * 
 * @author Christian Ilog
 * @version 1.0
 * @since 12.05.23
 */
public class PriceAtlas {

	private static CLI cli;
	private static DBQueries cmd;
	private static DBEditMode editMode;
	public static boolean editModeBool;
	private static int gameSel;
	private static int itemSel;

	public static void main(String[] args) throws SQLException, Exception {
		Scanner scnr = new Scanner(System.in);
		int errFlag = -1;
		int userInput = 0;
		cmd = new DBQueries(scnr);
		editMode = new DBEditMode(cmd.getConnection());
		cli = new CLI();

		do {
			try {
				if (!editModeBool) {
					cli.printMainMenu();
					userInput = scnr.nextInt();
					System.out.println();
					userQueryInteraction(userInput, scnr);
				} else {
					cli.printEditDBMenu();
					userInput = scnr.nextInt();
					System.out.println();
					userEditDBInteraction(userInput, scnr);
				}

			} catch (InputMismatchException e) {
				System.out.println(
						"\nThere was a entry mismatch that occured during your query, returning you to the main menu...");
				scnr.nextLine();
				errFlag = 1;
			} catch (Exception e) {
				System.out.println("\nAn unexpected System error orrcured.");
				e.printStackTrace();
				cmd.closeConnection();
				errFlag = 0;
			} finally {
				if (errFlag == 0) {
					break;
				}
			}
		} while (userInput != 0);

		scnr.close();
	}

	/**
	 * The function `userQueryInteraction` handles user input and performs different
	 * actions based on the
	 * input.
	 * 
	 * @param userInput The userInput parameter is an integer that represents the
	 *                  user's choice for a
	 *                  specific query option. It is used to determine which action
	 *                  to perform based on the user's input.
	 * @param scnr      The parameter "scnr" is of type Scanner. It is used to read
	 *                  user input from the
	 *                  console.
	 */
	private static void userQueryInteraction(int userInput, Scanner scnr) throws SQLException {
		switch (userInput) {
			case 0: // Exit program
				cmd.closeConnection();
				break;
			case 1: // Return all the games in the Database
				cmd.getAllGames();
				break;
			case 2: // Return the items for a game within the database
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				cmd.getAllGameItems(gameSel);
				break;
			case 3: // Return the prices of items at all locations
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				cmd.getAllGameItems(gameSel);
				System.out.print("Please select a itemID from the list above (i.e. 1): ");
				itemSel = scnr.nextInt();
				cmd.specificItemCostLoc(itemSel, gameSel);
				break;
			case 4: // Returns the stats of of an item from a specific game
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				cmd.getAllGameItems(gameSel);
				System.out.print("Please select a itemID from the list above (i.e. 1): ");
				itemSel = scnr.nextInt();
				cmd.itemStats(itemSel, gameSel, itemSel);
				break;
			case 5: // Returns all items with a prices less than the given threshold
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				System.out.print("Please provide a maximum price threshold (i.e. 10000): ");
				float maxPrice = scnr.nextInt();
				cmd.itemsUnderPrice(gameSel, maxPrice);
				break;
			case 6: // Return the market capital of all the items being tracked for the game
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				cmd.getGameCapital(gameSel);
				break;
			case 7: // Initialize the switch to EditDBMode
				System.out.println("Initializing Database Edit Mode...");
				editModeBool = true;
				break;

			default:
				System.out.println("Unknown query option: " + userInput);
				break;
		}
	}

	/**
	 * The function `userEditDBInteraction` handles user input for various database
	 * editing operations
	 * such as adding a game, adding an item, adding a new price instance, editing a
	 * price instance,
	 * deleting a game, deleting an item, and deleting an item instance.
	 * 
	 * @param userInput The userInput parameter is an integer that represents the
	 *                  user's choice in the
	 *                  menu. It determines which action to perform in the database
	 *                  interaction.
	 * @param scnr      The parameter "scnr" is of type Scanner. It is used to read
	 *                  user input from the
	 *                  console.
	 */

	private static void userEditDBInteraction(int userInput, Scanner scnr) throws SQLException, Exception {
		String location;
		int stock;
		float price;
		String currency;
		switch (userInput) {
			case -1:
				System.out.println("Returning to Query menu...");
				editModeBool = false;
				break;
			case 1: // add a game
				scnr.nextLine();
				System.out.print("Enter Game Title: ");
				String gameTitle = scnr.nextLine();
				System.out.print("Enter Game Release Date (yyyy-mm-dd): ");
				String releaseDate = scnr.nextLine();
				System.out.print("Enter Game Developer: ");
				String developer = scnr.nextLine();
				editMode.addGame(gameTitle, releaseDate, developer);
				break;

			case 2: // add an item
				scnr.nextLine();
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				scnr.nextLine();
				System.out.print("Enter the Item Name: ");
				String itemName = scnr.nextLine();
				System.out.print("Enter the Item's Type (i.e. Ore): ");
				String itemType = scnr.nextLine();
				System.out.print("Enter an Item Description (max 64 characters): ");
				String descrip = scnr.nextLine();
				editMode.addItem(gameSel, itemName, itemType, descrip);
				break;

			case 3: // add a new price instance
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				cmd.getAllGameItems(gameSel);
				System.out.print(
						"Please select an ItemID from the List above (i.e. 1): ");
				itemSel = scnr.nextInt();
				scnr.nextLine();
				System.out.print("\nEnter the location: ");
				location = scnr.nextLine();

				System.out.print(
						"Enter the price value at " + location + "(i.e 1000.00 or 1000): ");
				price = scnr.nextFloat();
				scnr.nextLine();

				System.out.print("Enter the stock count at " + location + " (i.e. 10): ");
				stock = scnr.nextInt();
				scnr.nextLine();

				System.out.print("Enter the currency for this value (i.e. SC): ");
				currency = scnr.nextLine();
				editMode.addPriceInstance(itemSel, location, stock, price, currency);
				break;

			case 4: // edit a price instance
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				// allGameItemData(conn, gameSel);
				System.out.print("Please select an ItemID from the List above (i.e. 1): ");
				itemSel = scnr.nextInt();
				System.out.print("Enter the location: ");
				location = scnr.nextLine();
				System.out.print("Enter the price value at " + location + "(i.e 1000.00 or 1000): ");
				price = scnr.nextFloat();
				editMode.updateItemPrice(itemSel, price, location);
				break;

			case 5: // delete a game
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				editMode.deleteGame(gameSel);
				break;

			case 6: // delete an item
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				cmd.getAllGameItems(gameSel);
				System.out.print(
						"Please select an ItemID from the List above (i.e. 1): ");
				itemSel = scnr.nextInt();
				editMode.deleteItem(itemSel);
				break;

			case 7: // deleting an item instance
				cmd.getAllGames();
				System.out.print("Please select a gameID from the list above (i.e. 1): ");
				gameSel = scnr.nextInt();
				cmd.getAllGameItems(gameSel);
				System.out.print("Please select an ItemID from the List above (i.e. 1): ");
				itemSel = scnr.nextInt();
				scnr.nextLine();
				System.out.print("Enter the location: ");
				location = scnr.nextLine();
				editMode.deleteItemPrice(itemSel, location);
				break;
			default:
				break;
		}
	}
}
