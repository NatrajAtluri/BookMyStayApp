import java.util.LinkedList;
import java.util.Queue;

// Represents a guest booking request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName + ", RoomType=" + roomType + "]";
    }
}

// Handles booking request queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View all queued requests
    public void viewRequests() {
        if (queue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests in Queue (FIFO Order):");
        for (Reservation r : queue) {
            System.out.println(r);
        }
    }

    // Get next request (without removing)
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Process next request (removes from queue)
    public Reservation processNextRequest() {
        return queue.poll();
    }
}

// Main Application
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulating incoming booking requests
        requestQueue.addRequest(new Reservation("Alice", "Deluxe"));
        requestQueue.addRequest(new Reservation("Bob", "Suite"));
        requestQueue.addRequest(new Reservation("Charlie", "Standard"));

        // View all requests
        requestQueue.viewRequests();

        // Peek next request (FIFO)
        System.out.println("\nNext request to process: " + requestQueue.peekNextRequest());

        // Process requests one by one
        System.out.println("\nProcessing requests...");
        while (requestQueue.peekNextRequest() != null) {
            Reservation processed = requestQueue.processNextRequest();
            System.out.println("Processed: " + processed);
        }

        // Final state
        requestQueue.viewRequests();
    }
}