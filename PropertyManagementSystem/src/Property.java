/**
 * Represents a property in the Property Management System.
 * Stores information such as the address, property type,
 * nightly rate, bedrooms, availability, and owner.
 */

public class Property {

    // Variables to store property information
    private int propertyId;
    private String address;
    private String propertyType;
    private double nightlyRate;
    private int bedrooms;
    private boolean available;
    private String owner;

    /**
     * Creates a new property with the provided information.
     *
     * @param propertyId the unique ID of the property
     * @param address the property address
     * @param propertyType the type of property
     * @param nightlyRate the cost per night
     * @param bedrooms the number of bedrooms
     * @param available whether the property is available
     * @param owner the name of the property owner
     */
    public Property(int propertyId, String address,
                    String propertyType, double nightlyRate,
                    int bedrooms, boolean available,
                    String owner) {

        this.propertyId = propertyId;
        this.address = address;
        this.propertyType = propertyType;
        this.nightlyRate = nightlyRate;
        this.bedrooms = bedrooms;
        this.available = available;
        this.owner = owner;
    }

    /**
     * Gets the property ID.
     *
     * @return the property ID
     */

    public int getPropertyId() {
        return propertyId;
    }

     /**
     * Gets the property address.
     *
     * @return the property address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the property type.
     *
     * @return the property type
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Gets the nightly rate.
     *
     * @return the nightly rate
     */
    public double getNightlyRate() {
        return nightlyRate;
    }

    /**
     * Gets the number of bedrooms.
     *
     * @return the number of bedrooms
     */
    public int getBedrooms() {
        return bedrooms;
    }

    /**
     * Checks if the property is available.
     *
     * @return true if the property is available, false otherwise
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Gets the name of the property owner.
     *
     * @return the name of the property owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Updates the property ID if it is greater than zero.
     *
     * @param propertyId the new property ID
     * @return true if updated, otherwise false
     */
    public boolean setPropertyId(int propertyId) {
        if (propertyId <= 0) {
            return false;
        }
        this.propertyId = propertyId;
        return true;
    }

    /**
     * Updates the property address if it is not null or blank.
     *
     * @param address the new property address
     * @return true if updated, otherwise false
     */
    public boolean setAddress(String address) {
        if (address == null || address.isBlank()) {
            return false;
        }
        this.address = address;
        return true;
    }

    /**
     * Updates the property type if it is not null or blank.
     *
     * @param propertyType the new property type
     * @return true if updated, otherwise false
     */
    public boolean setPropertyType(String propertyType) {
        if (propertyType == null || propertyType.isBlank()) {
            return false;
        }
        this.propertyType = propertyType;
        return true;
    }

    /**
     * Updates the nightly rate if it is a finite positive value.
     *
     * @param nightlyRate the new nightly rate
     * @return true if updated, otherwise false
     */
    public boolean setNightlyRate(double nightlyRate) {
        if (!Double.isFinite(nightlyRate) || nightlyRate <= 0) {
            return false;
        }
        this.nightlyRate = nightlyRate;
        return true;
    }

    /** 
     * Updates the number of bedrooms if it is non-negative.
     *
     * @param bedrooms the new number of bedrooms
     * @return true if updated, otherwise false
     */
    public boolean setBedrooms(int bedrooms) {
        if (bedrooms < 0) {
            return false;
        }
        this.bedrooms = bedrooms;
        return true;
    }

    /**
     * Updates the availability status of the property.
     *
     * @param available the new availability status
     * @return true if updated, otherwise false
     */
    public boolean setAvailable(boolean available) {
        this.available = available;
        return true;
    }

    /**
     * Updates the property owner if it is not null or blank.
     *
     * @param owner the new property owner
     * @return true if updated, otherwise false
     */
    public boolean setOwner(String owner) {
        if (owner == null || owner.isBlank()) {
            return false;
        }
        this.owner = owner;
        return true;
    }

    /**
     * Creates a text description of the property.
     * Includes the property ID, address, type, nightly rate,
     * number of bedrooms, availability, and owner.
     *
     * @return a formatted string containing the property information
     */
    @Override
    public String toString() {
        
        return "Property ID: " + propertyId
                + "\nAddress: " + address
                + "\nProperty Type: " + propertyType
                + "\nNightly Rate: $" + nightlyRate
                + "\nBedrooms: " + bedrooms
                + "\nAvailability: " + (available ? "Available" : "Not Available")
                + "\nOwner: " + owner
                + "\n-------------------------";
    }
}
