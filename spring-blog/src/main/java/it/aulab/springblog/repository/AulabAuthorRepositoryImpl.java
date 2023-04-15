package it.aulab.springblog.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.aulab.springblog.model.Author;
import jakarta.persistence.EntityManager;

@Repository
public class AulabAuthorRepositoryImpl implements AulabAuthorRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Author> findQualcosa(String partialEmail) {
        return entityManager
                .createQuery("SELECT a FROM Author a WHERE a.email LIKE :partialEmail", Author.class)
                .setParameter("partialEmail", "%" + partialEmail + "%")

                //con l'utilizzo di like nelle query se mettiamo un parametro all'interno di due % vuol dire che cerchiamo un oggetto nel database con contenga quel parametro se invece mettiamo un parametro seguito dal % vuol dire che deve iniziare se lo mettiamo all'inizio il % e poi il parametro vuol dire che deve finire col parametro
                .getResultList();
    }

}

