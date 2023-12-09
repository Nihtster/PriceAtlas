import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

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

	public void printTableHeader(ResultSetMetaData metaData, int[] columnWidths, List<String> columnsToPrint)
			throws SQLException {
		StringBuilder headerFormat = new StringBuilder("|");

		printSeperators(columnWidths);

		if (columnsToPrint != null && !columnsToPrint.isEmpty()) {
			for (int i = 1; i <= columnWidths.length; i++) {
				String columnName = metaData.getColumnName(i);
				if (columnsToPrint.contains(columnName)) {
					headerFormat.append(String.format(" %-" + (columnWidths[i - 1]) + "s |", columnName));
				}
			}
		} else {
			for (int i = 1; i <= columnWidths.length; i++) {
				String columnName = metaData.getColumnName(i);
				headerFormat.append(String.format(" %-" + (columnWidths[i - 1]) + "s |", columnName));
			}
			System.out.println(headerFormat.toString());
		}
		printSeperators(columnWidths);
	}

	public void printTableData(ResultSet resultSet, int columnCount, int[] columnWidths, List<String> columnsToPrint)
			throws SQLException {
		StringBuilder rowFormat = new StringBuilder("|");

		for (int i = 1; i <= columnWidths.length; i++) {
			rowFormat.append(" %-" + columnWidths[i - 1] + "s |");
		}

		while (resultSet.next()) {
			Object[] rowData = new Object[columnCount];

			if (columnsToPrint == null || columnsToPrint.isEmpty()) {
				for (int i = 1; i <= columnCount; i++) {
					rowData[i - 1] = resultSet.getString(i);
				}
				System.out.printf(rowFormat.toString(), rowData);
			} else {
				for (String columnName : columnsToPrint) {
					for (int i = 1; i <= columnCount; i++) {
						if (columnName.equals(resultSet.getMetaData().getColumnName(i))) {
							rowData[i - 1] = resultSet.getString(i);
						}
					}
				}
				System.out.printf(rowFormat.toString(), rowData);
			}

			System.out.println();
		}

		printSeperators(columnWidths);
		System.out.println();
	}

	public void printSeperators(int[] columnWidths) {
		for (int i = 1; i <= columnWidths.length; i++) {
			System.out.print("+");
			for (int j = 0; j < columnWidths[i - 1] + 2; j++) {
				System.out.print("-");
			}
		}
		System.out.println("+");
	}

}
