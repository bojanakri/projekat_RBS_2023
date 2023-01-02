package com.zuehlke.securesoftwaredevelopment.controller;

import com.zuehlke.securesoftwaredevelopment.config.AuditLogger;
import com.zuehlke.securesoftwaredevelopment.domain.*;
import com.zuehlke.securesoftwaredevelopment.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MoviesController {

    private static final Logger LOG = LoggerFactory.getLogger(MoviesController.class);
    private static final AuditLogger auditLogger = AuditLogger.getAuditLogger(MoviesController.class);

    private MovieRepository movieRepository;
    private CommentRepository commentRepository;
    private RatingRepository ratingRepository;
    private PersonRepository userRepository;

    private GenreRepository genreRepository;

    public MoviesController(MovieRepository movieRepository, CommentRepository commentRepository, RatingRepository ratingRepository, PersonRepository userRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.commentRepository = commentRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/")
    public String showSearch(Model model) {
        model.addAttribute("movies", movieRepository.getAll());
        return "movies";
    }

    @GetMapping("/create-form")
    public String CreateForm(Model model) {
        model.addAttribute("genres", genreRepository.getAll());
        return "create-form";
    }

    @GetMapping(value = "/api/movies/search", produces = "application/json")
    @ResponseBody
    public List<Movie> search(@RequestParam("query") String query) throws SQLException {
        return movieRepository.search(query);
    }

    @GetMapping("/movies")
    public String showMovie(@RequestParam(name = "id", required = false) String id, Model model, Authentication authentication) {
        if (id == null) {
            model.addAttribute("movies", movieRepository.getAll());
            return "movies";
        }

        User user = (User) authentication.getPrincipal();
        List<Genre> genreList = this.genreRepository.getAll();

        model.addAttribute("movie", movieRepository.get(Integer.parseInt(id), genreList));
        List<Comment> comments = commentRepository.getAll(id);
        List<Rating> ratings = ratingRepository.getAll(id);
        Optional<Rating> userRating = ratings.stream().filter(rating -> rating.getUserId() == user.getId()).findFirst();
        if (userRating.isPresent()) {
            model.addAttribute("userRating", userRating.get().getRating());
        }
        if (ratings.size() > 0) {
            Integer sumRating = ratings.stream().map(rating -> rating.getRating()).reduce(0, (total, rating) -> total + rating);
            Double avgRating = (double)sumRating/ratings.size();
            model.addAttribute("averageRating", avgRating);
        }

        List<ViewComment> commentList = new ArrayList<>();

        for (Comment comment : comments) {
            Person person = userRepository.get("" + comment.getUserId());
            commentList.add(new ViewComment(person.getFirstName() + " " + person.getLastName(), comment.getComment()));
        }

        model.addAttribute("comments", commentList);

        return "movie";
    }

    @PostMapping("/movies")
    public String createMovie(NewMovie newMovie) throws SQLException {
        List<Genre> genreList = this.genreRepository.getAll();
        List<Genre> genresToInsert = newMovie.getGenres().stream().map(genreId -> genreList.stream().filter(genre -> genre.getId() == genreId).findFirst().get()).collect(Collectors.toList());
        Long id = movieRepository.create(newMovie, genresToInsert);
        return "redirect:/movies?id=" + id;
    }
}
