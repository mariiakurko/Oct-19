import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Manages properties and reservations in the Property Management System.
 * Provides methods to add, find, remove, and display properties,
 * as well as create and display reservations.
 */
public class PropertyManager {

    // Lists to store properties and reservations
    private ArrayList<Property> properties;
    private ArrayList<Reservation> reservations;

    /**
     * Creates a new PropertyManager with empty lists
     * for properties and reservations.
     */
    public PropertyManager() {
        properties = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    /**
     * Adds a new property to the list.
     * Checks that the property information is valid
     * and that the property ID is not already being used.
     *
     * @param property the property to add
     * @return true if the property was added, otherwise false
     */
    public boolean addProperty(Property property) {

        if (property == null) {
            return false;
        }

        if (property.getPropertyId() <= 0 ||
                property.getAddress() == null ||
                property.getAddress().isBlank() ||
                property.getPropertyType() == null ||
                property.getPropertyType().isBlank() ||
                !Double.isFinite(property.getNightlyRate()) ||
                property.getNightlyRate() <= 0 ||
                property.getBedrooms() < 0 ||
                property.getOwner() == null ||
                property.getOwner().isBlank()) {
            return false;
        }

        // Check for duplicate IDs
        if (findProperty(property.getPropertyId()) != null) {
            return false;
        }

        properties.add(property);
        return true;
    }

    /**
     * Searches for a property using its ID.
     *
     * @param propertyId the ID of the property to find
     * @return the matching Property object, or null if not found
     */
    public Property findProperty(int propertyId) {

        for (Property property : properties) {
            if (property.getPropertyId() == propertyId) {
                return property;
            }
        }

        return null;
    }

    /**
     * Removes a property from the list.
     *
     * @param propertyId the ID of the property to remove
     * @return true if the property was removed, otherwise false
     */
    public boolean removeProperty(int propertyId) {

        Property property = findProperty(propertyId);

        if (property == null) {
            return false;
        }

        // Do not remove a property with reservations
        for (Reservation reservation : reservations) {
            if (reservation.getPropertyId() == propertyId) {
                return false;
            }
        }

        properties.remove(property);
        return true;
    }

    /**
     * Displays information about all properties in the list.
     * Returns a message if no properties are available.
     *
     * @return a string containing all property information,
     *         or a message if the list is empty
     */
    public String displayProperties() {

        if (properties.isEmpty()) {
            return "No properties found.";
        }

        String result = "";

        for (Property property : properties) {
            result += property.toString() + "\n";
        }

        return result;
    }

    /**
     * Gets the number of properties in the list.
     *
     * @return the property count
     */
    public int getPropertyCount() {
        return properties.size();
    }

    /**
     * Loads property information from a text file.
     *
     * @param fileName the name of the file to read
     * @return true if the file was read successfully, otherwise false
     */

    public boolean loadProperties(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            System.out.println("File name cannot be empty.");
            return false;
        }

        int loaded = 0;
        int skipped = 0;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {

                try {
                    String[] data = line.split(",", -1);

                    if (data.length != 7) {
                        skipped++;
                        continue;
                    }

                    int id = Integer.parseInt(data[0].trim());
                    String address = data[1].trim();
                    String type = data[2].trim();
                    double rate = Double.parseDouble(data[3].trim());
                    int bedrooms = Integer.parseInt(data[4].trim());
                    String availability = data[5].trim();
                    String owner = data[6].trim();

                    // Validate all information
                    if (id <= 0 || address.isEmpty()
                            || type.isEmpty()
                            || !Double.isFinite(rate) || rate <= 0
                            || bedrooms < 0 || owner.isEmpty()
                            || (!availability.equalsIgnoreCase("true")
                            && !availability.equalsIgnoreCase("false"))) {
                        skipped++;
                        continue;
                    }

                    boolean available =
                            Boolean.parseBoolean(availability);

                    Property property = new Property(
                            id, address, type, rate,
                            bedrooms, available, owner
                    );

                    // Do not add duplicate properties
                    if (addProperty(property)) {
                        loaded++;
                    } else {
                        skipped++;
                    }

                } catch (NumberFormatException e) {
                    skipped++;
                }
            }

            System.out.println("Properties loaded: " + loaded);
            System.out.println("Records skipped: " + skipped);
            return true;

        } catch (IOException | SecurityException e) {
            System.out.println("Unable to read file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks whether a property is available for the requested dates.
     * Prevents overlapping reservations for the same property.
     * Allows a new guest to check in on another guest's checkout date.
     *
     * @param propertyId the ID of the property
     * @param checkIn the requested check-in date
     * @param checkOut the requested check-out date
     * @return true if the dates are valid and do not overlap
     *         with another reservation, otherwise false
     */
    public boolean isDateAvailable(int propertyId,
                                   LocalDate checkIn,
                                   LocalDate checkOut) {

        if (checkIn == null || checkOut == null ||
                !checkOut.isAfter(checkIn)) {
            return false;
        }

        for (Reservation reservation : reservations) {

            if (reservation.getPropertyId() == propertyId) {

                // Dates overlap if the new booking starts before
                // the old checkout and ends after the old check-in
                if (checkIn.isBefore(reservation.getCheckOut()) &&
                        checkOut.isAfter(reservation.getCheckIn())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Creates a new guest reservation for a property.
     * Checks that the property exists, is available, and that
     * the guest name and reservation dates are valid.
     * Also prevents overlapping reservations.
     *
     * @param propertyId the ID of the property being reserved
     * @param guestName the name of the guest
     * @param checkIn the reservation check-in date
     * @param checkOut the reservation check-out date
     * @return true if the reservation was created, otherwise false
     */
    public boolean createReservation(int propertyId,
                                     String guestName,
                                     LocalDate checkIn,
                                     LocalDate checkOut) {

        Property property = findProperty(propertyId);

        // Check if the property exists
        if (property == null) {
            return false;
        }

        // Check guest name and dates
        if (guestName == null || guestName.isBlank() ||
                checkIn == null || checkOut == null ||
                !checkOut.isAfter(checkIn)) {
            return false;
        }

        // Check property availability
        if (!property.isAvailable()) {
            return false;
        }

        // Check for overlapping reservations
        if (!isDateAvailable(propertyId, checkIn, checkOut)) {
            return false;
        }

        // Save the new reservation
        Reservation reservation = new Reservation(
                propertyId,
                guestName,
                checkIn,
                checkOut,
                property.getNightlyRate()
        );

        reservations.add(reservation);

        System.out.println("\nReservation created successfully!");
        System.out.println(reservation);

        return true;
    }

    /**
     * Displays information about all guest reservations.
     * Returns a message if no reservations have been created.
     *
     * @return a string containing all reservation details,
     *         or a message if the reservation list is empty
     */
    public String displayReservations() {

        if (reservations.isEmpty()) {
            return "No reservations found.";
        }

        String result = "";

        for (Reservation reservation : reservations) {
            result += reservation.toString() + "\n";
        }

        return result;
    }
}
