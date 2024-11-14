import java.util.ArrayList;
import java.util.Scanner;

// Room base class
class Room {
    protected int roomNumber;
    protected String roomType;
    protected double pricePerNight;
    protected boolean isAvailable;

    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public void bookRoom() throws RoomNotAvailableException {
        if (!isAvailable) {
            throw new RoomNotAvailableException("Room " + roomNumber + " is not available.");
        }
        isAvailable = false;
    }

    public void releaseRoom() {
        isAvailable = true;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}

// Room subclasses
class StandardRoom extends Room {
    public StandardRoom(int roomNumber) {
        super(roomNumber, "Standard", 8000.0);
    }
}

class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber) {
        super(roomNumber, "Deluxe", 12000.0);
    }
}

// Customer class
class Customer {
    private String name;
    private String contactNumber;

    public Customer(String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}

// Booking class
class Booking {
    private Room room;
    private Customer customer;
    private int nights;

    public Booking(Room room, Customer customer, int nights) {
        this.room = room;
        this.customer = customer;
        this.nights = nights;
    }

    public double calculateTotalCost() {
        return room.getPricePerNight() * nights;
    }

    public void confirmBooking() {
        try {
            room.bookRoom();
            System.out.println("\nBooking confirmed for " + customer.getName() + " in Room " + room.roomNumber);
        } catch (RoomNotAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelBooking() {
        room.releaseRoom();
        System.out.println("Booking cancelled for Room " + room.roomNumber);
    }
}

// Custom Exception
class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

// Main class
public class ResortBookingSystem {
    private static ArrayList<Room> rooms = new ArrayList<>();

    public static void main(String[] args) {
        initializeRooms();
        Scanner scanner = new Scanner(System.in);

        boolean keepRunning = true;
        while (keepRunning) {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine();
            System.out.print("Enter contact number: ");
            String contact = scanner.nextLine();
            Customer customer = new Customer(name, contact);

            boolean bookingConfirmed = false;

            while (!bookingConfirmed) {
                System.out.println("Room Types Available:");
                System.out.println("1. Standard Room (Rs.8000 per night)");
                System.out.println("2. Deluxe Room (Rs.12000 per night)");

                int roomChoice;
                while (true) {
                    System.out.print("Choose room type (1 or 2): ");
                    roomChoice = scanner.nextInt();
                    if (roomChoice == 1 || roomChoice == 2) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 for Standard Room or 2 for Deluxe Room.");
                    }
                }

                String selectedRoomType = (roomChoice == 1) ? "Standard" : "Deluxe";
                Room room = findAvailableRoom(selectedRoomType);

                if (room != null) {
                    System.out.print("Enter number of nights: ");
                    int nights = scanner.nextInt();

                    Booking booking = new Booking(room, customer, nights);
                    booking.confirmBooking();
                    System.out.println("Total cost: Rs." + booking.calculateTotalCost());
                    bookingConfirmed = true;
                } else {
                    System.out.println("No available " + selectedRoomType + " rooms. Please try a different room type.");
                }
            }

            // Ask if user wants to make another booking or exit
            System.out.print("\nDo you want to make another booking? (yes/no): ");
            scanner.nextLine(); // Consume the newline character
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("no")) {
                keepRunning = false; // Exit the loop and the program
            }
        }

        scanner.close();
        System.out.println("Thank you for using the Resort Booking System. Goodbye!");
    }

    private static void initializeRooms() {
        rooms.add(new StandardRoom(101));
        rooms.add(new StandardRoom(102));
        rooms.add(new DeluxeRoom(201));
        rooms.add(new DeluxeRoom(202));
    }

    private static Room findAvailableRoom(String roomType) {
        for (Room room : rooms) {
            if (room.roomType.equals(roomType) && room.isAvailable()) {
                return room;
            }
        }
        return null;
    }
}