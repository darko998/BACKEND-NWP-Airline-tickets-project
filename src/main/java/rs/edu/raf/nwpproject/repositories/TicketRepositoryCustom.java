package rs.edu.raf.nwpproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import rs.edu.raf.nwpproject.dtos.TicketDto;
import rs.edu.raf.nwpproject.models.Ticket;

import java.util.List;

@Component
public class TicketRepositoryCustom {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TicketDto> getUserReservedTickets(Long userId) {

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
                        "inner join reservations reservation on ticket.id = reservation.ticket_id " +
                        "inner join cities destinationCity on flight.destination_city_id = destinationCity.id " +
                        "where reservation.user_id = " + userId;

        List<TicketDto> tickets = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(TicketDto.class));

        return tickets;
    }


    public List<TicketDto> getCompanyTickets(Long companyId) {

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
                        "inner join cities destinationCity on flight.destination_city_id = destinationCity.id " +
                        "where ticket.company_id = " + companyId;

        List<TicketDto> tickets = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(TicketDto.class));

        return tickets;
    }

    public void deleteCompanyTickets(Long companyId) {
        String query = "delete from tickets where company_id = " + companyId;

        jdbcTemplate.execute(query);
    }

    public void reserveTickets(int numberOfTickets, int ticketId) {

        String query = "update tickets set count = " + numberOfTickets + " where id = " + ticketId;

        jdbcTemplate.execute(query);
    }


    public void cancelReservedTickets(int numberOfTickets, int ticketId) {

        String query = "update tickets set count = " + numberOfTickets + " where id = " + ticketId;

        jdbcTemplate.execute(query);
    }
}
