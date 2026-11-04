import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PropertyManagerTest {

    // Test if a new property can be added
    @Test
    public void testAddProperty() {

        PropertyManager manager = new PropertyManager();

        Property property = new Property(
                101,
                "123 Beach Ave",
                "Condo",
                185.00,
                2,
                true,
                "John Smith"
        );

        // Add property to the list
        boolean result = manager.addProperty(property);

        // Check if the property was added correctly
        assertTrue(result);
        assertEquals(1, manager.getPropertyCount());
        assertNotNull(manager.findProperty(101));

        // Check if the address is correct
        assertEquals("123 Beach Ave",
                manager.findProperty(101).getAddress());
    }

    // Test if duplicate property IDs are rejected
    @Test
    public void testDuplicateProperty() {

        PropertyManager manager = new PropertyManager();

        Property property1 = new Property(
                101, "123 Beach Ave", "Condo",
                185.00, 2, true, "John Smith"
        );

        Property property2 = new Property(
                101, "456 Ocean Dr", "House",
                250.00, 3, true, "Mary Johnson"
        );

        // Add the first property
        assertTrue(manager.addProperty(property1));

        // Try adding another property with the same ID
        assertFalse(manager.addProperty(property2));

        // Make sure only one property was saved
        assertEquals(1, manager.getPropertyCount());
    }


    // Test if a property can be removed
    @Test
    public void testRemoveProperty() {

        PropertyManager manager = new PropertyManager();

        Property property = new Property(
                101, "123 Beach Ave", "Condo",
                185.00, 2, true, "John Smith"
        );

        // Add property before removing it
        manager.addProperty(property);

        // Remove property 101
        boolean result = manager.removeProperty(101);

        // Check if the property was removed
        assertTrue(result);
        assertEquals(0, manager.getPropertyCount());
        assertNull(manager.findProperty(101));
    }

    // Test removing a property that does not exist
    @Test
    public void testRemoveInvalidProperty() {

        PropertyManager manager = new PropertyManager();

        // Try removing property 999
        boolean result = manager.removeProperty(999);

        // Check that the program rejects the request
        assertFalse(result);
        assertEquals(0, manager.getPropertyCount());
    }


    // Test if property information can be updated
    @Test
    public void testUpdateProperty() {

        PropertyManager manager = new PropertyManager();

        Property property = new Property(
                101, "123 Beach Ave", "Condo",
                185.00, 2, true, "John Smith"
        );

        manager.addProperty(property);

        // Change the nightly rate
        boolean result = property.setNightlyRate(210.00);

        // Check if the new rate was saved
        assertTrue(result);
        assertEquals(210.00,
                manager.findProperty(101).getNightlyRate());
    }

    // Test if an invalid nightly rate is rejected
    @Test
    public void testInvalidUpdate() {

        PropertyManager manager = new PropertyManager();

        Property property = new Property(
                101, "123 Beach Ave", "Condo",
                185.00, 2, true, "John Smith"
        );

        manager.addProperty(property);

        // Try to enter a negative nightly rate
        boolean result = property.setNightlyRate(-50.00);

        // Check that the update failed
        assertFalse(result);

        // Original rate should still be 185
        assertEquals(185.00,
                manager.findProperty(101).getNightlyRate());
    }


    // Test creating a guest reservation
    @Test
    public void testCreateReservation() {

        PropertyManager manager = new PropertyManager();

        Property property = new Property(
                101, "123 Beach Ave", "Condo",
                185.00, 2, true, "John Smith"
        );

        manager.addProperty(property);

        LocalDate checkIn = LocalDate.of(2026, 11, 5);
        LocalDate checkOut = LocalDate.of(2026, 11, 8);

        // Check the reservation calculation
        Reservation reservation = new Reservation(
                101, "Sarah Johnson", checkIn, checkOut, 185.00
        );

        assertEquals(3, reservation.getNights());
        assertEquals(555.00, reservation.getTotalCost());

        // Check if the reservation can be created
        boolean result = manager.createReservation(
                101, "Sarah Johnson", checkIn, checkOut
        );

        assertTrue(result);
    }

    // Test if overlapping reservations are rejected
    @Test
    public void testOverlappingReservation() {

        PropertyManager manager = new PropertyManager();

        Property property = new Property(
                101, "123 Beach Ave", "Condo",
                185.00, 2, true, "John Smith"
        );

        manager.addProperty(property);

        // Create the first reservation
        boolean firstBooking = manager.createReservation(
                101,
                "Sarah Johnson",
                LocalDate.of(2026, 11, 5),
                LocalDate.of(2026, 11, 8)
        );

        assertTrue(firstBooking);

        // Try booking the same property for overlapping dates
        boolean secondBooking = manager.createReservation(
                101,
                "Michael Brown",
                LocalDate.of(2026, 11, 6),
                LocalDate.of(2026, 11, 9)
        );

        // The second booking should not be allowed
        assertFalse(secondBooking);
    }

    
    // Test if properties can be loaded from a file
    @Test
    public void testLoadFile() throws IOException {

        PropertyManager manager = new PropertyManager();

        // Create a temporary file for testing
        File file = File.createTempFile("properties", ".txt");
        file.deleteOnExit();

        // Add one property to the test file
        java.nio.file.Files.writeString(
                file.toPath(),
                "101,123 Beach Ave,Condo,185.00,2,true,John Smith"
        );

        // Load the property from the file
        boolean result = manager.loadProperties(file.getAbsolutePath());

        // Check if the file was loaded correctly
        assertTrue(result);
        assertEquals(1, manager.getPropertyCount());
        assertNotNull(manager.findProperty(101));
    }

    // Test if an incorrect file name is rejected
    @Test
    public void testInvalidFile() {

        PropertyManager manager = new PropertyManager();

        // Try opening a file that does not exist
        boolean result = manager.loadProperties(
                "missing_property_file_12345.txt"
        );

        // Check that loading failed
        assertFalse(result);
        assertEquals(0, manager.getPropertyCount());
    }

}
