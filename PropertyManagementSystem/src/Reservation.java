
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    // Information about the reservation
    private int propertyId;
    private String guestName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double nightlyRate;

    // Constructor to create a reservation
    public Reservation(int propertyId, String guestName,
                       LocalDate checkIn, LocalDate checkOut,
                       double nightlyRate) {

        this.propertyId = propertyId;
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.nightlyRate = nightlyRate;
    }

    // Get reservation information
    public int getPropertyId() {
        return propertyId;
    }

    public String getGuestName() {
    return guestName;
    }

    public double getNightlyRate() {
    return nightlyRate;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    // Calculate number of nights
    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    // Calculate total reservation cost
    public double getTotalCost() {
        return getNights() * nightlyRate;
    }

    // Display reservation information
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
