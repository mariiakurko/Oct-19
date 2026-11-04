
public class Property {

    // Variables to store property information
    private int propertyId;
    private String address;
    private String propertyType;
    private double nightlyRate;
    private int bedrooms;
    private boolean available;
    private String owner;

    // Constructor to create a property
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

    // Get property information
    public int getPropertyId() {
        return propertyId;
    }

    public String getAddress() {
        return address;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public double getNightlyRate() {
        return nightlyRate;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getOwner() {
        return owner;
    }

    // Update property information
    public boolean setPropertyId(int propertyId) {
        if (propertyId <= 0) {
            return false;
        }
        this.propertyId = propertyId;
        return true;
    }

    public boolean setAddress(String address) {
        if (address == null || address.isBlank()) {
            return false;
        }
        this.address = address;
        return true;
    }

    public boolean setPropertyType(String propertyType) {
        if (propertyType == null || propertyType.isBlank()) {
            return false;
        }
        this.propertyType = propertyType;
        return true;
    }

    public boolean setNightlyRate(double nightlyRate) {
        if (!Double.isFinite(nightlyRate) || nightlyRate <= 0) {
            return false;
        }
        this.nightlyRate = nightlyRate;
        return true;
    }

    public boolean setBedrooms(int bedrooms) {
        if (bedrooms < 0) {
            return false;
        }
        this.bedrooms = bedrooms;
        return true;
    }

    public boolean setAvailable(boolean available) {
        this.available = available;
        return true;
    }

    public boolean setOwner(String owner) {
        if (owner == null || owner.isBlank()) {
            return false;
        }
        this.owner = owner;
        return true;
    }

    // Display property information
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
