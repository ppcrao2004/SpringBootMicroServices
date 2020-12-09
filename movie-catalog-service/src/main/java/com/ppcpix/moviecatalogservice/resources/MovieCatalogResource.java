package com.ppcpix.moviecatalogservice.resources;

import com.ppcpix.moviecatalogservice.models.CatalogItem;
import com.ppcpix.moviecatalogservice.models.Movie;
import com.ppcpix.moviecatalogservice.models.Rating;
import com.ppcpix.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){


        //get all rated movie ids
        UserRating ratings = restTemplate.getForObject("http://RATING-DATA-SERVICE/ratingsdata/users/" + userId, UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
            //for each movie ID , call movie info service and get detail
            Movie movie = restTemplate.getForObject("http://RATING-DATA-SERVICE/movies/" + rating.getMovieId(), Movie.class);


            return new CatalogItem(movie.getName() , "Desc" , rating.getRating());
        })
        .collect(Collectors.toList());



    }
}

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();

