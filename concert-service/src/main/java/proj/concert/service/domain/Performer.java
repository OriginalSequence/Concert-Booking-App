package proj.concert.service.domain;

import proj.concert.common.types.Genre;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "PERFORMERS")
public class Performer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "BLURB", length = 1024)
    private String blurb;

    @Column(name = "IMAGE_NAME", nullable = false)
    private String imageName;

    @Column(name = "VERSION")
    private Long version;
    @Enumerated(EnumType.STRING)
    @Column(name = "GENRE")
    private Genre genre;

    public Performer(){}

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

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public Genre getGenre(){
        return genre;
    }

    public void setGenre(Genre genre){
        this.genre = genre;
    }

    public String getBlurb(){
        return blurb;
    }

    public void setBlurb(String blurb){
        this.blurb = blurb;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Performer))
            return false;
        if (obj == this)
            return true;

        Performer o = (Performer) obj;
        return new EqualsBuilder()
                .append(id, o.id)
                .append(name, o.name)
                .append(imageName, o.imageName)
                .append(genre, o.genre)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(imageName)
                .append(genre)
                .toHashCode();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Performer, id: ");
        buffer.append(id);
        buffer.append(", name: ");
        buffer.append(name);
        buffer.append(", s3 image: ");
        buffer.append(imageName);
        buffer.append(", genre: ");
        buffer.append(genre.toString());

        return buffer.toString();
    }

}
