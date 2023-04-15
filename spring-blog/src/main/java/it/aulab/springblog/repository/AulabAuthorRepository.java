// custom repository


package it.aulab.springblog.repository;

import java.util.List;

import it.aulab.springblog.model.Author;

public interface AulabAuthorRepository {

    List<Author> findQualcosa(String partialEmail);

}
