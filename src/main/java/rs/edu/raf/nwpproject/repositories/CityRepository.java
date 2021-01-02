package rs.edu.raf.nwpproject.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.nwpproject.models.City;

public interface CityRepository extends CrudRepository<City, Long> {

    City findByName(String name);
}
