package br.com.jcpvix.githubapi.service;

import br.com.jcpvix.githubapi.model.Page;

public interface GithubService {

	Page execute(String repositoryUrl) throws Exception;
}
