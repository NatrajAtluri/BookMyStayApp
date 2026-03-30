import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }


    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public void addRoom(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    // Read-only snapshot (prevents modification)
    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(roomAvailability);
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public List<Room> searchAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {
            String roomType = entry.getKey();
            int count = entry.getValue();

            // Defensive check
            if (count <= 0) continue;

            Room room = roomCatalog.get(roomType);
            if (room != null) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }
}

// Main Application
public class BookMyStayApp {
    public static void main(String[] args) {

        // Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Deluxe", 3);
        inventory.addRoom("Suite", 0);       // unavailable
        inventory.addRoom("Standard", 5);

        // Setup Room Catalog
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Deluxe",
                new Room("Deluxe", 4500,
                        Arrays.asList("WiFi", "TV", "Mini Bar")));

        roomCatalog.put("Suite",
                new Room("Suite", 8000,
                        Arrays.asList("WiFi", "TV", "Jacuzzi")));

        roomCatalog.put("Standard",
                new Room("Standard", 2500,
                        Arrays.asList("WiFi", "Fan")));

        // Search Service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Guest performs search
        System.out.println("Available Rooms:\n");

        List<Room> results = searchService.searchAvailableRooms();

        for (Room room : results) {
            System.out.println("Room Type: " + room.getType());
            System.out.println("Price: ₹" + room.getPrice());
            System.out.println("Amenities: " + room.getAmenities());
            System.out.println("-----------------------------");
        }
    }
}