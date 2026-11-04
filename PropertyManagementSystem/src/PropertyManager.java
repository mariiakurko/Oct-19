
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class PropertyManager {

    // Lists to store properties and reservations
    private ArrayList<Property> properties;
    private ArrayList<Reservation> reservations;

    // Constructor creates empty lists
    public PropertyManager() {
        properties = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    // Add a new property
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

    // Find a property by its ID
    public Property findProperty(int propertyId) {

        for (Property property : properties) {
            if (property.getPropertyId() == propertyId) {
                return property;
            }
        }

        return null;
    }

    // Remove a property
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

    // Display all properties
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

    // Get the number of properties
    public int getPropertyCount() {
        return properties.size();
    }

    // Load property information from a text file
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

    // Check if requested dates overlap with another reservation
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

    // Create a guest reservation
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

    // Display reservations
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
