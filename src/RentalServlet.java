// Car.java, Customer.java, and Rental.java classes remain mostly the same

// CarRentalSystem.java - Modified to remove console-specific code
class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    // Other methods remain the same
    // Add getters for cars, customers, and rentals lists
    public List<Car> getAllCars() {
        return cars;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Rental> getRentals() {
        return rentals;
    }
}

// Create a servlet to handle requests
@WebServlet("/rental")
public class RentalServlet extends HttpServlet {
    private CarRentalSystem rentalSystem;

    @Override
    public void init() {
        rentalSystem = new CarRentalSystem();
        // Initialize with some cars
        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("rent".equals(action)) {
            String customerName = request.getParameter("customerName");
            String carId = request.getParameter("carId");
            int days = Integer.parseInt(request.getParameter("days"));
            
            Customer customer = new Customer("CUS" + (rentalSystem.getCustomers().size() + 1), customerName);
            Car selectedCar = findCarById(carId);
            
            if (selectedCar != null && selectedCar.isAvailable()) {
                rentalSystem.rentCar(selectedCar, customer, days);
                response.sendRedirect("success.jsp");
            } else {
                response.sendRedirect("error.jsp");
            }
        } else if ("return".equals(action)) {
            String carId = request.getParameter("carId");
            Car carToReturn = findCarById(carId);
            
            if (carToReturn != null) {
                rentalSystem.returnCar(carToReturn);
                response.sendRedirect("success.jsp");
            } else {
                response.sendRedirect("error.jsp");
            }
        }
    }

    private Car findCarById(String carId) {
        for (Car car : rentalSystem.getAllCars()) {
            if (car.getCarId().equals(carId)) {
                return car;
            }
        }
        return null;
    }
}
