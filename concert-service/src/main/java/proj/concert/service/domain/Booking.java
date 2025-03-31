package proj.concert.service.domain;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.*;

@Entity
public class Booking {
	@Column(name = "VERSION")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private long concertId;
    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private List<Seat> seats;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user = null;

    public Booking(){}

    public Booking(long concertId, LocalDateTime date, List<Seat> seats){
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    public List<Seat> getSeats(){
        return seats;
    }

    public void setSeats(List<Seat> seats){
        this.seats = seats;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
    public long getConcertId(){
        return concertId;
    }

    public void setConcertId(long concertId){
        this.concertId = concertId;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getDate(){
        return date;
    }
    
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public void setDate(LocalDateTime date){
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(concertId, date, id, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) obj;
        return concertId == other.concertId && Objects.equals(date, other.date) && Objects.equals(id, other.id)
                && Objects.equals(user, other.user);
    }
}
