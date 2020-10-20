package cinema.cinema;

import cinema.cinema.entities.Film;
import cinema.cinema.entities.Projection;
import cinema.cinema.entities.Salle;
import cinema.cinema.entities.Ticket;
import cinema.cinema.service.ICinemaInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner {
    @Autowired
    private ICinemaInitService iCinemaInitService;
    //EXPOSITION DES ID
    @Autowired
    private RepositoryRestConfiguration restConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //EXPOSER L'ID AVEC SPRING DATA REST
        restConfiguration.exposeIdsFor(Film.class);
        restConfiguration.exposeIdsFor(Projection.class);
        restConfiguration.exposeIdsFor(Ticket.class, Salle.class);
        /*iCinemaInitService.initVilles();
        iCinemaInitService.initCinemas();
        iCinemaInitService.initSalles();
        iCinemaInitService.initPlaces();
        iCinemaInitService.initSeances();
        iCinemaInitService.initCategories();
        iCinemaInitService.initFilms();
        iCinemaInitService.initProjections();
        iCinemaInitService.initTickets();*/
    }
}
