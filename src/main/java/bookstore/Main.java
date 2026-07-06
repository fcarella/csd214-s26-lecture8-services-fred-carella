package bookstore;

import bookstore.entities.ProductEntity;
import bookstore.repositories.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner bootInput = new Scanner(System.in);
        System.out.println("=== Lab 5: System Boot & Architecture Configuration ===");
        System.out.println("1. Volatile RAM (ArrayList - Sequential)");
        System.out.println("2. Volatile RAM (HashMap - Indexed)");
        System.out.println("3. Integration Testing (H2 In-Memory SQL)");
        System.out.println("4. Production (MySQL Database - Port 3333)");
        System.out.print("Select active storage engine: ");

        int choice = bootInput.nextInt();
        IRepository<ProductEntity> selectedRepo = null;

        // PHASE 1: Data-access strategy selection
        switch (choice) {
            case 1: selectedRepo = new InMemoryListRepository(); break;
            case 2: selectedRepo = new InMemoryMapRepository(); break;
            case 3: selectedRepo = new H2Repository(); break;
            case 4: selectedRepo = new MySqlRepository(); break;
            default:
                System.out.println("Invalid selection. Defaulting to Volatile RAM List.");
                selectedRepo = new InMemoryListRepository();
        }

        // Print active strategy
        System.out.println("\nActive engine: " + selectedRepo.getDataSourceType() + "\n");

        // PHASE 2: Assembly & Dependency Injection [6]
        App app = new App(selectedRepo);

        // PHASE 3: Execution & Resource Management
        try {
            app.run();
        } finally {
            app.shutdown(); // Ensures repository resources are closed safely [7]
        }
    }
}
