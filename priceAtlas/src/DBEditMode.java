import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The DBEditMode class provides methods for adding, updating, and deleting
 * game, item, and price
 * information in a database.
 * 
 * @author Christian Ilog
 * @version 1.0
 * @since 12.05.23
 */

public class DBEditMode {
    private Connection connection = null;
    private int highestGameID;
    private int highestItemID;

    public DBEditMode(Connection conn) throws SQLException, Exception {
        connection = conn;
    }

    /**
     * The function retrieves the highest GameID from the GAME table in a database.
     * 
     * @return The method is returning the highest game ID from the GAME table.
     */
    private int getHighestGameID() {
        try {
            String query = "SELECT MAX(GameID) AS MaxGameID FROM GAME";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                highestGameID = resultSet.getInt("MaxGameID");
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return highestGameID;
    }

    /**
     * The function retrieves the highest ItemID from a database table named ITEM.
     * 
     * @return The method is returning the highest ItemID from the ITEM table.
     */
    private int getHighestItemID() {
        try {
            String query = "SELECT MAX(ItemID) AS MaxItemID FROM ITEM";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                highestItemID = resultSet.getInt("MaxItemID");
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return highestItemID;
    }

    /**
     * The addGame function inserts a new game into a database table with the
     * provided title, release
     * date, and developer.
     * 
     * @param title       The title of the game that you want to add to the
     *                    database.
     * @param releaseDate The releaseDate parameter is a String that represents the
     *                    release date of the
     *                    game.
     * @param developer   The developer parameter is a String that represents the
     *                    name of the developer of
     *                    the game.
     */
    public void addGame(String title, String releaseDate, String developer)
            throws SQLException, Exception {
        try {
            String query = "INSERT INTO GAME (GameID, Title, ReleaseDate, Developer) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            highestGameID = getHighestGameID();
            preparedStatement.setInt(1, highestGameID + 1);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, releaseDate);
            preparedStatement.setString(4, developer);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nGame added successfully.");
            } else {
                System.out.println("\nFailed to add game.");
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function `addItem` inserts a new item into a database table with the
     * provided game ID, item
     * name, item type, and item description.
     * 
     * @param gameID          The gameID parameter is an integer that represents the
     *                        ID of the game to which the
     *                        item belongs.
     * @param itemName        The name of the item that you want to add to the
     *                        database.
     * @param itemType        The itemType parameter is a String that represents the
     *                        type of the item being
     *                        added. It could be something like "weapon", "armor",
     *                        "consumable", etc.
     * @param itemDescription The itemDescription parameter is a String that
     *                        represents the description
     *                        of the item being added. It provides additional
     *                        information or details about the item.
     */
    public void addItem(int gameID, String itemName, String itemType, String itemDescription) {
        try {
            String query = "INSERT INTO ITEM (ItemID, ItemName, ItemType, ItemDescription, RefGameID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            highestItemID = getHighestItemID();
            preparedStatement.setInt(1, highestItemID + 1);
            preparedStatement.setString(2, itemName);
            preparedStatement.setString(3, itemType);
            preparedStatement.setString(4, itemDescription);
            preparedStatement.setInt(5, gameID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item added successfully.");
            } else {
                System.out.println("Failed to add item.");
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function `addPriceInstance` inserts a new price instance into the
     * database for a given item
     * ID, location, stock count, price value, and currency.
     * 
     * @param itemID   The itemID parameter is an integer that represents the unique
     *                 identifier for the
     *                 item.
     * @param location The location where the price instance is being added.
     * @param stockCnt The stock count of the item at the specified location.
     * @param priceVal The price value of the item.
     * @param currency The currency parameter is a String that represents the type
     *                 of currency for the
     *                 price value. For example, it could be "USD" for US dollars,
     *                 "EUR" for euros, or "JPY" for
     *                 Japanese yen.
     */
    public void addPriceInstance(int itemID, String location, int stockCnt, double priceVal,
            String currency) throws SQLException, Exception {

        String query1 = "INSERT INTO statcontainer (ContainerID, RefItemID) VALUES (?, ?)";
        PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
        preparedStatement1.setInt(1, itemID);
        preparedStatement1.setInt(2, itemID);

        preparedStatement1.executeUpdate();

        try {
            String query = "INSERT INTO PRICE (PriceLocation, stock, Cost, CurrencyType, PriceContainerID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(5, itemID);
            preparedStatement.setString(1, location);
            preparedStatement.setInt(2, stockCnt);
            preparedStatement.setDouble(3, priceVal);
            preparedStatement.setString(4, currency);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Added a New Price Instance for " + itemID + "at " + location);
            } else {
                System.out.println(
                        "Failed to add a new Price instance.");
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The function updates the price of an item in a database table based on the
     * item ID and location.
     * 
     * @param itemID       The itemID parameter is an integer that represents the
     *                     unique identifier of the
     *                     item whose price needs to be updated.
     * @param newCost      The new cost of the item that you want to update.
     * @param itemLocation The itemLocation parameter is a String that represents
     *                     the location of the
     *                     item. It is used to identify the specific item in the
     *                     database that needs to have its price
     *                     updated.
     */
    public void updateItemPrice(int itemID, float newCost, String itemLocation) {
        try {

            String query = "UPDATE PRICE SET Cost = ? WHERE PriceContainerID = ? AND PriceLocation = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFloat(1, newCost);
            preparedStatement.setInt(2, itemID);
            preparedStatement.setString(3, itemLocation);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item price updated successfully.");
            } else {
                System.out.println("Failed to update item price. Check if the item ID and location exist.");
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The deleteGame function deletes a game and all items tied to it from the
     * database based on the
     * provided game ID.
     * 
     * @param gameID The gameID parameter is an integer that represents the unique
     *               identifier of the
     *               game that needs to be deleted from the database.
     */
    public void deleteGame(int gameID) {
        try {
            String query = "DELETE FROM GAME WHERE GameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, gameID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(
                        "\nGame and all items tied to the game have been Successfully deleted from the Database.");
            } else {
                System.out.println("\nFailed to delete game. Check if the game ID exists.");
            }

            highestGameID = getHighestGameID();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The deleteItem function deletes an item and all its instances from the
     * database based on the
     * provided item ID.
     * 
     * @param itemID The itemID parameter is an integer that represents the unique
     *               identifier of the
     *               item that needs to be deleted from the database.
     */
    public void deleteItem(int itemID) {
        try {
            String query = "DELETE FROM ITEM WHERE ItemID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, itemID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nItem and all item instances have been successuflly deleted from the Database.");

            } else {
                System.out.println("\nFailed to delete item. Check if the item ID exists.");
            }

            highestItemID = getHighestGameID();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The deleteItemPrice function deletes a specific item price instance from the
     * PRICE table in a
     * database based on the item ID and location provided.
     * 
     * @param itemID   The itemID parameter is an integer that represents the unique
     *                 identifier of the
     *                 item whose price instance needs to be deleted.
     * @param location The "location" parameter is a string that represents the
     *                 location of the item
     *                 price instance that you want to delete.
     */
    public void deleteItemPrice(int itemID, String location) {
        try {
            String query = "DELETE FROM PRICE WHERE PriceContainerID = ? AND PriceLocation = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setString(2, location);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item price instance deleted successfully.");
            } else {
                System.out.println("Failed to Delete item price. Check if the item ID and location exist.");
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
