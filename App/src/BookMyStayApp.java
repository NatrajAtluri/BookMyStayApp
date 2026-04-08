import java.util.*;

// Reservation Entity
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;
    private int nights;
    private double pricePerNight;

    public Reservation(String reservationId, String customerName,
                       String roomType, int nights, double pricePerNight) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    public double getTotalAmount() {
        return nights * pricePerNight;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + reservationId + '\'' +
                ", customer='" + customerName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", nights=" + nights +
                ", total=" + getTotalAmount() +
                '}';
    }
}

// Booking History (Data Storage)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Store confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all bookings (read-only view)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }
}

// Reporting Service (No modification of data)
class BookingReportService {

    // Print all bookings
    public void printAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalAmount();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: $" + totalRevenue);

        System.out.println("\nRoom Type Distribution:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + ": " + roomTypeCount.get(type));
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe", 2, 120);
        Reservation r2 = new Reservation("R102", "Bob", "Standard", 3, 80);
        Reservation r3 = new Reservation("R103", "Charlie", "Suite", 1, 200);

        // Step 1: Confirm booking → add to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Step 2: Admin retrieves booking history
        List<Reservation> bookings = history.getAllReservations();

        // Step 3: Reporting
        reportService.printAllBookings(bookings);
        reportService.generateSummary(bookings);
    }
}