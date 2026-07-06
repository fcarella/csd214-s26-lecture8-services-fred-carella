package bookstore;

import bookstore.entities.ProductEntity;
import bookstore.repositories.*;
import bookstore.services.AutomotiveService;
import bookstore.services.BookstoreService;
import java.util.Scanner;

/**
 * Main: The Manual IoC Container / System Assembler [6].
 * Responsible for selecting the data infrastructure, instantiating the
 * business logic services, and injecting them into the presentation layer [6].
 */
public class Main {
    public static void main(String[] args) {
        Scanner bootInput = new Scanner(System.in);
        System.out.println("=== S26 Lecture 8: System Boot & Architecture Configuration ===");
        System.out.println("1. Volatile RAM (ArrayList - Sequential)");
        System.out.println("2. Volatile RAM (HashMap - Indexed)");
        System.out.println("3. Integration Testing (H2 In-Memory SQL)");
        System.out.println("4. Production (MySQL Database - Port 3333)");
        System.out.print("Select active storage engine: ");

        int choice = bootInput.nextInt();
        IRepository<ProductEntity> selectedRepo = null;

        // PHASE 1: Data-access strategy selection
        switch (choice) {
            case 1:
                selectedRepo = new InMemoryListRepository();
                break;
            case 2:
                selectedRepo = new InMemoryMapRepository();
                break;
            case 3:
                selectedRepo = new H2Repository();
                break;
            case 4:
                selectedRepo = new MySqlRepository();
                break;
            default:
                System.out.println("Invalid selection. Defaulting to Volatile RAM List.");
                selectedRepo = new InMemoryListRepository();
        }

        // Print active strategy
        System.out.println("\nActive engine: " + selectedRepo.getDataSourceType() + "\n");

// Inside Main.java (The Wiring Phase)

// PHASE 2: Bean Creation (Instantiate generalist and specialist services)
        BookstoreService bookstoreService = new BookstoreService(selectedRepo);
        AutomotiveService automotiveService = new AutomotiveService(selectedRepo);

// PHASE 3: Final Assembly (Inject all three dependencies into App)
        App app = new App(selectedRepo, bookstoreService, automotiveService);

        try {
            app.run();
        } finally {
            app.shutdown();
        }


        // PHASE 4: Execution & Resource Management
        try {
            app.run();
        } finally {
            app.shutdown(); // Ensures database or file-socket resources are closed safely [7]
        }
    }
}
