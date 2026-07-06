package bookstore;

import bookstore.entities.*;
import bookstore.pojos.*;
import bookstore.repositories.IRepository;
import bookstore.repositories.ProductRepository;
import bookstore.services.AutomotiveService;
import bookstore.services.BookstoreService;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
    private final IRepository<ProductEntity> repository;
    private final BookstoreService bookstoreService;
    private final AutomotiveService automotiveService;
    private final CashTill cashTill = new CashTill();
    private final Scanner input = new Scanner(System.in);


    // Constructor Injection of multiple dependencies [6]
    public App(IRepository<ProductEntity> repository,
               BookstoreService bookstoreService,
               AutomotiveService automotiveService) {
        this.repository = repository;
        this.bookstoreService = bookstoreService;
        this.automotiveService = automotiveService;

    }


    public void run() {
        populate();
        int choice = 0;
        while (choice != 99) {
            System.out.println("\n***********************");
            System.out.println(" 1. Add Items (Repository Save)");
            System.out.println(" 2. Edit Items (Repository Save/Update)");
            System.out.println(" 3. Delete Items (Repository Delete)");
            System.out.println(" 4. Sell item(s) (Logic & Repo Sync)");
            System.out.println(" 5. List items (Polymorphic Filtering)");
            System.out.println(" 6. Apply Discount");
            System.out.println("99. Quit");
            System.out.println("***********************");
            System.out.print("Enter choice: \n");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                choice = 0;
            }

// Inside App.java run() menu loop
            switch (choice) {
                case 1: addItem(); break;
                case 2: editItem(); break;
                case 3: deleteItem(); break;
                case 4: sellItem(); break;
                case 5: listAny(); break;
                case 6: discountFeature(); break; // <--- ADD THIS CHOICE [6]
                case 99: break;
                default: System.out.println("Invalid choice.");
            }


        }
    }
