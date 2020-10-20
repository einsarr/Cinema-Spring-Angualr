package cinema.cinema.web;

import cinema.cinema.dao.FilmRepository;
import cinema.cinema.dao.TicketRepository;
import cinema.cinema.entities.Film;
import cinema.cinema.entities.Ticket;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CinemaRestController {
    private FilmRepository filmRepository;
    private TicketRepository ticketRepository;

    public CinemaRestController(FilmRepository filmRepository, TicketRepository ticketRepository) {
        this.filmRepository = filmRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping(path = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable (name = "id") Long id) throws Exception{
        Film f= filmRepository.findById(id).get();
        String photoName = f.getPhoto();
        File file = new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }
    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTicket(@RequestBody TicketForm ticketForm){
        List<Ticket> listTickets = new ArrayList<>();
        ticketForm.getTickets().forEach(idTicket->{
            Ticket ticket = ticketRepository.findById(idTicket).get();
            ticket.setNomClient(ticketForm.getNomClient());
            ticket.setReserve(true);
            ticket.setCodePaiement(ticketForm.getCode_paiement());
            ticketRepository.save(ticket);
            listTickets.add(ticket);
        });
        return listTickets;
    }


    //POUR LES FICHIERS AU FORMAT PDF
   /* @GetMapping(path = "/imageFilm/{id}",produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] image(@PathVariable (name = "id") Long id) throws Exception{
        Film f= filmRepository.findById(id).get();
        String photoName = f.getPhoto();
        File file = new File(System.getProperty("user.home")+"/cinema/images/"+photoName+".pdf");
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }*/
    /*@GetMapping("/listeFilms")
    public List<Film> films(){
        return filmRepository.findAll();
    }*/
}

@Data
class TicketForm{
    private String nomClient;
    private Integer code_paiement;
    private List<Long> tickets = new ArrayList<>();
}