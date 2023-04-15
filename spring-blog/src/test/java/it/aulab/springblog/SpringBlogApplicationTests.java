package it.aulab.springblog;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
// import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import it.aulab.springblog.model.Author;
import it.aulab.springblog.model.Comment;
import it.aulab.springblog.model.Post;
import it.aulab.springblog.repository.CrudAuthorRepository;
import it.aulab.springblog.repository.JpaAuthorRepository;
import it.aulab.springblog.repository.PagingAndSortingAuthorRepository;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

// serrve ad importare tutti i metodi statici della classe


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpringBlogApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	// serve per fare l'ingenction
	private JpaAuthorRepository authorRepository;

	// @Autowired
	// private CrudAuthorRepository crudAuthorRepository;

	// @Autowired
	// private PagingAndSortingAuthorRepository pAndSAuthorRepository;


	@BeforeEach
	// col beforeeach si possono aggiungere elementi al database per fare i test
	public void createData() {
		Author a1 = new Author();
		a1.setFirstName("Mirko");
		a1.setLastName("Abbrescia");
		a1.setEmail("mirkoabbrescia@aulab.it");
	

		entityManager.persist(a1);

		Author a2 = new Author();
		a2.setFirstName("Andrea");
		a2.setLastName("Mininni");
		a2.setEmail("andreamininni@aulab.it");

		entityManager.persist(a2);

		Post p1 = new Post();
		p1.setAuthor(a1);
		p1.setTitle("Il nuovo Pixel 7 Pro");
		p1.setBody("Lorem ipsum");
		p1.setPublishDate("20230411");

		entityManager.persist(p1);

		Post p2 = new Post();
		p2.setAuthor(a1);
		p2.setTitle("Il nuovo IPhone 14");
		p2.setBody("Lorem ipsum");
		p2.setPublishDate("20230411");

		entityManager.persist(p2);

		Comment c1 = new Comment();
		c1.setEmail("andreamininni@aulab.it");
		c1.setPost(p1);
		c1.setBody("Non mi è piace");
		c1.setPublishDate("20230411");

		Comment c2 = new Comment();
		c2.setEmail("andreamininni@aulab.it");
		c2.setPost(p1);
		c2.setBody("Bellissimo, consigliato");
		c1.setPublishDate("20230412");

		entityManager.persist(c1);
		entityManager.persist(c2);

	}

	@Test
	void authorCheck() {

		List<Author> authors = entityManager
				.createQuery("SELECT a FROM Author a", Author.class)
				.getResultList();

		assertThat(authors).hasSize(2);
	}

	@Test
	void postCheck() {

		List<Post> posts = entityManager
				.createQuery("SELECT p FROM Post p", Post.class)
				.getResultList();

		assertThat(posts).hasSize(2);
	}

	@Test
	void commentCheck() {

		List<Post> posts = entityManager
				.createQuery("SELECT p FROM Post p", Post.class)
				.getResultList();

		System.out.println(posts.get(0).getId());
		List<Comment> commmentsPost1 = entityManager.createQuery(
				"SELECT c FROM Comment c WHERE c.post.id = ?1",
				
				Comment.class).setParameter(1, posts.get(0).getId())
				.getResultList();

		assertThat(commmentsPost1).hasSize(2);

		System.out.println(posts.get(1).getId());
		List<Comment> commmentsPost2 = entityManager.createQuery(
				"SELECT c FROM Comment c WHERE c.post.id = ?1",
				Comment.class).setParameter(1, posts.get(1).getId())
				.getResultList();

		assertThat(commmentsPost2).hasSize(0);
	}

	@Test
	void queryCheck() {

		List<Post> posts = entityManager
				.createQuery("SELECT p FROM Post p", Post.class)
				.getResultList();

		// List<Comment> commment =
		// entityManager.createQuery(
		// "SELECT c FROM Comment c WHERE c.post.id = :id AND c.email = :email",
		// Comment.class
		// )
		// .setParameter("id", 1)
		// .setParameter("email", "andreamininni@aulab.it")
		// .getResultList();

		List<Comment> commment = entityManager.createQuery(
				"SELECT c FROM Comment c WHERE c.post.id = ?1 AND c.email = ?2",
				// il puntointerrogativo mi dice in che posizione si trova il parametro che istanzio nel setParameter oppure quando ho creato l'oggetto
				Comment.class)
				.setParameter(1, posts.get(0).getId())
				.setParameter(2, "andreamininni@aulab.it")
				.getResultList();

		/*
		 * select c1_0.id,c1_0.body,c1_0.email,c1_0.post_id,c1_0.date
		 * from comments c1_0
		 * where
		 * c1_0.post_id=?
		 * and
		 * c1_0.email=?
		 */

		assertThat(commment).hasSize(2);
	}

	@Test
	void authorRepositoryCheck() {
		assertThat(authorRepository.count()).isEqualTo(2);

		assertThat(authorRepository.findAll())
				.first()
				.extracting("firstName")
				.isEqualTo("Mirko");

		Author a = authorRepository.findAll().get(0);

		assertThat(authorRepository.findById(a.getId()).get())
				.isNotNull()
				.extracting("lastName")
				.isEqualTo("Abbrescia");
	}

	@Test
	void findByLastName() {
		
		assertThat(authorRepository.findByLastName("Abbrescia"))
				.first()
				.extracting("lastName")
				.isEqualTo("Abbrescia");
	}

	@Test
	void findByFirstnameAndLastname() {
		assertThat(authorRepository.findByFirstNameAndLastName("Andrea", "Abbrescia"))
				.hasSize(0);

		assertThat(authorRepository.findByFirstNameOrLastName("andrea", "abbrescia"))
				.hasSize(2);
	}

	@Test
	void notIgnoreCase() {

		assertThat(authorRepository.findByFirstNameNotIgnoreCase("andrea"))
				.first()
				.extracting("firstName")
				.isEqualTo("Mirko");

		assertThat(authorRepository.findByFirstNameEquals("andrea"))
				.first()
				.extracting("firstName")
				.isEqualTo("Andrea");

	
		assertThat(authorRepository.findByFirstNameContains("r")).hasSize(2);

	}

	@Test
	void createCrud() {
		Author a = new Author();
		a.setFirstName("Gianvito");
		a.setLastName("Palmisano");
		a.setEmail("gvpalm@gmail.com");

		authorRepository.save(a);

		assertThat(authorRepository.findAll()).hasSize(3);

	}

	@Test
	void createJpa() {
		Author a = new Author();
		a.setFirstName("Gianvito");
		a.setLastName("Palmisano");
		a.setEmail("gvpalm@gmail.com");

		authorRepository.save(a);

		assertThat(authorRepository.findAll()).hasSize(3);
	
	}

	@Test
	void sorting() {
		/*sort classe statica di spring che ci permette di ordinare i dati  */
		assertThat(authorRepository.findAll(Sort.by("lastName")))
				.first()
				.extracting("lastName")
				.isEqualTo("Abbrescia");

		

		/*
		 * 
		 * Direction.ASC di default
		 * - altrimenti inserire Sort.by(Direction.DESC, "lastName")
		 * 
		 */

		assertThat(authorRepository.findAll(Sort.by("lastName")))
				.extracting("lastName")
				.contains("Abbrescia", "Mininni");

		assertThat(authorRepository.findAll(Sort.by("lastName")))
				.extracting("lastName")
				.contains("Abbrescia", atIndex(0))
				.contains("Mininni", atIndex(1));
	}

	@Test
	void sorting2() {

		Author a1 = new Author();
		a1.setFirstName("Paolo");
		a1.setLastName("abbrescia");
		a1.setEmail("paolo@aulab.it");

		entityManager.persist(a1);

		assertThat(authorRepository.findAll(Sort.by("lastName", "firstName")))
				.extracting("firstName")
				.contains("Mirko", atIndex(0))
				.contains("Paolo", atIndex(1))
				.contains("Andrea", atIndex(2));
	}

	@Test
	void paging() {

		// authorRepository.deleteAll();

		for (int i = 0; i < 100; i++) {
			Author a = new Author();
			a.setFirstName(String.valueOf(i));
			a.setLastName(String.valueOf(i));
			a.setEmail(String.valueOf(i));

			authorRepository.save(a);
		}
		assertThat(authorRepository.findAll()).hasSize(102);
		// assertThat(authorRepository.findAll()).hasSize(100);
		// 102 senza la deletAll

		// questa è più veloce
		// assertThat(authorRepository.count()).isEqualTo(100);

		int pageSize = 10;
		PageRequest.of(0, pageSize);
		assertThat(authorRepository.findAll(PageRequest.of(0, pageSize)).getContent())
				.extracting("firstName")
				.contains("Mirko", atIndex(0))
				.contains("Andrea", atIndex(1));

		assertThat(authorRepository.findAll(PageRequest.of(1, pageSize)).getContent())
				.extracting("lastName")
				.contains("8", atIndex(0))
				.contains("9", atIndex(1));

		assertThat(authorRepository.findAll(PageRequest.of(9, pageSize)).getContent())
				.extracting("email")
				.contains("96", atIndex(8))
				.contains("97", atIndex(9));

	}
	
	@Test
	void customRepository() {
		List<Author> find = authorRepository.findQualcosa("nn");

		assertThat(find).hasSize(1);

		assertThat(find.get(0))
				.extracting("firstName")
				.isEqualTo("Andrea");
	}

	@Test
	void sorting3() {

		Author a2 = new Author();
		a2.setFirstName("Paolo");
		a2.setLastName("abbrescia");
		a2.setEmail("paolo@aulab.it");

		entityManager.persist(a2);

		List<Author> sorted = authorRepository.findAll(
				Sort.by("lastName").descending()
						.and(
								Sort.by("firstName").ascending()));

		for (Author a : sorted) {
			System.out.println("Nome: " + a.getFirstName());
			System.out.println("Cognome: " + a.getLastName());
			System.out.println("email: " + a.getEmail());
		}

		assertThat(sorted)
				.extracting("firstName")
				.contains("Andrea", atIndex(0))
				.contains("Mirko", atIndex(1))
				.contains("Paolo", atIndex(2));
	}




}
