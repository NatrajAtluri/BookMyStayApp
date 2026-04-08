import java.io.*;
import java.util.*;

// Reservation Entity (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    String id;
    String customerName;
    String roomType;

    public Reservation(String id, String customerName, String roomType) {
        this.id = id;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return id + " | " + customerName + " | " + roomType;
    }
}

// Wrapper class for persistence (Inventory + Bookings)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "bookings.dat";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) in.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading data. Starting with safe defaults.");
        }

        // Return default state if failure
        Map<String, Integer> defaultInventory = new HashMap<>();
        defaultInventory.put("Standard", 2);
        defaultInventory.put("Deluxe", 2);
        defaultInventory.put("Suite", 1);

        return new SystemState(defaultInventory, new ArrayList<>());
    }
}

// Booking Service
class BookingService {

    private Map<String, Integer> inventory;
    private List<Reservation> bookings;

    public BookingService(SystemState state) {
        this.inventory = state.inventory;
        this.bookings = state.bookings;
    }

    public void createBooking(String id, String name, String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            System.out.println("Booking Failed: No rooms available for " + roomType);
            return;
        }

        inventory.put(roomType, available - 1);
        Reservation r = new Reservation(id, name, roomType);
        bookings.add(r);

        System.out.println("Booking Confirmed: " + r);
    }

    public void showState() {
        System.out.println("\n--- Current Inventory ---");
        System.out.println(inventory);

        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    public SystemState getState() {
        return new SystemState(inventory, bookings);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Load previous state
        SystemState state = PersistenceService.load();

        BookingService service = new BookingService(state);

        // Step 2: Perform operations
        service.createBooking("R1", "Alice", "Deluxe");
        service.createBooking("R2", "Bob", "Suite");
        service.createBooking("R3", "Charlie", "Suite"); // may fail if no inventory

        // Step 3: Display current state
        service.showState();

        // Step 4: Save state before shutdown
        PersistenceService.save(service.getState());
    }
}