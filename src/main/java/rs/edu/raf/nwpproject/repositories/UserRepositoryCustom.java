package rs.edu.raf.nwpproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import rs.edu.raf.nwpproject.dtos.TicketDto;
import rs.edu.raf.nwpproject.models.User;

import java.util.List;

@Component
public class UserRepositoryCustom {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TicketDto> getTickets() {
        String query =
                "select ticket.id, " +
                "ticket.company_id, " +
                "company.name [companyName], " +
                "ticket.one_way, " +
                "ticket.depart_date, " +
                "ticket.return_date, " +
                "ticket.flight_id, " +
                "originCity.name [originCity], " +
                "destinationCity.name [destinationCity], " +
                "ticket.count, " +
                "ticket.reserved " +
                "from tickets ticket " +
                "inner join companies company on ticket.company_id = company.id " +
                "inner join flights flight on ticket.flight_id = flight.id " +
                "inner join cities originCity on flight.origin_city_id = originCity.id " +
                "inner join cities destinationCity on flight.destination_city_id = destinationCity.id";

        List<TicketDto> tickets = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(TicketDto.class));

        return tickets;
    }
}
