import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
     * The function retrieves all games from a database and processes the data for
     * display.
     */
    public void getAllGames() throws SQLException {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM game");

            processDataForDisplay(resultSet);

            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function retrieves all game items associated with a specific game ID from
     * a database and
     * processes the data for display.
     * 
     * @param gameId The `gameId` parameter is an integer that represents the ID of
     *               the game for which
     *               we want to retrieve all the game items.
     */
    public void getAllGameItems(int gameId) {
        try {
            String query = "SELECT ITEM.ItemID, ITEM.ItemName FROM ITEM WHERE ITEM.RefGameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, gameId);

            ResultSet resultSet = preparedStatement.executeQuery();
            processDataForDisplay(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The specificItemCostLoc function retrieves the cost and location information
     * for a specific item
     * in a game.
     * 
     * @param itemID The itemID parameter is an integer that represents the ID of
     *               the specific item you
     *               want to retrieve information for.
     * @param gameID The gameID parameter is an integer that represents the ID of a
     *               game. It is used in
     *               the SQL query to filter the results based on the game ID.
     */
    public void specificItemCostLoc(int itemID, int gameID) {
        try {
            String query = "SELECT ITEM.ItemID, ITEM.ItemName, PRICE.PriceLocation, PRICE.Cost, PRICE.CurrencyType, PRICE.Stock "
                    +
                    "FROM ITEM " +
                    "LEFT JOIN STATCONTAINER ON STATCONTAINER.RefItemID = ITEM.ItemID " +
                    "LEFT JOIN PRICE ON PRICE.PriceContainerID = STATCONTAINER.ContainerID " +
                    "WHERE ITEM.ItemID = ? AND ITEM.RefGameID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setInt(2, gameID);

            ResultSet resultSet = preparedStatement.executeQuery();
            processDataForDisplay(resultSet);

            preparedStatement.close();
            resultSet.close();
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
     * @param gameID     The gameID parameter is used to specify the ID of the game
     *                   for which the item
     *                   stats are being retrieved.
     * @param instanceID The instanceID parameter is used to specify the unique
     *                   identifier of a
     *                   specific instance of the game. It is used in the SQL query
     *                   to filter the results and retrieve
     *                   the item statistics for that particular instance.
     */

    public void itemStats(int itemID, int gameID, int instanceID) {
        try {
            String query = "SELECT DISTINCT ITEM.ItemID, ITEM.ItemName, ITEM.ItemType, ITEM.ItemDescription, STATS.StatName, STATS.StatValue "
                    +
                    "FROM ITEM, STATCONTAINER, STATS " +
                    "WHERE ITEM.ItemID = ? AND ITEM.RefGameID = ? AND STATS.RefContainerID = ? AND STATCONTAINER.RefItemID = ITEM.ItemID";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setInt(2, gameID);
            preparedStatement.setInt(3, instanceID);

            ResultSet resultSet = preparedStatement.executeQuery();
            processDataForDisplay(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function retrieves items under a certain price for a specific game from a
     * database and
     * processes the data for display.
     * 
     * @param gameID   The gameID parameter is an integer that represents the ID of
     *                 a game. It is used to
     *                 filter the items based on the game they belong to.
     * @param maxPrice The maximum price that you want to filter the items by.
     */

    public void itemsUnderPrice(int gameID, float maxPrice) {
        try {
            String query = "SELECT PRICE.PriceLocation, PRICE.Stock, ITEM.ItemID, ITEM.ItemName, PRICE.Cost, PRICE.CurrencyType "
                    +
                    "FROM ITEM, STATCONTAINER, PRICE " +
                    "WHERE PRICE.PriceContainerID = STATCONTAINER.ContainerID " +
                    "AND STATCONTAINER.RefItemID = ITEM.ItemID " +
                    "AND PRICE.Cost < ?" +
                    "AND ITEM.RefGameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFloat(1, maxPrice);
            preparedStatement.setInt(2, gameID);

            ResultSet resultSet = preparedStatement.executeQuery();
            processDataForDisplay(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function retrieves the total cost and currency type of items associated
     * with a specific game
     * ID.
     * 
     * @param gameID The gameID parameter is an integer that represents the ID of a
     *               game. It is used in
     *               the SQL query to retrieve the total cost and currency type of
     *               items related to that game.
     */

    public void getGameCapital(int gameID) {
        try {
            String query = "SELECT SUM(PRICE.Cost * PRICE.Stock) AS TotalCost, PRICE.CurrencyType " +
                    "FROM ITEM, STATCONTAINER, PRICE " +
                    "WHERE ITEM.RefGameID = ? " +
                    "AND STATCONTAINER.RefItemID = ITEM.ItemID " +
                    "AND PRICE.PriceContainerID = STATCONTAINER.ContainerID " +
                    "GROUP BY PRICE.CurrencyType;";
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, gameID);
            ResultSet resultSet = preparedStatement.executeQuery();
            processDataForDisplay(resultSet);

            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processDataForDisplay(ResultSet rSet) throws SQLException {
        ResultSetMetaData metaData = rSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        int[] columnWidths = new int[columnCount];

        for (int i = 1; i <= columnCount; i++) {
            columnWidths[i - 1] = metaData.getColumnName(i).length();
        }

        while (rSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                int dataLength = rSet.getString(i).length();
                if (dataLength > columnWidths[i - 1]) {
                    columnWidths[i - 1] = dataLength;
                }
            }
        }

        cli.printTableHeader(metaData, columnWidths);
        rSet.beforeFirst();
        cli.printTableData(rSet, columnCount, columnWidths);
    }

}