package rs.edu.raf.nwpproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.nwpproject.dtos.FlightDto;
import rs.edu.raf.nwpproject.models.Flight;
import rs.edu.raf.nwpproject.repositories.FlightRepository;
import rs.edu.raf.nwpproject.repositories.FlightRepositoryCustom;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService implements IService<Flight, Long>{

    private final FlightRepository flightRepository;

    @Autowired
    private final FlightRepositoryCustom flightRepositoryCustom;

    public FlightService(FlightRepository flightRepository, FlightRepositoryCustom flightRepositoryCustom) {
        this.flightRepository = flightRepository;
        this.flightRepositoryCustom = flightRepositoryCustom;
    }

    @Override
    public Flight create(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight update(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public List<Flight> findAll() {
        return (List<Flight>) flightRepository.findAll();
    }

    public List<FlightDto> findAllDtos() {
        return flightRepositoryCustom.getFlightDtos();
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        flightRepository.deleteById(id);
    }

    public Flight findByOriginCityIdAndDestinationCityId(int originCityId, int destinationCityId){
        return flightRepository.findByOriginCityIdAndDestinationCityId(originCityId, destinationCityId);
    }
}
