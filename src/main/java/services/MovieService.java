/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.mycompany.entities.Movie;
import com.mycompany.repository.MovieJpaController;
import com.mycompany.repository.exceptions.NonexistentEntityException;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOM;
import org.jvnet.staxex.StreamingDataHandler;

/**
 *
 * @author CBui
 */
@WebService(serviceName = "MovieService")
@HandlerChain(file = "UploadMovieHandler_handler.xml")
@MTOM(enabled = true, threshold = 100)
public class MovieService {

    /**
     * This is a sample web service operation
     */
    @WebMethod
   @XmlMimeType("application/octet-stream")
//    public int uploadMovie(String fileName, byte[] data, String description) throws Exception {
    public void uploadMovie(String fileName,  @XmlMimeType("application/octet-stream") DataHandler data, String description) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestMovie");
        MovieJpaController movieRepo = new MovieJpaController(emf);

        Movie movie = new Movie();
        movie.setName(fileName);
        final InputStream in = data.getInputStream();
        byte[] byteArray = org.apache.commons.io.IOUtils.toByteArray(in);
//        try {
//            StreamingDataHandler dh = (StreamingDataHandler) data;
//            File file = File.createTempFile(fileName, "");
//            dh.moveTo(file);
//            movie.setData(Files.readAllBytes(file.toPath()));
//            dh.close();
//            
//        } catch (Exception e) {
//            throw new WebServiceException(e);
//        }
        if (byteArray != null) {
            movie.setData(byteArray);
        }

        movie.setDescription(description);

        //Save movie into the database
        movieRepo.create(movie);
//        return data.length;
    }

    public byte[] downloadMovie(String fileName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestMovie");
        MovieJpaController movieRepo = new MovieJpaController(emf);

        Movie movie = movieRepo.findMovieEntities().stream()
                .filter(x -> x.getName().equalsIgnoreCase(fileName)).findFirst().orElse(null);

        if (movie != null) {
            return movie.getData();
        } else {
            return new byte[0];
        }
    }

    public List<Movie> getAllMovies() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestMovie");
        MovieJpaController movieRepo = new MovieJpaController(emf);
        List<Movie> movies = new ArrayList<>();

        movies = movieRepo.findMovieEntities();
        return movies;
    }

    public void deleteMovie(int id) throws NonexistentEntityException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestMovie");
        MovieJpaController movieRepo = new MovieJpaController(emf);

        movieRepo.destroy(id);
    }

    public Movie findMovie(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestMovie");
        MovieJpaController movieRepo = new MovieJpaController(emf);
        return movieRepo.findMovie(id);
    }

    public void editMovie(Movie movie) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestMovie");
        MovieJpaController movieRepo = new MovieJpaController(emf);
//        Movie movie = movieRepo.findMovie(id);
        movieRepo.edit(movie);
    }

}