// Add to src/main/java/bookstore/App.java

    public void discountFeature() {
        // 1. Fetch clean database entities from the repository
        List<ProductEntity> results = repository.findAll();
        if (results.isEmpty()) {
            System.out.println("No records found to discount.");
            return;
        }

        // 2. Display selection menu
        System.out.println("Select item index to apply a 10% discount:");
        for (int i = 0; i < results.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(results.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < results.size()) {
                Long dbId = results.get(idx).getId();

                // 3. DELEGATE: The Waiter tells the Chef what to do [6]
                bookstoreService.applyDiscount(dbId, 0.10);
            } else {
                System.out.println("Invalid index selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        }
    }


    public void shutdown() {
        repository.close();
        System.out.println("Database connections closed safely.");
    }

    public void addItem() {
        int choice = 0;
        while (choice != 99) {
            System.out.println("\nAdd an item\n");
            System.out.println("1. Add Book");
            System.out.println("2. Add Magazine");
            System.out.println("3. Add DiscMag");
            System.out.println("4. Add Ticket");
            System.out.println("5. Add Tire");
            System.out.println("6. Add Battery");
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) return;

            Editable item = null;
            switch (choice) {
                case 1:
                    item = new Book();
                    break;
                case 2:
                    item = new Magazine();
                    break;
                case 3:
                    item = new DiscMag();
                    break;
                case 4:
                    item = new Ticket();
                    break;
                case 5:
                    item = new Tire();
                    break;
                case 6:
                    item = new Battery();
                    break;
                default:
                    System.out.println("Invalid selection.");
                    continue;
            }

            item.initialize(this.input);
            saveToDb(item);
        }
    }

    private void saveToDb(Editable item) {
        ProductEntity entity = null;
        if (item instanceof Book) {
            entity = ((Book) item).toEntity();
        } else if (item instanceof DiscMag) {
            entity = ((DiscMag) item).toEntity();
        } else if (item instanceof Magazine) {
            entity = ((Magazine) item).toEntity();
        } else if (item instanceof Ticket) {
            entity = ((Ticket) item).toEntity();
        } else if (item instanceof Battery) {
            entity = ((Battery) item).toEntity();
        } else if (item instanceof Tire) {
            entity = ((Tire) item).toEntity();
        }

        if (entity != null) {
            try {
                repository.save(entity);
                System.out.println("Successfully saved to Database via Repository!");
            } catch (Exception e) {
                System.out.println("Error saving entity: " + e.getMessage());
            }
        }
    }

    public void listAny() {
        int choice = 0;
        while (choice != 99) {
            System.out.println("\nAll Items");
            System.out.println("-----------");
            System.out.println("List Options");
            System.out.println("1. All Products");
            System.out.println("2. Books Only");
            System.out.println("3. Magazines Only");
            System.out.println("4. DiscMags Only");
            System.out.println("5. Tickets Only");
            System.out.println("6. Tires Only");
            System.out.println("7. Batteries Only");
            System.out.println("99. Exit");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            if (choice == 99) return;

            Class<? extends ProductEntity> filterClass = null;
            switch (choice) {
                case 1:
                    filterClass = ProductEntity.class;
                    break;
                case 2:
                    filterClass = BookEntity.class;
                    break;
                case 3:
                    filterClass = MagazineEntity.class;
                    break;
                case 4:
                    filterClass = DiscMagEntity.class;
                    break;
                case 5:
                    filterClass = TicketEntity.class;
                    break;
                case 6:
                    filterClass = TireEntity.class;
                    break;
                case 7:
                    filterClass = BatteryEntity.class;
                    break;
                default:
                    System.out.println("Invalid selection.");
                    continue;
            }

            // High-Level collection filtering style [6]
            List<ProductEntity> dbEntities = repository.findAll();
            for (ProductEntity entity : dbEntities) {
                if (!filterClass.isInstance(entity)) {
                    continue;
                }
                if (filterClass == MagazineEntity.class && entity instanceof DiscMagEntity) {
                    continue;
                }
                printEntityAsDto(entity);
            }
        }
    }

    private void printEntityAsDto(ProductEntity entity) {
        if (entity instanceof BookEntity) {
            System.out.println(Book.fromEntity((BookEntity) entity));
        } else if (entity instanceof DiscMagEntity) {
            System.out.println(DiscMag.fromEntity((DiscMagEntity) entity));
        } else if (entity instanceof MagazineEntity) {
            System.out.println(Magazine.fromEntity((MagazineEntity) entity));
        } else if (entity instanceof TicketEntity) {
            System.out.println(Ticket.fromEntity((TicketEntity) entity));
        } else if (entity instanceof BatteryEntity) {
            System.out.println(Battery.fromEntity((BatteryEntity) entity));
        } else if (entity instanceof TireEntity) {
            System.out.println(Tire.fromEntity((TireEntity) entity));
        }
    }

    public void editItem() {
        List<ProductEntity> entities = repository.findAll();
        if (entities.isEmpty()) {
            System.out.println("No records found to edit.");
            return;
        }

        System.out.println("Select item index to edit (0 to " + (entities.size() - 1) + "):");
        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                ProductEntity updatedEntity = null;

                if (entity instanceof BookEntity) {
                    Book dto = Book.fromEntity((BookEntity) entity);
                    dto.edit(this.input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof DiscMagEntity) {
                    DiscMag dto = DiscMag.fromEntity((DiscMagEntity) entity);
                    dto.edit(this.input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof MagazineEntity) {
                    Magazine dto = Magazine.fromEntity((MagazineEntity) entity);
                    dto.edit(this.input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TicketEntity) {
                    Ticket dto = Ticket.fromEntity((TicketEntity) entity);
                    dto.edit(this.input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof BatteryEntity) {
                    Battery dto = Battery.fromEntity((BatteryEntity) entity);
                    dto.edit(this.input);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TireEntity) {
                    Tire dto = Tire.fromEntity((TireEntity) entity);
                    dto.edit(this.input);
                    updatedEntity = dto.toEntity();
                }

                if (updatedEntity != null) {
                    repository.save(updatedEntity);
                    System.out.println("Successfully updated the database via Repository.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid selection or transaction error: " + e.getMessage());
        }
    }

    public void deleteItem() {
        List<ProductEntity> entities = repository.findAll();
        if (entities.isEmpty()) {
            System.out.println("No records found to delete.");
            return;
        }

        System.out.println("Select item index to delete:");
        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                repository.delete(entity.getId());
                System.out.println("Successfully deleted from DB via Repository.");
            }
        } catch (Exception e) {
            System.out.println("Deletion error: " + e.getMessage());
        }
    }

    public void sellItem() {
        List<ProductEntity> entities = repository.findAll();
        if (entities.isEmpty()) {
            System.out.println("No inventory found to sell.");
            return;
        }

        System.out.println("Select item index to sell:");
        for (int i = 0; i < entities.size(); i++) {
            System.out.print(i + ". ");
            printEntityAsDto(entities.get(i));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < entities.size()) {
                ProductEntity entity = entities.get(idx);
                ProductEntity updatedEntity = null;

                if (entity instanceof BookEntity) {
                    Book dto = Book.fromEntity((BookEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof DiscMagEntity) {
                    DiscMag dto = DiscMag.fromEntity((DiscMagEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof MagazineEntity) {
                    Magazine dto = Magazine.fromEntity((MagazineEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TicketEntity) {
                    Ticket dto = Ticket.fromEntity((TicketEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof BatteryEntity) {
                    Battery dto = Battery.fromEntity((BatteryEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                } else if (entity instanceof TireEntity) {
                    Tire dto = Tire.fromEntity((TireEntity) entity);
                    cashTill.sellItem(dto);
                    updatedEntity = dto.toEntity();
                }

                if (updatedEntity != null) {
                    repository.save(updatedEntity);
                    System.out.println("Sale synchronized.");
                }
            }
        } catch (Exception e) {
            System.out.println("Transaction error during purchase: " + e.getMessage());
        }
    }

    public void populate() {
        long count = repository.count();
        if (count > 0) {
            System.out.println("Database already seeded. Safe boot complete.");
            return;
        }

        System.out.println("Seeding database via Repository...");
        Faker faker = new Faker();

        try {
            for (int i = 0; i < 2; i++) {
                BookEntity b = new BookEntity(
                        faker.book().title(),
                        faker.number().randomDouble(2, 10, 50),
                        faker.number().numberBetween(1, 20),
                        faker.book().author()
                );
                repository.save(b);

                MagazineEntity m = new MagazineEntity(
                        faker.book().title() + " Monthly",
                        faker.number().randomDouble(2, 5, 15),
                        faker.number().numberBetween(5, 50),
                        faker.number().numberBetween(100, 500),
                        faker.date().past(30, TimeUnit.DAYS)
                );
                repository.save(m);

                DiscMagEntity dm = new DiscMagEntity(
                        "Tech Disc: " + faker.app().name(),
                        faker.number().randomDouble(2, 10, 25),
                        faker.number().numberBetween(5, 30),
                        faker.number().numberBetween(50, 200),
                        faker.date().past(60, TimeUnit.DAYS),
                        faker.bool().bool()
                );
                repository.save(dm);

                TicketEntity t = new TicketEntity();
                t.setDescription("Concert: " + faker.rockBand().name());
                t.setPrice(faker.number().randomDouble(2, 50, 150));
                repository.save(t);

                TireEntity tire = new TireEntity(
                        faker.company().name() + " Tires",
                        faker.number().randomDouble(2, 80, 350),
                        faker.number().numberBetween(15, 22)
                );
                repository.save(tire);

                BatteryEntity battery = new BatteryEntity(
                        faker.company().name() + " Batteries",
                        faker.number().randomDouble(2, 100, 250),
                        faker.number().numberBetween(500, 950)
                );
                repository.save(battery);
            }
            System.out.println("Seeding complete. All products persisted directly to database via Repository.");
        } catch (Exception e) {
            System.out.println("Failure during database seeding: " + e.getMessage());
        }
    }

// ✅ Optimized Database Lookup (Loads exactly 1 row)
// Open src/main/java/bookstore/App.java

    public SaleableItem findItem(SaleableItem item) {
        // 1. Try finding by UUID first (Production Optimized lookup) [7]
        if (item instanceof Product) {
            String uuid = ((Product) item).getProductId();
            ProductEntity entity = repository.findByProductId(uuid);

            if (entity != null) {
                // Map the single retrieved entity back to its corresponding DTO
                if (entity instanceof BookEntity) {
                    return Book.fromEntity((BookEntity) entity);
                } else if (entity instanceof DiscMagEntity) {
                    return DiscMag.fromEntity((DiscMagEntity) entity);
                } else if (entity instanceof MagazineEntity) {
                    return Magazine.fromEntity((MagazineEntity) entity);
                } else if (entity instanceof TicketEntity) {
                    return Ticket.fromEntity((TicketEntity) entity);
                } else if (entity instanceof BatteryEntity) {
                    return Battery.fromEntity((BatteryEntity) entity);
                } else if (entity instanceof TireEntity) {
                    return Tire.fromEntity((TireEntity) entity);
                }
            }
        }

        // 2. Fallback: Perform a logical scan using logical equality [6]
        // This is necessary for unit tests (like AppTest) where the "expected" POJO
        // contains a different randomly-generated UUID but matches the content [6].
        List<ProductEntity> entities = repository.findAll();
        for (ProductEntity entity : entities) {
            SaleableItem pojo = null;

            if (entity instanceof BookEntity) {
                pojo = Book.fromEntity((BookEntity) entity);
            } else if (entity instanceof DiscMagEntity) {
                pojo = DiscMag.fromEntity((DiscMagEntity) entity);
            } else if (entity instanceof MagazineEntity) {
                pojo = Magazine.fromEntity((MagazineEntity) entity);
            } else if (entity instanceof TicketEntity) {
                pojo = Ticket.fromEntity((TicketEntity) entity);
            } else if (entity instanceof BatteryEntity) {
                pojo = Battery.fromEntity((BatteryEntity) entity);
            } else if (entity instanceof TireEntity) {
                pojo = Tire.fromEntity((TireEntity) entity);
            }

            if (pojo != null && pojo.equals(item)) {
                return pojo; // Returns match based on business properties [6]
            }
        }
        return null;
    }
    public boolean findItemExists(SaleableItem item) {
        return findItem(item) != null;
    }

    public SaleableItem getItem(SaleableItem item) {
        return findItem(item);
    }
}
