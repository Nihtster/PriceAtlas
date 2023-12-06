public class CLI {

	public void printMainMenu() {
		String qList[] = { "Exit Program", "List All Games", "List All Items in a Game", "Get All Instances of an Item",
				"Get All Stats of an Item", "Get Market Items", "Get Market Capital", "Edit DB Elements" };

		System.out.println("\n+------------------------------------+");
		System.out.println("|              Query Menu            |");
		System.out.println("+------------------------------------+");

		for (int index = 0; index < qList.length; index++) {
			System.out.printf("| %d: %-32s|\n", index, qList[index]);
		}
		System.out.println("+------------------------------------+\n");

		System.out.print("Please select a DB query option from the list above (i.e. 1): ");
	}

	public void printEditDBMenu() {
		String eList[] = { "Add Game", "Add Item", "Add a New Price Instance", "Update an Instance Price",
				"Delete Game", "Delete Item", "Delete Price Instance" };
		System.out.println("\n+--------------------------------------+");
		System.out.println("|              Edit DB Menu            |");
		System.out.println("+--------------------------------------+");
		for (int index = 0; index < eList.length; index++) {
			System.out.printf("| %d: %-33s |\n", index + 1, eList[index]);
		}
		System.out.printf("|%-37s |\n", "-1: Return to Query Menu ");
		System.out.println("+--------------------------------------+\n");
		System.out.print("Please select an DB edit option from the list above (i.e. 2): ");
	}
}
