import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-safe Inventory
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String roomType, String guest) {
        int available = rooms.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName()
                    + " allocating " + roomType + " to " + guest);

            rooms.put(roomType, available - 1);

            // Simulate delay (to expose race condition if not synchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            return true;
        } else {
            System.out.println(Thread.currentThread().getName()
                    + " FAILED for " + guest + " (No " + roomType + " available)");
            return false;
        }
    }

    public void showInventory() {
        System.out.println("Final Inventory: " + rooms);
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notify(); // notify waiting threads
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // wait if empty
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) { // each thread processes 3 requests
            BookingRequest request = queue.getRequest();
            inventory.allocateRoom(request.roomType, request.guestName);
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Add booking requests (simulating multiple guests)
        queue.addRequest(new BookingRequest("Alice", "Deluxe"));
        queue.addRequest(new BookingRequest("Bob", "Suite"));
        queue.addRequest(new BookingRequest("Charlie", "Deluxe"));
        queue.addRequest(new BookingRequest("David", "Suite")); // should fail
        queue.addRequest(new BookingRequest("Eve", "Standard"));
        queue.addRequest(new BookingRequest("Frank", "Standard"));

        // Create multiple threads (concurrent processors)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.showInventory();
    }
}