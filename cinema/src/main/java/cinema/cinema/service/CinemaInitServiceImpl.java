package cinema.cinema.service;

import cinema.cinema.dao.*;
import cinema.cinema.entities.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService{
    private VilleRepository villeRepository;
    private CategoryRepository categoryRepository;
    private CinemaRepository cinemaRepository;
    private PlaceRepository placeRepository;
    private SalleRepository salleRepository;
    private SeanceRepository seanceRepository;
    private FilmRepository filmRepository;
    private ProjectionRepository projectionRepository;
    private TicketRepository ticketRepository;

    public CinemaInitServiceImpl(VilleRepository villeRepository, CategoryRepository categoryRepository,
                                 CinemaRepository cinemaRepository, PlaceRepository placeRepository,
                                 SalleRepository salleRepository, SeanceRepository seanceRepository,
                                 FilmRepository filmRepository, ProjectionRepository projectionRepository, TicketRepository ticketRepository) {
        this.villeRepository = villeRepository;
        this.categoryRepository = categoryRepository;
        this.cinemaRepository = cinemaRepository;
        this.placeRepository = placeRepository;
        this.salleRepository = salleRepository;
        this.seanceRepository = seanceRepository;
        this.filmRepository = filmRepository;
        this.projectionRepository = projectionRepository;
        this.ticketRepository = ticketRepository;
    }


    @Override
    public void initVilles() {
        Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
            Ville ville = new Ville();
            ville.setName(nameVille);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v->{
            Stream.of("MegaRama","IMAX","FOUNOUN","CHARAZAD","DAOULIZ")
                    .forEach(nameCinema->{
                        Cinema cinema = new Cinema();
                        cinema.setName(nameCinema);
                        cinema.setNombreSalle(3+(int)(Math.random()*7));
                        cinema.setVille(v);
                        cinemaRepository.save(cinema);
                    });
        });
    }

    @Override
    public void initSalles() {
            cinemaRepository.findAll().forEach(cinema -> {
                for(int i =0;i<cinema.getNombreSalle();i++){
                    Salle salle = new Salle();
                    salle.setName("Salle "+(i+1));
                    salle.setCinema(cinema);
                    salle.setNombrePlace(15+(int)Math.random()*35);
                    salleRepository.save(salle);
                }
            });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for(int i=0;i<salle.getNombrePlace();i++){
                Place place = new Place();
                place.setNumero(i+1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initSeances() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(s->{
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(s));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initCategories() {
        Stream.of("Histoire","Actions","Fiction","Drama").forEach(cat->{
            Categorie categorie = new Categorie();
            categorie.setName(cat);
            categoryRepository.save(categorie);
        });
    }

    @Override
    public void initFilms() {
        double[] durees = new double[]{1,1.5,2,2.5,3};
        List<Categorie> categories = categoryRepository.findAll();
        Stream.of("bruce lee","film","game-of-thrones","ong back","prison","prison")
        .forEach(titreFilm->{
                Film film = new Film();
                film.setTitre(titreFilm);
                film.setDuree(durees[new Random().nextInt(durees.length)]);
                film.setPhoto(titreFilm.replace(" ","")+".jpg");
                film.setCategorie(categories.get(new Random().nextInt(categories.size())));
                filmRepository.save(film);
        });
    }

    @Override
    public void initProjections() {
        double[] prices = new double[]{30,50,60,70,90,100};
        List<Film> films = filmRepository.findAll();
        villeRepository.findAll().forEach(ville->{
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    int index = new Random().nextInt(films.size());
                    Film film = films.get(index);
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection = new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
                });
            });
        });
    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach(p->{
            p.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(p.getPrix());
                ticket.setProjection(p);
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });
    }
}
