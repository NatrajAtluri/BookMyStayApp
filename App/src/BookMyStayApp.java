import java.util.HashMap;
import java.util.Map;

/**
 * The RoomInventory class encapsulates the logic for managing room availability.
 * It uses a HashMap to provide O(1) lookup and update performance.
 * * @author Atluri Natraj
 * @version 3.0
 */
class RoomInventory {
    // HashMap to map Room Type (String) to Available Count (Integer)
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    /**
     * Registers or updates a room type in the inventory.
     */
    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    /**
     * Retrieves the current availability for a specific room type.
     */
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    /**
     * Updates availability (e.g., after a booking or cancellation).
     */
    public void updateAvailability(String type, int change) {
        if (inventory.containsKey(type)) {
            int current = inventory.get(type);
            inventory.put(type, current + change);
        }
    }

    /**
     * Displays the complete status of the inventory.
     */
    public void displayInventory() {
        System.out.println("Current Inventory Status:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}

/**
 * Main application class: BookMyStayApp
 * Demonstrates centralized inventory management using HashMap.
 */
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App v3.0     ");
        System.out.println("========================================");

        // 1. Initialize Centralized Inventory
        RoomInventory hotelInventory = new RoomInventory();

        // 2. Register Room Types (Populating the HashMap)
        hotelInventory.addRoomType("Single Room", 10);
        hotelInventory.addRoomType("Double Room", 5);
        hotelInventory.addRoomType("Suite Room", 2);

        // 3. Display Initial State
        hotelInventory.displayInventory();

        // 4. Demonstrate Controlled Updates
        System.out.println("\n--- Processing a Booking for Double Room ---");
        hotelInventory.updateAvailability("Double Room", -1);

        // 5. Retrieve Specific State
        int currentDouble = hotelInventory.getAvailability("Double Room");
        System.out.println("Updated Double Room count: " + currentDouble);

        System.out.println("\n--- Final System State ---");
        hotelInventory.displayInventory();
        System.out.println("========================================");
    }
}