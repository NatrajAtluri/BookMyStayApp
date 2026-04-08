import java.util.*;

// Custom Exception
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Reservation Entity
class Reservation {
    String id;
    String customerName;
    String roomType;
    boolean isCancelled;

    public Reservation(String id, String customerName, String roomType) {
        this.id = id;
        this.customerName = customerName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    @Override
    public String toString() {
        return id + " | " + customerName + " | " + roomType +
                (isCancelled ? " (CANCELLED)" : " (CONFIRMED)");
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

    public void allocateRoom(String roomType) throws BookingException {
        if (!rooms.containsKey(roomType)) {
            throw new BookingException("Invalid room type: " + roomType);
        }

        int available = rooms.get(roomType);
        if (available <= 0) {
            throw new BookingException("No rooms available for: " + roomType);
        }

        rooms.put(roomType, available - 1);
    }

    public void releaseRoom(String roomType) {
        rooms.put(roomType, rooms.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + rooms);
    }
}

// Booking Service
class BookingService {
    private Map<String, Reservation> reservations = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Create Booking
    public void createBooking(String id, String name, String roomType) {
        try {
            inventory.allocateRoom(roomType);

            Reservation r = new Reservation(id, name, roomType);
            reservations.put(id, r);

            System.out.println("Booking Confirmed: " + r);

        } catch (BookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    // Cancel Booking (Rollback)
    public void cancelBooking(String id) {
        try {
            // Validate existence
            if (!reservations.containsKey(id)) {
                throw new BookingException("Reservation not found: " + id);
            }

            Reservation r = reservations.get(id);

            // Prevent duplicate cancellation
            if (r.isCancelled) {
                throw new BookingException("Reservation already cancelled: " + id);
            }

            // Step 1: Record rollback (LIFO)
            rollbackStack.push(id);

            // Step 2: Restore inventory
            inventory.releaseRoom(r.roomType);

            // Step 3: Update state
            r.isCancelled = true;

            System.out.println("Booking Cancelled: " + r);

        } catch (BookingException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    public void showHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack (LIFO): " + rollbackStack);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Create bookings
        service.createBooking("R1", "Alice", "Deluxe");
        service.createBooking("R2", "Bob", "Suite");

        // Cancel booking
        service.cancelBooking("R1");

        // Invalid cancellation
        service.cancelBooking("R1"); // already cancelled
        service.cancelBooking("R5"); // not found

        // Display system state
        inventory.displayInventory();
        service.showHistory();
        service.showRollbackStack();
    }
}