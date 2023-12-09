import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * The DBQueries class is a Java class that handles various database queries and
 * operations.
 * 
 * @author Christian Ilog
 * @version 1.0
 * @since 12.05.23
 */
public class DBQueries {
    private Connection connection;
    private CLI cli;

    // The above code is a constructor for a class called DBQueries. It takes a
    // Scanner object as a
    // parameter.
    public DBQueries(Scanner scnr) throws SQLException, Exception {

        do {
            System.out.print("Enter DB URL: ");
            String URL = scnr.nextLine();
            System.out.print("Enter Username: ");
            String USER = scnr.nextLine();
            System.out.print("Enter Password: ");
            String PASSWORD = scnr.nextLine();
            System.out.print("Enter the JDBC Driver: ");
            String DRIVER = scnr.nextLine();

            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (Exception e) {
                System.out.println("\nConnection unsuccessful, please retry the connection.\n");
            } finally {
                if (connection != null)
                    break;
            }
        } while (true);

        System.out.println("\nConnection to Database Successful!");
        cli = new CLI();
    }

    /**
     * The function returns a connection object.
     * 
     * @return The method is returning a Connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * The closeConnection() function closes the connection and prints a message
     * before shutting down.
     */
    public void closeConnection() {
        try {
            System.out.println("Closing connection and shutting down...\n");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function getAllGames retrieves all game records from a database and
     * prints them in a
     * formatted table.
     */
    public void getAllGames() throws SQLException {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM game");

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int[] columnWidths = new int[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columnWidths[i - 1] = metaData.getColumnName(i).length();
            }

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    int dataLength = resultSet.getString(i).length();
                    if (dataLength > columnWidths[i - 1]) {
                        columnWidths[i - 1] = dataLength;
                    }
                }
            }

            cli.printTableHeader(metaData, columnWidths, null);
            resultSet.beforeFirst();
            cli.printTableData(resultSet, columnCount, columnWidths, null);

            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function retrieves all game items associated with a specific game ID from
     * a database and prints them in a formatted table.
     * 
     * @param gameId The `gameId` parameter is an integer that represents the ID of
     *               the game for which
     *               you want to retrieve all the game items.
     */
    public void getAllGameItems(int gameId) {
        try {
            String query = "SELECT * FROM ITEM WHERE ITEM.RefGameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, gameId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int[] columnWidths = new int[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columnWidths[i - 1] = metaData.getColumnName(i).length();
            }

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    int dataLength = resultSet.getString(i).length();
                    if (dataLength > columnWidths[i - 1]) {
                        columnWidths[i - 1] = dataLength;
                    }
                }
            }

