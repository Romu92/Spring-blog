package it.aulab.springblog.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
// ogni elemento creato potrà essere inserito nel database con un persist
@Table(name="authors")
// collegamento con la tabella del database corrispondente
public class Author {


    @Id
    // serve ad indicare che è una chiave primaria e sostituisce @column e not null
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // si usa quando la chiave primaria è autogestita dal database in questo caso è autoincrementata
    private Long id;

    @Column(name = "firstname", length = 100, nullable = false)
    private String firstName;

    @Column(name = "lastname", length = 100, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false)
    private String email;

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<Post>();

    public Author() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
