package bookstore.services;

import bookstore.entities.BatteryEntity;
import bookstore.entities.ProductEntity;
import bookstore.entities.TireEntity;
import bookstore.repositories.IRepository;

public class AutomotiveService {
    private final IRepository<ProductEntity> repository;

    public AutomotiveService(IRepository<ProductEntity> repository) {
        this.repository = repository;
    }

    /**
     * Business Rule: Inspect battery cold-cranking amps before allowing a sale.
     */
    public boolean verifyBatterySafety(Long id) {
        ProductEntity item = repository.findById(id);
        if (item instanceof BatteryEntity battery) {
            // Safety rule: Battery must have at least 600 CCA to be deemed safe for sale
            return battery.getColdCrankingAmps() >= 600;
        }
        return false;
    }
}

