import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Manages the connection between the Property Management System
 * and the SQLite database.
 * Provides methods to load, add, update, and remove properties,
 * as well as check and save guest reservations.
 */
public class DatabaseManager {

    /**
     * Creates a DatabaseManager for handling SQLite operations.
     */
    public DatabaseManager() {
    }
   
    /**
     * Connects to an existing SQLite database using the provided file path.
     * Checks that the path is valid and the database file can be read.
     *
     * @param databasePath the path to the SQLite database file
     * @return a Connection object for the database
     * @throws SQLException if the path is invalid, the file cannot be
     *         accessed, or the database connection fails
     */
public Connection connect(String databasePath) throws SQLException {
    if (databasePath == null || databasePath.isBlank()) {
    throw new SQLException("Please enter a database file path.");
        }

    File databaseFile = new File(databasePath);

    if (!databaseFile.isFile() || !databaseFile.canRead()) {
    throw new SQLException("Database file not found or cannot be read.");}
    String url = "jdbc:sqlite:" + databasePath;
    return DriverManager.getConnection(url);
}

    /**
     * Retrieves all property records from the SQLite database.
     * Converts each database record into a Property object
     * and adds it to a list.
     *
     * @param databasePath the path to the SQLite database file
     * @return an ArrayList containing all properties in the database
     * @throws SQLException if a database error occurs
     */
public ArrayList<Property> getAllProperties(String databasePath) throws SQLException {

    ArrayList<Property> properties = new ArrayList<>();

    String sql = "SELECT * FROM properties";

    try (Connection connection = connect(databasePath);
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet results = statement.executeQuery()) {

        while (results.next()) {
            Property property = new Property(
                results.getInt("id"),
                results.getString("address"),
                results.getString("property_type"),
                results.getDouble("nightly_rate"),
                results.getInt("bedrooms"),
                results.getInt("available") == 1,
                results.getString("owner")
            );

            properties.add(property);
        }
    }

    return properties;
}
    /**
     * Adds a new property record to the SQLite database.
     * Uses a prepared SQL statement to save the property information.
     *
     * @param databasePath the path to the SQLite database file
     * @param property the Property object to save
     * @throws SQLException if the property cannot be saved
     *         or a database error occurs
     */
public void addProperty(String databasePath, Property property)
        throws SQLException {

    String sql = "INSERT INTO properties " +
        "(id, address, property_type, nightly_rate, bedrooms, available, owner) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = connect(databasePath);
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, property.getPropertyId());
        statement.setString(2, property.getAddress());
        statement.setString(3, property.getPropertyType());
        statement.setDouble(4, property.getNightlyRate());
        statement.setInt(5, property.getBedrooms());
        statement.setInt(6, property.isAvailable() ? 1 : 0);
        statement.setString(7, property.getOwner());

        statement.executeUpdate();
    }
}
    /**
     * Removes a property from the SQLite database.
     * Prevents deletion if the property has existing reservations.
     *
     * @param databasePath the path to the SQLite database file
     * @param propertyId the ID of the property to remove
     * @return true if the property was removed, otherwise false
     * @throws SQLException if a database error occurs
     */
public boolean removeProperty(String databasePath, int propertyId)
        throws SQLException {

    String sql = "DELETE FROM properties " +
    "WHERE id = ? " +
    "AND NOT EXISTS (" +
    "SELECT 1 FROM reservations WHERE property_id = ?" +
    ")";

    try (Connection connection = connect(databasePath);
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, propertyId);
        statement.setInt(2, propertyId);

        int rowsDeleted = statement.executeUpdate();

        return rowsDeleted > 0;
    }
}

    /**
     * Checks whether a property has any reservations
     * stored in the SQLite database.
     *
     * @param databasePath the path to the SQLite database file
     * @param propertyId the ID of the property to check
     * @return true if the property has reservations, otherwise false
     * @throws SQLException if a database error occurs
     */
public boolean hasReservations(String databasePath, int propertyId)
        throws SQLException {

    String sql = "SELECT COUNT(*) FROM reservations WHERE property_id = ?";

    try (Connection connection = connect(databasePath);
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, propertyId);

        try (ResultSet results = statement.executeQuery()) {
            return results.next() && results.getInt(1) > 0;
        }
    }
}

    /**
     * Updates an existing property in the SQLite database.
     * Saves the updated address, type, nightly rate,
     * bedrooms, availability, and owner information.
     *
     * @param databasePath the path to the SQLite database file
     * @param property the Property object containing updated information
     * @return true if the property was updated, otherwise false
     * @throws SQLException if a database error occurs
     */
public boolean updateProperty(String databasePath, Property property)
        throws SQLException {

    String sql = "UPDATE properties SET " +
        "address = ?, property_type = ?, nightly_rate = ?, " +
        "bedrooms = ?, available = ?, owner = ? " +
        "WHERE id = ?";

    try (Connection connection = connect(databasePath);
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, property.getAddress());
        statement.setString(2, property.getPropertyType());
        statement.setDouble(3, property.getNightlyRate());
        statement.setInt(4, property.getBedrooms());
        statement.setInt(5, property.isAvailable() ? 1 : 0);
        statement.setString(6, property.getOwner());
        statement.setInt(7, property.getPropertyId());

        int rowsUpdated = statement.executeUpdate();

        return rowsUpdated > 0;
    }

}   

    /**
     * Saves a new guest reservation to the SQLite database.
     * Checks that the property exists, is available, and does not
     * have another reservation with overlapping dates.
     *
     * @param databasePath the path to the SQLite database file
     * @param reservation the Reservation object to save
     * @return true if the reservation was saved, otherwise false
     * @throws SQLException if a database error occurs
     */
public boolean addReservation(String databasePath, Reservation reservation)
        throws SQLException {

    String sql = "INSERT INTO reservations " +
        "(property_id, guest_name, check_in, check_out, nightly_rate) " +
        "SELECT ?, ?, ?, ?, ? " +
        "WHERE EXISTS (" +
        "SELECT 1 FROM properties WHERE id = ? AND available = 1" +
        ") AND NOT EXISTS (" +
        "SELECT 1 FROM reservations " +
        "WHERE property_id = ? AND check_in < ? AND check_out > ?" +
        ")";

    try (Connection connection = connect(databasePath);
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, reservation.getPropertyId());
        statement.setString(2, reservation.getGuestName());
        statement.setString(3, reservation.getCheckIn().toString());
        statement.setString(4, reservation.getCheckOut().toString());
        statement.setDouble(5, reservation.getNightlyRate());

        statement.setInt(6, reservation.getPropertyId());
        statement.setInt(7, reservation.getPropertyId());
        statement.setString(8, reservation.getCheckOut().toString());
        statement.setString(9, reservation.getCheckIn().toString());

        return statement.executeUpdate() > 0;
    }
}

}
