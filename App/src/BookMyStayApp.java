/**
 * Abstract class representing a general Room in the Hotel Booking System.
 * Encapsulates common properties like type and price.
 * * @author Atluri Natraj
 * @version 2.0
 */
abstract class Room {
    private String roomType;
    private double pricePerNight;

    public Room(String roomType, double pricePerNight) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    /**
     * Abstract method to be implemented by concrete subclasses
     * to display unique room features.
     */
    public abstract void displayRoomFeatures();
}

/** Concrete class representing a Single Room */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1000.0);
    }

    @Override
    public void displayRoomFeatures() {
        System.out.println("Features: Single Bed, Free WiFi, Standard Desk.");
    }
}

/** Concrete class representing a Double Room */
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 1800.0);
    }

    @Override
    public void displayRoomFeatures() {
        System.out.println("Features: Double Bed, Mini Fridge, City View.");
    }
}

/** Concrete class representing a Suite Room */
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3500.0);
    }

    @Override
    public void displayRoomFeatures() {
        System.out.println("Features: King Size Bed, Private Lounge, Ocean View.");
    }
}

/**
 * Main application class: BookMyStayApp
 * Demonstrates Room initialization and static inventory management.
 * * @version 2.0
 */
public class BookMyStayApp {
    public static void main(String[] args) {
        // Welcome Message (Refined from Use Case 1)
        System.out.println("========================================");
        System.out.println("   Welcome to Book My Stay App v2.0     ");
        System.out.println("========================================");

        // 1. Object Modeling: Creating room instances via Polymorphism
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // 2. Static Availability: Using primitive variables (Manual state management)
        int singleAvailable = 10;
        int doubleAvailable = 5;
        int suiteAvailable = 2;

        // 3. Displaying System State
        printRoomDetails(single, singleAvailable);
        printRoomDetails(doubleRoom, doubleAvailable);
        printRoomDetails(suite, suiteAvailable);

        System.out.println("\nSystem setup complete. No errors found.");
        System.out.println("========================================");
    }

    /**
     * Helper method that accepts a generic Room type (Polymorphism).
     */
    private static void printRoomDetails(Room room, int count) {
        System.out.println("\nRoom: " + room.getRoomType());
        System.out.println("Rate: " + room.getPricePerNight() + " / night");
        room.displayRoomFeatures();
        System.out.println("Current Inventory: " + count);
    }
}