package rs.edu.raf.nwpproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import rs.edu.raf.nwpproject.dtos.FlightDto;
import rs.edu.raf.nwpproject.dtos.TicketDto;

import java.util.List;

@Component
public class FlightRepositoryCustom {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<FlightDto> getFlightDtos() {
        String query = "select flight.id, " +
                "flight.origin_city_id, " +
                "originCity.name [originCityName], " +
                "flight.destination_city_id, " +
                "destinationCity.name [destinationCityName] " +
                "from flights flight " +
                "inner join cities originCity on flight.origin_city_id = originCity.id " +
                "inner join cities destinationCity on flight.destination_city_id = destinationCity.id";

        List<FlightDto> flights = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(FlightDto.class));

        return flights;
    }
}
