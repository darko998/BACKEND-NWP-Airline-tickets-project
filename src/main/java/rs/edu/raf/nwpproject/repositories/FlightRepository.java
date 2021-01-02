package rs.edu.raf.nwpproject.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.nwpproject.models.Flight;

public interface FlightRepository extends CrudRepository<Flight, Long> {

    Flight findByOriginCityIdAndDestinationCityId(int originCityId, int destinationCityId);
}
