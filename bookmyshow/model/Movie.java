package model;

public class Movie {
    private final String id;
    private final String movieName;
    private final Integer durationInMinutes;

    public Movie(String id, String movieName, Integer durationInMinutes) {
        this.id = id;
        this.movieName = movieName;
        this.durationInMinutes = durationInMinutes;
    }

    public String getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }
}
