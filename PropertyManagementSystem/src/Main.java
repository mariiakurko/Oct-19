
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        PropertyManager manager = new PropertyManager();
        Main app = new Main();

        int choice = 0;

        // Keep the menu running until the user chooses Exit
        while (choice != 7) {

            System.out.println("\n===== PROPERTY MANAGEMENT SYSTEM =====");
            System.out.println("1. Load Properties From File");
            System.out.println("2. Display All Properties");
            System.out.println("3. Add New Property");
            System.out.println("4. Remove Property");
            System.out.println("5. Update Property");
            System.out.println("6. Create Guest Reservation");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            // Check if the menu input is a number
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {

                case 1:
                    System.out.print("Enter file name or path: ");
                    String fileName = scanner.nextLine().trim();
                    manager.loadProperties(fileName);
                    break;

                case 2:
                    System.out.println(manager.displayProperties());
                    break;

                case 3:
                    app.addNewProperty(scanner, manager);
                    break;

                case 4:
                    app.deleteProperty(scanner, manager);
                    break;

                case 5:
                    app.updateProperty(scanner, manager);
                    break;

                case 6:
                    app.createGuestReservation(scanner, manager);
                    break;

                case 7:
                    System.out.println("Thank you for using Property Management System!");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1-7.");
            }
        }

        scanner.close();
    }

    // Add a new rental property
    public boolean addNewProperty(Scanner scanner, PropertyManager manager) {

        System.out.println("\n===== ADD NEW PROPERTY =====");

        int propertyId;

        while (true) {
            System.out.print("Enter Property ID: ");

            if (scanner.hasNextInt()) {
                propertyId = scanner.nextInt();
                scanner.nextLine();

                if (propertyId > 0 &&
                        manager.findProperty(propertyId) == null) {
                    break;
                }
            } else {
                scanner.nextLine();
            }

            System.out.println("Invalid or duplicate Property ID.");
        }

        String address;

        while (true) {
            System.out.print("Enter Address: ");
            address = scanner.nextLine().trim();

            if (!address.isEmpty()) {
                break;
            }

            System.out.println("Address cannot be empty.");
        }

        String propertyType;

        while (true) {
            System.out.print("Enter Property Type: ");
            propertyType = scanner.nextLine().trim();

            if (!propertyType.isEmpty()) {
                break;
            }

            System.out.println("Property type cannot be empty.");
        }

        double nightlyRate;

        while (true) {
            System.out.print("Enter Nightly Rate: ");

            if (scanner.hasNextDouble()) {
                nightlyRate = scanner.nextDouble();
                scanner.nextLine();

                if (Double.isFinite(nightlyRate) && nightlyRate > 0) {
                    break;
                }
            } else {
                scanner.nextLine();
            }

            System.out.println("Please enter a valid nightly rate.");
        }

        int bedrooms;

        while (true) {
            System.out.print("Enter Number of Bedrooms: ");

            if (scanner.hasNextInt()) {
                bedrooms = scanner.nextInt();
                scanner.nextLine();

                if (bedrooms >= 0) {
                    break;
                }
            } else {
                scanner.nextLine();
            }

            System.out.println("Please enter a valid bedroom number.");
        }

        boolean available;

        while (true) {
            System.out.print("Is Property Available? (yes/no): ");
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("yes")) {
                available = true;
                break;
            }

            if (answer.equalsIgnoreCase("no")) {
                available = false;
                break;
            }

            System.out.println("Please enter yes or no.");
        }

        String owner;

        while (true) {
            System.out.print("Enter Owner Name: ");
            owner = scanner.nextLine().trim();

            if (!owner.isEmpty()) {
                break;
            }

            System.out.println("Owner name cannot be empty.");
        }

        // Create and save the property
        Property property = new Property(
                propertyId, address, propertyType,
                nightlyRate, bedrooms, available, owner
        );

        if (manager.addProperty(property)) {
            System.out.println("\nProperty added successfully!");
            System.out.println(property);
            return true;
        }

        System.out.println("Unable to add property.");
        return false;
    }

    // Remove a property using its ID
    public boolean deleteProperty(Scanner scanner, PropertyManager manager) {

        System.out.println("\n===== REMOVE PROPERTY =====");
        System.out.print("Enter Property ID to remove: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid Property ID.");
            scanner.nextLine();
            return false;
        }

        int propertyId = scanner.nextInt();
        scanner.nextLine();

        if (propertyId <= 0) {
            System.out.println("Property ID must be greater than 0.");
            return false;
        }

        Property property = manager.findProperty(propertyId);

        if (property == null) {
            System.out.println("Property not found.");
            return false;
        }

        System.out.println("\nProperty found:");
        System.out.println(property);

        System.out.print("Are you sure you want to delete this property? (yes/no): ");
        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return false;
        }

        // PropertyManager also checks for existing reservations
        if (manager.removeProperty(propertyId)) {
            System.out.println("Property removed successfully!");
            return true;
        }

        System.out.println("Unable to remove property. It may have a reservation.");
        return false;
    }

    // Update an existing property's information
    public boolean updateProperty(Scanner scanner, PropertyManager manager) {

        System.out.println("\n===== UPDATE PROPERTY =====");
        System.out.print("Enter Property ID to update: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid Property ID.");
            scanner.nextLine();
            return false;
        }

        int propertyId = scanner.nextInt();
        scanner.nextLine();

        Property property = manager.findProperty(propertyId);

        if (property == null) {
            System.out.println("Property not found.");
            return false;
        }

        System.out.println("\nCurrent property information:");
        System.out.println(property);

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Property ID");
        System.out.println("2. Address");
        System.out.println("3. Property Type");
        System.out.println("4. Nightly Rate");
        System.out.println("5. Bedrooms");
        System.out.println("6. Availability");
        System.out.println("7. Owner");
        System.out.print("Enter your choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid option.");
            scanner.nextLine();
            return false;
        }

        int updateChoice = scanner.nextInt();
        scanner.nextLine();

        boolean updated = false;

        switch (updateChoice) {

            case 1:
                System.out.print("Enter new Property ID: ");

                if (scanner.hasNextInt()) {
                    int newId = scanner.nextInt();
                    scanner.nextLine();

                    if (newId > 0 &&
                            (newId == propertyId ||
                             manager.findProperty(newId) == null)) {
                        updated = property.setPropertyId(newId);
                    } else {
                        System.out.println("Invalid or duplicate Property ID.");
                    }
                } else {
                    scanner.nextLine();
                    System.out.println("Invalid Property ID.");
                }
                break;

            case 2:
                System.out.print("Enter new Address: ");
                updated = property.setAddress(scanner.nextLine().trim());
                break;

            case 3:
                System.out.print("Enter new Property Type: ");
                updated = property.setPropertyType(scanner.nextLine().trim());
                break;

            case 4:
                System.out.print("Enter new Nightly Rate: ");

                if (scanner.hasNextDouble()) {
                    double newRate = scanner.nextDouble();
                    scanner.nextLine();
                    updated = property.setNightlyRate(newRate);
                } else {
                    scanner.nextLine();
                }
                break;

            case 5:
                System.out.print("Enter new Number of Bedrooms: ");

                if (scanner.hasNextInt()) {
                    int newBedrooms = scanner.nextInt();
                    scanner.nextLine();
                    updated = property.setBedrooms(newBedrooms);
                } else {
                    scanner.nextLine();
                }
                break;

            case 6:
                System.out.print("Is Property Available? (yes/no): ");
                String answer = scanner.nextLine().trim();

                if (answer.equalsIgnoreCase("yes")) {
                    updated = property.setAvailable(true);
                } else if (answer.equalsIgnoreCase("no")) {
                    updated = property.setAvailable(false);
                }
                break;

            case 7:
                System.out.print("Enter new Owner Name: ");
                updated = property.setOwner(scanner.nextLine().trim());
                break;

            default:
                System.out.println("Invalid update option.");
        }

        if (updated) {
            System.out.println("\nProperty updated successfully!");
            System.out.println(property);
            return true;
        }

        System.out.println("Unable to update property. Invalid input.");
        return false;
    }

    // Create a reservation and calculate its cost
    public boolean createGuestReservation(Scanner scanner,
                                          PropertyManager manager) {

        System.out.println("\n===== CREATE GUEST RESERVATION =====");

        System.out.print("Enter Property ID: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid Property ID.");
            scanner.nextLine();
            return false;
        }

        int propertyId = scanner.nextInt();
        scanner.nextLine();

        // Find the property
        Property property = manager.findProperty(propertyId);

        if (property == null) {
            System.out.println("Property not found.");
            return false;
        }

        if (!property.isAvailable()) {
            System.out.println("This property is not available for booking.");
            return false;
        }

        System.out.println("\nSelected Property:");
        System.out.println(property);

        // Ask for guest name
        System.out.print("Enter Guest Name: ");
        String guestName = scanner.nextLine().trim();

        if (guestName.isEmpty()) {
            System.out.println("Guest name cannot be empty.");
            return false;
        }

        // Ask for check-in date
        System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
        String checkInText = scanner.nextLine().trim();

        // Ask for check-out date
        System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
        String checkOutText = scanner.nextLine().trim();

        try {
            LocalDate checkIn = LocalDate.parse(checkInText);
            LocalDate checkOut = LocalDate.parse(checkOutText);

            // Check if checkout is after check-in
            if (!checkOut.isAfter(checkIn)) {
                System.out.println("Check-out must be after check-in.");
                return false;
            }

            // Check if the dates are already booked
            if (!manager.isDateAvailable(propertyId, checkIn, checkOut)) {
                System.out.println("These dates are already booked.");
                return false;
            }

            // Create reservation and display calculated total
            return manager.createReservation(
                    propertyId, guestName, checkIn, checkOut
            );

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return false;
        }
    }
}