            cli.printTableHeader(metaData, columnWidths, null);
            resultSet.beforeFirst();
            cli.printTableData(resultSet, columnCount, columnWidths, null);

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The specificItemCostLoc function retrieves and displays the cost and location
     * information for a
     * specific item in a game.
     * 
     * @param itemID The itemID parameter is used to specify the ID of the item you
     *               want to retrieve
     *               the cost for.
     * @param gameID The gameID parameter is used to specify the ID of the game for
     *               which you want to
     *               retrieve the specific item cost and location.
     */
    public void specificItemCostLoc(int itemID, int gameID) {
        try {
            String query = "SELECT * " +
                    "FROM ITEM " +
                    "LEFT JOIN STATCONTAINER ON STATCONTAINER.RefItemID = ITEM.ItemID " +
                    "LEFT JOIN PRICE ON PRICE.PriceContainerID = STATCONTAINER.ContainerID " +
                    "WHERE ITEM.ItemID = ? AND ITEM.RefGameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setInt(2, gameID);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> columnsToPrint = Arrays.asList("ItemID", "ItemName");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int[] columnWidths = new int[columnsToPrint.size()];

            // Specify the columns you want to print

            // Initialize column widths with specified column names length
            int counter = 0;
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                if (columnsToPrint.contains(columnName)) {
                    columnWidths[counter] = columnName.length();
                    counter++;
                }
            }

            counter = 0;
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    if (columnsToPrint.contains(columnName)) {
                        int dataLength = resultSet.getString(i).length();
                        if (dataLength > columnWidths[counter]) {
                            columnWidths[counter] = dataLength;
                            counter++;
                        }
                    }
                }
            }

            cli.printTableHeader(metaData, columnWidths, columnsToPrint);
            resultSet.beforeFirst();
            cli.printTableData(resultSet, columnCount, columnWidths, columnsToPrint);

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The itemStats function retrieves and displays the statistics of a specific
     * item in a game.
     * 
     * @param itemID     The itemID parameter is used to specify the ID of the item
     *                   for which you want to
     *                   retrieve the statistics.
     * @param gameID     The gameID parameter is used to specify the reference game
     *                   ID for the items you
     *                   want to retrieve stats for.
     * @param instanceID The instanceID parameter is used to specify the ID of the
     *                   stat container
     *                   instance. It is used in the SQL query to filter the results
     *                   and retrieve the stats associated
     *                   with a specific instance of an item.
     */
    public void itemStats(int itemID, int gameID, int instanceID) {
        try {
            String query = "SELECT DISTINCT * " +
                    "FROM ITEM, STATCONTAINER, STATS " +
                    "WHERE ITEM.ItemID = ? AND ITEM.RefGameID = ? AND STATS.RefContainerID = ? AND STATCONTAINER.RefItemID = ITEM.ItemID";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setInt(2, gameID);
            preparedStatement.setInt(3, instanceID);

            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int[] columnWidths = new int[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                columnWidths[i - 1] = metaData.getColumnName(i).length();
            }

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    int dataLength = resultSet.getString(i).length();
                    if (dataLength > columnWidths[i - 1]) {
                        columnWidths[i - 1] = dataLength;
                    }
                }
            }

            cli.printTableHeader(metaData, columnWidths, null);
            resultSet.beforeFirst();
            cli.printTableData(resultSet, columnCount, columnWidths, null);

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function retrieves items under a certain price for a given game ID from a
     * database and
     * prints the results.
     * 
     * @param gameID   The gameID parameter is an integer that represents the ID of
     *                 the game for which
     *                 you want to find items under a certain price.
     * @param maxPrice The maximum price that you want to filter the items by.
     */
    public void itemsUnderPrice(int gameID, float maxPrice) {
        try {
            String query = "SELECT PRICE.PriceLocation, ITEM.ItemName, PRICE.Stock, PRICE.Cost, PRICE.CurrencyType " +
                    "FROM ITEM, STATCONTAINER, PRICE " +
                    "WHERE PRICE.PriceContainerID = STATCONTAINER.ContainerID " +
                    "AND STATCONTAINER.RefItemID = ITEM.ItemID " +
                    "AND PRICE.Cost < ?" +
                    "AND ITEM.RefGameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFloat(1, maxPrice);
            preparedStatement.setInt(2, gameID);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println(
                    "+-----------------------------------------------------------------------------------------------------------------------+");
            System.out.printf("| %-20s %-40s %-20s %-20s %-5s |\n", "Price Location", "Item Name", "Stock", "Cost",
                    "Currency Type");
            System.out.println(
                    "+-----------------------------------------------------------------------------------------------------------------------+");

            while (resultSet.next()) {
                System.out.printf("| %-20s %-40s %-20d %-20.2f %-13s |\n",
                        resultSet.getString("PriceLocation"),
                        resultSet.getString("ItemName"),
                        resultSet.getInt("Stock"),
                        resultSet.getFloat("Cost"),
                        resultSet.getString("CurrencyType"));
            }
            System.out.println(
                    "+-----------------------------------------------------------------------------------------------------------------------+");

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function retrieves the market capital and currency type for a given game
     * ID by querying the
     * database.
     * 
     * @param gameID The gameID parameter is an integer that represents the ID of a
     *               game.
     */
    public void getGameCapital(int gameID) {
        try {
            String query = "SELECT SUM(PRICE.Cost * PRICE.Stock) AS TotalCost, PRICE.CurrencyType " +
                    "FROM ITEM, STATCONTAINER, PRICE " +
                    "WHERE ITEM.RefGameID = ? " +
                    "AND STATCONTAINER.RefItemID = ITEM.ItemID " +
                    "AND PRICE.PriceContainerID = STATCONTAINER.ContainerID " +
                    "GROUP BY PRICE.CurrencyType;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, gameID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("\n+--------------------------+");
                    System.out.printf("| %-15s %s |\n", "Market Capital", "Currency");
                    System.out.println("+--------------------------+");

                    while (resultSet.next()) {
                        float totalCost = resultSet.getFloat("TotalCost");
                        String currency = resultSet.getString("CurrencyType");

                        System.out.printf("| %-15.2f %-8s |\n", totalCost, currency);
                    }
                    System.out.println("+--------------------------+");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}