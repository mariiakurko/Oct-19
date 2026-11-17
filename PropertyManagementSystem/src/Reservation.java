import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a guest reservation in the Property Management System.
 * Stores the property ID, guest name, check-in and check-out dates,
 * and nightly rate.
 */
public class Reservation {

    // Information about the reservation
    private int propertyId;
    private String guestName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double nightlyRate;

    /**
     * Creates a reservation with the provided guest and property information.
     *
     * @param propertyId the ID of the reserved property
     * @param guestName the name of the guest
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @param nightlyRate the cost per night
     */
    public Reservation(int propertyId, String guestName,
                       LocalDate checkIn, LocalDate checkOut,
                       double nightlyRate) {

        this.propertyId = propertyId;
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.nightlyRate = nightlyRate;
    }

    /**
     * Gets the ID of the reserved property.
     *
     * @return the property ID
     */
    public int getPropertyId() {
        return propertyId;
    }

    /**
     * Gets the name of the guest who made the reservation.
     *
     * @return the guest name
     */
    public String getGuestName() {
    return guestName;
    }

    /**
     * Gets the nightly rate for the reserved property.
     *
     * @return the nightly rate
     */
    public double getNightlyRate() {
    return nightlyRate;
    }
    
    /**
     * Gets the reservation check-in date.
     *
     * @return the check-in date
     */
    public LocalDate getCheckIn() {
        return checkIn;
    }

    /**
     * Gets the reservation check-out date.
     *
     * @return the check-out date
     */
    public LocalDate getCheckOut() {
        return checkOut;
    }

    /**
     * Calculates the number of nights between check-in and check-out.
     *
     * @return the total number of nights
     */
    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    
    /**
     * Calculates the total cost of the reservation.
     *
     * @return the total cost
     */
    public double getTotalCost() {
        return getNights() * nightlyRate;
    }

    /**
     * Creates a text description of the reservation.
     * Includes the property ID, guest name, check-in and check-out dates,
     * number of nights, nightly rate, and total cost.
     *
     * @return a formatted string containing the reservation details
     */
    @Override
    public String toString() {
        return "\n===== RESERVATION DETAILS ====="
                + "\nProperty ID: " + propertyId
                + "\nGuest Name: " + guestName
                + "\nCheck-in: " + checkIn
                + "\nCheck-out: " + checkOut
                + "\nNumber of Nights: " + getNights()
                + "\nNightly Rate: $" + nightlyRate
                + "\nTotal Cost: $" + getTotalCost()
                + "\n==============================";
    }
}
