package bookstore.pojos;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest {
    @Test
    void testBatteryInitialization() {
        // 1. Prepare the Script (Title -> Manufacturer -> Price -> Diameter)
        // Based on the "Bucket Brigade" order in VehiclePart and Tire
        String inputData =  "Duracell\n180.00\n850\n";

        // 2. Create the Mock Dependency
        Scanner mockScanner = new Scanner(new ByteArrayInputStream(inputData.getBytes()));

        // 3. Execute
        Battery battery=new Battery();
        battery.initialize(mockScanner); // Injection!

        // 4. Verify (Assert)
        assertEquals("Duracell", battery.getManufacturer());
        assertEquals(850, battery.getColdCrankingAmps());
        assertEquals(180.00, battery.getPrice(), 0.001);
    }


}