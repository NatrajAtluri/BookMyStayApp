import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Entity
class Reservation {
    String id;
    String customerName;
    String roomType;
    int nights;

    public Reservation(String id, String customerName, String roomType, int nights) {
        this.id = id;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    @Override
    public String toString() {
        return id + " | " + customerName + " | " + roomType + " | Nights: " + nights;
    }
}

// Inventory Management
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    public void validateAndReserve(String roomType) throws InvalidBookingException {

        // Validate room type
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = rooms.get(roomType);

        // Prevent negative inventory
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        // Safe update
        rooms.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + rooms);
    }
}

// Validator Class
class BookingValidator {

    public static void validateInput(String customerName, String roomType, int nights)
            throws InvalidBookingException {

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty");
        }

        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than 0");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;
    private List<Reservation> history;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.history = new ArrayList<>();
    }

    public void createBooking(String id, String name, String roomType, int nights) {
        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validateInput(name, roomType, nights);

            // Step 2: Validate system state
            inventory.validateAndReserve(roomType);

            // Step 3: Create booking
            Reservation reservation = new Reservation(id, name, roomType, nights);
            history.add(reservation);

            System.out.println("Booking Confirmed: " + reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    public void showHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Valid booking
        service.createBooking("R1", "Alice", "Deluxe", 2);

        // Invalid room type
        service.createBooking("R2", "Bob", "Premium", 1);

        // Invalid nights
        service.createBooking("R3", "Charlie", "Standard", 0);

        // Exhaust inventory
        service.createBooking("R4", "David", "Suite", 1);
        service.createBooking("R5", "Eve", "Suite", 1); // should fail

        // Empty name
        service.createBooking("R6", "", "Standard", 1);

        // Display results
        inventory.displayInventory();
        service.showHistory();
    }
}