package proj.concert.service.domain;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
public class ConcertInfoSubscription {
	
	@Column(name = "VERSION")

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private long concertId;
    private LocalDateTime date;
    private int percentageBooked;

    public ConcertInfoSubscription() {
    }

    public ConcertInfoSubscription(long concertId, LocalDateTime date, int percentageBooked) {
        this.concertId = concertId;
        this.date = date;
        this.percentageBooked = percentageBooked;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getDate() {
        return date;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPercentageBooked() {
        return percentageBooked;
    }

    public void setPercentageBooked(int percentageBooked) {
        this.percentageBooked = percentageBooked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        ConcertInfoSubscription sub = (ConcertInfoSubscription) obj;
        return new EqualsBuilder()
                .append(concertId, sub.concertId)
                .append(percentageBooked, sub.percentageBooked)
                .append(date, sub.date)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(concertId)
                .append(date)
                .append(percentageBooked)
                .toHashCode();
    }
}
