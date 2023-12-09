import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CLI {

	/**
	 * The `printMenu` function prints a menu with a given name, list of items, and
	 * an option to enable
	 * edit mode.
	 * 
	 * @param menuName The name of the menu that will be displayed at the top.
	 * @param items    An array of strings representing the menu items to be
	 *                 displayed.
	 * @param editMode The `editMode` parameter is a boolean value that determines
	 *                 whether the menu is in
	 *                 edit mode or not. If `editMode` is `true`, it means that the
	 *                 menu is in edit mode and additional
	 *                 information will be displayed for each item in the menu. If
	 *                 `editMode` is `
	 */
	private void printMenu(String menuName, String[] items, boolean editMode) {
		int maxItemLength = 0;
		int menuWidth = 0;
		int leftMenuPadding = 0;
		int rightMenuPadding = 0;

		for (String string : items) {
			if (maxItemLength < string.length())
				maxItemLength = string.length();
		}
		maxItemLength += 6; // +6 to account for |, colon(:), spaces(" "), and negitive one(-1)

		menuWidth = Math.max(maxItemLength, menuName.length());
		leftMenuPadding = (menuWidth - menuName.length()) / 2;
		rightMenuPadding = menuWidth - menuName.length() - leftMenuPadding;

		System.out.println("\n+" + "-".repeat(menuWidth) + "+");
		System.out.println("|" + " ".repeat(leftMenuPadding) + menuName + " ".repeat(rightMenuPadding) + "|");
		System.out.println("+" + "-".repeat(menuWidth) + "+");

		for (int index = 0; index < items.length; index++) {
			if (editMode) {
				System.out.printf("| %d: %-" + (maxItemLength - 6) + "s |\n", index - 1, items[index]);
				editMode = false;
			} else
				System.out.printf("| %d: %-" + (maxItemLength - 5) + "s |\n", index, items[index]);
		}
		System.out.println("+" + "-".repeat(menuWidth) + "+\n");
		System.out.printf("Please select a %s option from the list above (i.e. 1): ", menuName);
	}

	/**
	 * The function "printMainMenu" prints a menu with various options for querying
	 * a game database.
	 */
	public void printMainMenu() {
		String qList[] = {
				"Exit Program",
				"List All Games",
				"List All Game Items",
				"List All Price Locations of an Item",
				"Get All Stats of an Item",
				"Get Market Items",
				"Get Market Capital",
				"Edit the Database"
		};
		printMenu("Query Menu", qList, false);
	}

	/**
	 * The function "printEditDBMenu" prints a menu with options for editing a
	 * database.
	 */
	public void printEditDBMenu() {
		String eList[] = {
				"Return to Query Menu",
				"Add Game",
				"Add Item",
				"Add a New Price Instance",
				"Update an Instance Price",
				"Delete Game",
				"Delete Item",
				"Delete Price Instance"
		};
		printMenu("Edit DB Menu", eList, true);
	}

	public void printTableHeader(ResultSetMetaData metaData, int[] columnWidths)
			throws SQLException {
		StringBuilder headerFormat = new StringBuilder("|");

		System.out.println("");
		printSeperators(columnWidths);

		for (int i = 1; i <= columnWidths.length; i++) {
			String columnName = metaData.getColumnName(i);
			headerFormat.append(String.format(" %-" + (columnWidths[i - 1]) + "s |", columnName));
		}
		System.out.println(headerFormat.toString());
		printSeperators(columnWidths);
	}

	public void printTableData(ResultSet resultSet, int columnCount, int[] columnWidths) throws SQLException {
		StringBuilder rowFormat = new StringBuilder("|");

		for (int i = 1; i <= columnWidths.length; i++) {
			rowFormat.append(" %-" + columnWidths[i - 1] + "s |");
		}

		while (resultSet.next()) {
			Object[] rowData = new Object[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				rowData[i - 1] = resultSet.getString(i);
			}

			System.out.printf(rowFormat.toString(), rowData);
			System.out.println();
		}
		printSeperators(columnWidths);
		System.out.println();
	}

	private void printSeperators(int[] columnWidths) {
		for (int i = 1; i <= columnWidths.length; i++) {
			System.out.print("+");
			for (int j = 0; j < columnWidths[i - 1] + 2; j++) {
				System.out.print("-");
			}
		}
		System.out.println("+");
	}
}
