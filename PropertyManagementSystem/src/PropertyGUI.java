import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// Mariia Svereniak 
// Property Management System

public class PropertyGUI extends JFrame {

    public PropertyGUI() {

        DatabaseManager databaseManager = new DatabaseManager();
        final String[] databasePath = {null};
        PropertyManager manager = new PropertyManager();
        setTitle("Property Management System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel titleLabel = new JLabel("Property Management System");
        JButton displayButton = new JButton("Display Properties");
        JButton loadButton = new JButton("Connect Database");
        JButton addButton = new JButton("Add Property");
        JButton removeButton = new JButton("Remove Property");
        JButton updateButton = new JButton("Update Property");
        JButton reservationButton = new JButton("Create Reservation");
        JButton exitButton = new JButton("EXIT");

        // Create the panel and set its background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(235, 244, 252));

        // Style the Display Properties button
        displayButton.setBackground(new Color(173, 204, 241));
        displayButton.setForeground(new Color(42, 74, 107));

        loadButton.setBackground(new Color(173, 204, 241));
        loadButton.setForeground(new Color(42, 74, 107));

        addButton.setBackground(new Color(173, 204, 241));
        addButton.setForeground(new Color(42, 74, 107));

        removeButton.setBackground(new Color(173, 204, 241));
        removeButton.setForeground(new Color(42, 74, 107));

        updateButton.setBackground(new Color(173, 204, 241));
        updateButton.setForeground(new Color(42, 74, 107));

        reservationButton.setBackground(new Color(173, 204, 241));
        reservationButton.setForeground(new Color(42, 74, 107));

        exitButton.setBackground(new Color(173, 204, 241));
        exitButton.setForeground(new Color(42, 74, 107));

        // Add space around the buttons
        mainPanel.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Arrange the buttons in three rows and two columns
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10)); 
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(220, 234, 248)); 
        mainPanel.setFocusable(true);

        // Add the title to the header
        titleLabel.setForeground(new Color(42, 74, 107));
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));
        headerPanel.add(titleLabel);
        
        mainPanel.add(displayButton);
        mainPanel.add(loadButton);
        mainPanel.add(addButton);
        mainPanel.add(removeButton);
        mainPanel.add(updateButton);
        mainPanel.add(reservationButton);

        // to keep the title above the buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(mainPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // Area for displaying property information
        JTextArea propertyArea = new JTextArea();
        propertyArea.setEditable(false);
        propertyArea.setText("Property records will appear here...");

        // Create the table for displaying properties
        String[] columnNames = {
            "ID", "Address", "Type", "Nightly Rate",
                "Bedrooms", "Available", "Owner"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
        return false;
    }
};

