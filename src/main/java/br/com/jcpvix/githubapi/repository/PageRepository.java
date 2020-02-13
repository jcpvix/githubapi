package br.com.jcpvix.githubapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.jcpvix.githubapi.model.Page;

public interface PageRepository extends MongoRepository<Page, String> {

	Page findByUrl(String url);
	
}