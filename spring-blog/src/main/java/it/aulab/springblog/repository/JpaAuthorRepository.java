// le repository sono delle interfacce che estendono delle interfacce generiche in cui si inseriscono il tipo della colonna da gestire e il tipo dell'id da gestire di Spring
// conviene utilizzare jpa al posto di crud perchè ha più metodi



/*                              Repository
*                                  |
*                                  |
*         ---------------------------------------------------
*        |                                                   |
*        |                                                   |
* CrudRepository                                      PagingAndSortingRepository
* - metodi: findAll, save, findById...                 - metodi: findAll (sort and paging)
* - tipi: Iterable                                     - tipi: Iterable
*        |                                                   |
*        |                                                   |
* ListCrudRepository                                  ListPagingAndSortingRepository
* - tipi: List                                        - tipi: List
*        |                                                   |
*        |                                                   |
*         --------------------------------------------------- 
*                                  |
*                                  |              
*                            JpaRepository
*                            - metodi: saveAndFlush, getOne, saveAllAndFlush...
* 
*/


package it.aulab.springblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.springblog.model.Author;


public interface JpaAuthorRepository extends JpaRepository<Author, Long> {
     
    public List<Author> findByLastName(String lastname);

    public List<Author> findByFirstNameNotIgnoreCase(String s);

    public List<Author> findByFirstNameEquals(String s);

    public List<Author> findByFirstNameContains(String s);

    public List<Author> findByFirstNameAndLastName(String a, String b);

    public List<Author> findByFirstNameOrLastName(String a, String b);

}

