package it.aulab.springblog.repository;

import org.springframework.data.repository.ListPagingAndSortingRepository;

import it.aulab.springblog.model.Author;

public interface PagingAndSortingAuthorRepository extends ListPagingAndSortingRepository<Author, Long> {

}

