package bookstore.pojos;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TireTest {
    @Test
    void testTireEdit() {
        //..

    }

        @Test
    void testTireInitialization() {
        // 1. Prepare the Script (Title -> Manufacturer -> Price -> Diameter)
        // Based on the "Bucket Brigade" order in VehiclePart and Tire
        String inputData = "Michelin\n250.00\n18\n";

        // 2. Create the Mock Dependency
        Scanner mockScanner = new Scanner(new ByteArrayInputStream(inputData.getBytes()));

        // 3. Execute
        Tire tire = new Tire();
        tire.initialize(mockScanner); // Injection!

        // 4. Verify (Assert)
        assertEquals("Michelin", tire.getManufacturer());
        assertEquals(18, tire.getDiameter());
        assertEquals(250.00, tire.getPrice(), 0.001);
    }


}