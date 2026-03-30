import java.util.*;

// Reservation Model (from previous use case)
class Reservation {
    String reservationId;
    String customerName;
    String roomType;

    public Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Add-On Service Model
class AddOnService {
    String serviceName;
    double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationId, List of Services>
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Add services to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0;
        for (AddOnService service : services) {
            total += service.cost;
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        System.out.println("\nServices for Reservation ID: " + reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        System.out.println("Total Add-On Cost: $" + calculateTotalServiceCost(reservationId));
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Sample reservations (already confirmed from Use Case 6)
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Double");

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects services
        serviceManager.addService(r1.reservationId, new AddOnService("Breakfast", 20));
        serviceManager.addService(r1.reservationId, new AddOnService("Airport Pickup", 50));

        serviceManager.addService(r2.reservationId, new AddOnService("Extra Bed", 30));

        // Display services
        serviceManager.displayServices(r1.reservationId);
        serviceManager.displayServices(r2.reservationId);
    }
}