import java.util.*;

// Booking Request Model
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> roomInventory = new HashMap<>();

    public InventoryService() {
        roomInventory.put("Single", 2);
        roomInventory.put("Double", 2);
    }

    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + roomInventory);
    }
}

// Booking Service
class BookingService {
    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToRooms = new HashMap<>();
    private InventoryService inventoryService;
    private int roomCounter = 1;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void addBookingRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    private String generateRoomId(String roomType) {
        return roomType.substring(0, 1).toUpperCase() + roomCounter++;
    }

    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();
            System.out.println("\nProcessing booking for: " + request.customerName);

            if (!inventoryService.isAvailable(request.roomType)) {
                System.out.println("No rooms available for type: " + request.roomType);
                continue;
            }

            // Generate unique room ID
            String roomId;
            do {
                roomId = generateRoomId(request.roomType);
            } while (allocatedRoomIds.contains(roomId));

            // Atomic Allocation Logic
            allocatedRoomIds.add(roomId);
            roomTypeToRooms
                    .computeIfAbsent(request.roomType, k -> new HashSet<>())
                    .add(roomId);

            inventoryService.decrementRoom(request.roomType);

            System.out.println("Booking Confirmed!");
            System.out.println("Customer: " + request.customerName);
            System.out.println("Room Type: " + request.roomType);
            System.out.println("Allocated Room ID: " + roomId);

            inventoryService.displayInventory();
        }
    }

    public void displayAllocations() {
        System.out.println("\nFinal Room Allocations:");
        for (String type : roomTypeToRooms.keySet()) {
            System.out.println(type + " Rooms: " + roomTypeToRooms.get(type));
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Adding booking requests (FIFO)
        bookingService.addBookingRequest(new BookingRequest("Alice", "Single"));
        bookingService.addBookingRequest(new BookingRequest("Bob", "Double"));
        bookingService.addBookingRequest(new BookingRequest("Charlie", "Single"));
        bookingService.addBookingRequest(new BookingRequest("David", "Single")); // may fail

        // Process bookings
        bookingService.processBookings();

        // Display final allocations
        bookingService.displayAllocations();
    }
}