JTable propertyTable = new JTable(tableModel);
propertyTable.setSelectionMode(
    javax.swing.ListSelectionModel.SINGLE_SELECTION
);
        
        // Display Properties button PATH PropertyManagementSystem/properties.db
        displayButton.addActionListener(e -> {

        if (databasePath[0] == null) {
        JOptionPane.showMessageDialog(
            this,
            "Please connect to a database first!"
        );
        return;
        }

        try {
        java.util.ArrayList<Property> properties =
            databaseManager.getAllProperties(databasePath[0]);

        // Clear previous rows before displaying properties
        tableModel.setRowCount(0);

        for (Property property : properties) {
            tableModel.addRow(new Object[] {
            property.getPropertyId(),
            property.getAddress(),
            property.getPropertyType(),
            property.getNightlyRate(),
            property.getBedrooms(),
            property.isAvailable() ? "Available" : "Unavailable",
            property.getOwner()
    });
} }
        catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(
            this,
            "Error displaying properties: " + ex.getMessage());}
});
        
        // Load Property Button
        loadButton.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog(
            this, "Enter SQLite database file path:"
            );
                if (fileName == null) {
                    return;}
                if (fileName.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter a filename.");
                    return; }
        try {
        // Check that the SQLite database can be accessed
        databaseManager.getAllProperties(fileName);

        // Save the database path
        databasePath[0] = fileName;

        JOptionPane.showMessageDialog(
        this,
        "Database connected successfully!",
        "Success",
        JOptionPane.INFORMATION_MESSAGE
        ); } 
        catch (java.sql.SQLException ex) {
    JOptionPane.showMessageDialog(
        this,
        "Cannot connect to database: " + ex.getMessage(),
        "Connection Failed",
        JOptionPane.ERROR_MESSAGE
    );
}
                
                });

        // Add property button    
        addButton.addActionListener(e -> {
            // Make sure a database is connected before adding a property
        if (databasePath[0] == null) {
            JOptionPane.showMessageDialog(
            this,
                "Please connect to a database first!"
                );
                return;
        }
                JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                JTextField addressField = new JTextField(50);
                    formPanel.add(new JLabel("Address:"));
                    formPanel.add(addressField);

                JTextField idField = new JTextField(20);
                    formPanel.add(new JLabel("Property ID:"));
                    formPanel.add(idField);

                String[] propertyTypes = {"House", "Townhouse", "Condo", "Boat"};
                JComboBox<String> typeBox = new JComboBox<>(propertyTypes);
                    formPanel.add(new JLabel("Property Type:"));
                    formPanel.add(typeBox);

                JTextField rateField = new JTextField(20);
                    formPanel.add(new JLabel("Nightly Rate(USD):"));
                    formPanel.add(rateField);

                JTextField bedroomsField = new JTextField(20);
                    formPanel.add(new JLabel("Bedrooms Count:"));
                    formPanel.add(bedroomsField);

                String[] options = {"Available", "Unavailable"};
                JComboBox<String> availabilityBox = new JComboBox<>(options);
                    formPanel.add(new JLabel("Availability:"));
                    formPanel.add(availabilityBox);

                JTextField ownerField = new JTextField(20);
                    formPanel.add(new JLabel("Owner:"));
                    formPanel.add(ownerField);
                // Display the Add Property form
                int result = JOptionPane.showConfirmDialog(
                    this,
                     formPanel,
                         "Add New Property",
                            JOptionPane.OK_CANCEL_OPTION
                );


            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            // Validate Nightly Rate
            String rateText = rateField.getText().trim();

            if (!rateText.matches("\\d+(\\.\\d{1,2})?")) {
                JOptionPane.showMessageDialog(this,
                    "Nightly Rate must be a number with no more than 2 decimal places!");
                    return;
             }
            
    double nightlyRate;
    
            try {
                nightlyRate = Double.parseDouble(rateText);

                if (!Double.isFinite(nightlyRate) || nightlyRate <= 0) {
                        JOptionPane.showMessageDialog(this,
                             "Nightly Rate must be greater than zero!");
                                return;
                    }

            } 
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid nightly rate!");
                    return;
                }

            String bedroomsText = bedroomsField.getText().trim();

            // Check the input format
                if (!bedroomsText.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this,
                     "Bedrooms must be a whole number!");
                return;}

        int bedrooms;

            try {
                bedrooms = Integer.parseInt(bedroomsText);

                if (bedrooms <= 0) {
                 JOptionPane.showMessageDialog(this,
                    "Bedrooms must be greater than zero!");
                return;}}
            
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid bedroom count!");
                        return;}

            // Validate Property ID
            String idText = idField.getText().trim();

                if (!idText.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this,
                        "Property ID must be a whole number!");
                            return;
                                }
            int propertyId;

                try {
                    propertyId = Integer.parseInt(idText);

                         if (propertyId <= 0) {
                                JOptionPane.showMessageDialog(this,
                                    "Property ID must be greater than zero!");
                                        return;
                        }
                    }                       
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Property ID!");
                        return;
                            }
            
            String address = addressField.getText().trim();
            String propertyType = (String) typeBox.getSelectedItem();
            String owner = ownerField.getText().trim();

                if (address.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Address cannot be empty!");
                        return;
                            }

                if (owner.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Owner cannot be empty!");
                        return;
                            }
                boolean available = availabilityBox.getSelectedItem().equals("Available");

                

       
        Property property = new Property(
            propertyId,
            address,
            propertyType,
            nightlyRate,
            bedrooms,
            available,
            owner
        );

        try {
    // Save the new property directly to SQLite
    databaseManager.addProperty(databasePath[0], property);

        JOptionPane.showMessageDialog(
        this,
        "Property added successfully!"
        );} 
        catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(
            this,
                "Could not add property: " + ex.getMessage(),
                    "Database Error",
        JOptionPane.ERROR_MESSAGE
    );
}
    }); // End of Add Property listener

       
    
    
    // Remove Property listener
        removeButton.addActionListener(e -> {
            // Make sure a property is selected in the table
            int selectedRow = propertyTable.getSelectedRow();

            if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
            this,
            "Please select a property from the table first!");
    return;
}
            // Get the property ID from the selected row
            int propertyId = (int) propertyTable.getValueAt(selectedRow, 0);

        

        int confirm = JOptionPane.showConfirmDialog(
            this,
                "Are you sure you want to remove Property ID " + propertyId + "?",
                    "Confirm Removal",
                        JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return; }

        // attempt to remove the property
        try {
    if (databasePath[0] == null) {
        JOptionPane.showMessageDialog(
            this,
            "Please connect to a database first!"
        );
        return;
    }

    if (databaseManager.removeProperty(databasePath[0], propertyId)) {
        JOptionPane.showMessageDialog(
            this,
            "Property removed successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Remove the deleted property from the table
        tableModel.removeRow(selectedRow);

    } else {
        JOptionPane.showMessageDialog(
            this,
            "Cannot remove this property. It may have an existing reservation.",
            "Removal Failed",
            JOptionPane.ERROR_MESSAGE
        );
    }

}   catch (java.sql.SQLException ex) {
    JOptionPane.showMessageDialog(
        this,
        "Database error: " + ex.getMessage(),
        "Removal Failed",
        JOptionPane.ERROR_MESSAGE
    );
}

}); // end of remove property lostener

        // Update Property button
        updateButton.addActionListener(e -> {

            // Make sure a property is selected
        int selectedRow = propertyTable.getSelectedRow();

            if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
            this,
                "Please select a property from the table first!");
    return;
}

            // get the id of the selected property
            int propertyId = (int) propertyTable.getValueAt(selectedRow, 0);

        Property propertyToUpdate = null;

        try {
            if (databasePath[0] == null) {
        JOptionPane.showMessageDialog(
            this,
            "Please connect to a database first!"
        );
        return;
    }

        java.util.ArrayList<Property> properties =
        databaseManager.getAllProperties(databasePath[0]);

        for (Property property : properties) {
        if (property.getPropertyId() == propertyId) {
            propertyToUpdate = property;
            break;
        }
    }}       
        
        catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                    "Error loading property: " + ex.getMessage(),
                        "Database Error",
                            JOptionPane.ERROR_MESSAGE);
        return;}

        if (propertyToUpdate == null) {
            JOptionPane.showMessageDialog(this, "Property not found!");
                return; }

        // create the Update Property form
        JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Show the property's current address
        JTextField addressField = new JTextField(propertyToUpdate.getAddress());
                updatePanel.add(new JLabel("Address:"));
                updatePanel.add(addressField);

         // Nightly Rate
        JTextField rateField = new JTextField(
        String.valueOf(propertyToUpdate.getNightlyRate())
        );
        updatePanel.add(new JLabel("Nightly Rate (USD):"));
        updatePanel.add(rateField);

    //   // Bedrooms
        JTextField bedroomsField = new JTextField(
        String.valueOf(propertyToUpdate.getBedrooms())
        );
        updatePanel.add(new JLabel("Bedrooms:"));
        updatePanel.add(bedroomsField);   
        
        // Property Type dropdown
        String[] propertyTypes = {"House", "Townhouse", "Condo", "Boat"};

        JComboBox<String> typeBox = new JComboBox<>(propertyTypes);

        // Pre-select the property's current type
        typeBox.setSelectedItem(propertyToUpdate.getPropertyType());

        updatePanel.add(new JLabel("Property Type:"));
        updatePanel.add(typeBox);

        // Availability dropdown
        String[] availabilityOptions = {"Available", "Unavailable"};

        JComboBox<String> availabilityBox = new JComboBox<>(availabilityOptions);

        availabilityBox.setSelectedItem(
        propertyToUpdate.isAvailable() ? "Available" : "Unavailable"
        );

        // Owner
        JTextField ownerField = new JTextField(propertyToUpdate.getOwner());

        updatePanel.add(new JLabel("Owner:"));
        updatePanel.add(ownerField);

        updatePanel.add(new JLabel("Availability:"));
        updatePanel.add(availabilityBox);

        int result = JOptionPane.showConfirmDialog(
            this,
                updatePanel,
                    "Update Property",
                            JOptionPane.OK_CANCEL_OPTION);

        // Stop if the user clicks Cancel
        if (result != JOptionPane.OK_OPTION) {
        return;}
        
        String newAddress = addressField.getText().trim();

        if (newAddress.isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Address cannot be empty!");
                    return;
        }

        // Validate updated Nightly Rate
        String rateText = rateField.getText().trim();

        if (!rateText.matches("\\d+(\\.\\d{1,2})?")) {
            JOptionPane.showMessageDialog(this,
                "Nightly Rate must be a number with no more than 2 decimal places!");
            return;
        }

        double newRate;

        try {
            newRate = Double.parseDouble(rateText);

        if (!Double.isFinite(newRate) || newRate <= 0) {
            JOptionPane.showMessageDialog(this,
                "Nightly Rate must be greater than zero!");
        return;
        } }       
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Nightly Rate!");
                return;
        }  

        // Validate updated Bedrooms
            String bedroomsText = bedroomsField.getText().trim();

            if (!bedroomsText.matches("\\d+")) {
                 JOptionPane.showMessageDialog(this,
                    "Bedrooms must be a whole number!");
            return;}

        int newBedrooms;

        try {
            newBedrooms = Integer.parseInt(bedroomsText);

        if (newBedrooms <= 0) {
            JOptionPane.showMessageDialog(this,
            "Bedrooms must be greater than zero!");
                return;}}
                
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid bedroom count!");
                    return;}

        // Get the updated Property Type
        String newType = (String) typeBox.getSelectedItem();

        // Get the updated Availability
        boolean newAvailable = availabilityBox.getSelectedItem()
                .equals("Available");

        // Get the updated Owner
        String newOwner = ownerField.getText().trim();

        if (newOwner.isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Owner cannot be empty!");
                return;}

        propertyToUpdate.setAddress(newAddress);
        propertyToUpdate.setPropertyType(newType);
        propertyToUpdate.setNightlyRate(newRate);
        propertyToUpdate.setBedrooms(newBedrooms);
        propertyToUpdate.setAvailable(newAvailable);
        propertyToUpdate.setOwner(newOwner);

        try {
    if (databaseManager.updateProperty(databasePath[0], propertyToUpdate)) {
        JOptionPane.showMessageDialog(
            this,
            "Property updated successfully!"
        );
    } else {
        JOptionPane.showMessageDialog(
            this,
            "Property not found in database!"
        );
    }} 
        catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                    "Database error: " + ex.getMessage(),
                        "Update Failed",
                            JOptionPane.ERROR_MESSAGE
            );
        }

}); // End of Update Property listener

        // Create Reservation button
        reservationButton.addActionListener(e -> {
            JPanel reservationPanel = new JPanel(new GridLayout(0, 2, 10, 10));

          JTextField propertyIdField = new JTextField(20);

        reservationPanel.add(new JLabel("Property ID:"));
        reservationPanel.add(propertyIdField);
        
        // Guest Name
        JTextField guestNameField = new JTextField(20);

        reservationPanel.add(new JLabel("Guest Name:"));
        reservationPanel.add(guestNameField);

        // Check-in Date
        JTextField checkInField = new JTextField(20);

        reservationPanel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        reservationPanel.add(checkInField);

        // Check-out Date
        JTextField checkOutField = new JTextField(20);

        reservationPanel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        reservationPanel.add(checkOutField);

        int result = JOptionPane.showConfirmDialog(
            this,
                reservationPanel,
                    "Create Reservation",
                     JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;}

        String idText = propertyIdField.getText().trim();

        int propertyId;
        if (!idText.matches("\\d+")) {
        JOptionPane.showMessageDialog(this,
        "Property ID must be a whole number!");
            return;}

        try {
            propertyId = Integer.parseInt(idText);

        if (propertyId <= 0) {
            JOptionPane.showMessageDialog(this,
                "Property ID must be greater than zero!");
        return;}} 
        
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Property ID!");
        return;}

        Property selectedProperty = null;

        try {
            if (databasePath[0] == null) {
        JOptionPane.showMessageDialog(this,
            "Please connect to a database first!");
        return;
            }

        java.util.ArrayList<Property> properties =
            databaseManager.getAllProperties(databasePath[0]);

            for (Property property : properties) {
                if (property.getPropertyId() == propertyId) {
                    selectedProperty = property;
                    break;
                }
            }

        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading property: " + ex.getMessage());
            return;
        }

        if (selectedProperty == null) {
            JOptionPane.showMessageDialog(this,
                "Property not found!");
                    return;}

        if (!selectedProperty.isAvailable()) {
            JOptionPane.showMessageDialog(this,
                "This property is currently unavailable!");
                    return;}

        String guestName = guestNameField.getText().trim();

        if (guestName.isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Guest Name cannot be empty!");
        return;}

        LocalDate checkIn;
        LocalDate checkOut;

        try {
        checkIn = LocalDate.parse(checkInField.getText().trim());
        checkOut = LocalDate.parse(checkOutField.getText().trim());

        if (!checkOut.isAfter(checkIn)) {
        JOptionPane.showMessageDialog(this,
            "Check-out must be after check-in!");
        return;}} 
        
        catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid dates in YYYY-MM-DD format!");
        return;}       
        
        // Prepare the reservation for SQLite
        Reservation newReservation = new Reservation(
            propertyId,
            guestName,
            checkIn,
            checkOut,
            selectedProperty.getNightlyRate()
        );
    
        // Create the reservation
            boolean created;

            try {
                created = databaseManager.addReservation(
                    databasePath[0],
                    newReservation
                );
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + ex.getMessage(),
                    "Reservation Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

        if (created) {
            Reservation summary = new Reservation(
            propertyId,
            guestName,
            checkIn,
            checkOut,
            selectedProperty.getNightlyRate());


        JOptionPane.showMessageDialog(this,
            "Reservation created successfully!"
            + "\nGuest: " + guestName
            + "\nNumber of Nights: " + summary.getNights()
            + "\nNightly Rate: $" + String.format("%.2f", selectedProperty.getNightlyRate())
            + "\nTotal Cost: $" + String.format("%.2f", summary.getTotalCost())
        );
    }
        
        else {
            JOptionPane.showMessageDialog(this,
                "Reservation could not be created. Please check availability and dates.");}

}); // End of Create Reservation listener

        // Match the property display area with the blue theme
        propertyArea.setBackground(new Color(235, 244, 252));
        propertyArea.setForeground(new Color(42, 74, 107));
        propertyArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        
        // Make the property list scrollable
        propertyArea.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JScrollPane scrollPane = new JScrollPane(propertyTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for the Exit button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(235, 244, 252));

        // Place the Exit button on the right
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        bottomPanel.add(exitButton);
        // Close the program when Exit is clicked
        exitButton.addActionListener(e -> {
            dispose();
            });

        exitButton.setBackground(new Color(42, 74, 107));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Make the Exit button show its navy background
        exitButton.setOpaque(true);
        exitButton.setContentAreaFilled(true);
        exitButton.setBorderPainted(false);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
        javax.swing.SwingUtilities.invokeLater(
            () -> mainPanel.requestFocusInWindow() 
        );
    }

    public static void main(String[] args) {
        new PropertyGUI();
    }
}
