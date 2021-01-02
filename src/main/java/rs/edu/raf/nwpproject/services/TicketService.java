package rs.edu.raf.nwpproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.nwpproject.dtos.TicketDto;
import rs.edu.raf.nwpproject.models.Ticket;
import rs.edu.raf.nwpproject.repositories.TicketRepository;
import rs.edu.raf.nwpproject.repositories.TicketRepositoryCustom;
import rs.edu.raf.nwpproject.repositories.UserRepositoryCustom;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements IService<Ticket, Long> {

    @Autowired
    private final UserRepositoryCustom userRepositoryCustom;

    @Autowired
    private final TicketRepositoryCustom ticketRepositoryCustom;

    private final TicketRepository ticketRepository;

    public TicketService(UserRepositoryCustom userRepositoryCustom, TicketRepositoryCustom ticketRepositoryCustom, TicketRepository ticketRepository) {
        this.userRepositoryCustom = userRepositoryCustom;
        this.ticketRepositoryCustom = ticketRepositoryCustom;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket create(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket update(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> findAll() {
        return (List<Ticket>) ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<TicketDto> getTicketDtos () {
        return userRepositoryCustom.getTickets();
    }

    public List<TicketDto> getUserReservedTickets(Long userId){
        return ticketRepositoryCustom.getUserReservedTickets(userId);
    }

    public List<TicketDto> getCompanyTickets(Long companyId){
        return ticketRepositoryCustom.getCompanyTickets(companyId);
    }

    public void deleteCompanyTickets(Long companyId) {
        ticketRepositoryCustom.deleteCompanyTickets(companyId);
    }

    public void reserveTickets(int numberOfTickets, int ticketId) {
        ticketRepositoryCustom.reserveTickets(numberOfTickets, ticketId);
    }

    public void cancelReservedTickets(int numberOfTickets, int ticketId) {
        ticketRepositoryCustom.cancelReservedTickets(numberOfTickets, ticketId);
    }
}
