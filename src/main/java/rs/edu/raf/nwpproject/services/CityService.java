package rs.edu.raf.nwpproject.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.nwpproject.models.City;
import rs.edu.raf.nwpproject.repositories.CityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CityService implements IService<City, Long>{

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City create(City city) {
        return cityRepository.save(city);
    }

    @Override
    public City update(City city) {
        return cityRepository.save(city);
    }

    @Override
    public List<City> findAll() {
        return (List<City>) cityRepository.findAll();
    }

    @Override
    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }

    public City findByName(String name) {
        return cityRepository.findByName(name);
    }
}
