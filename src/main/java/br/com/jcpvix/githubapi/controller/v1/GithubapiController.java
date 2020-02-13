package br.com.jcpvix.githubapi.controller.v1;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jcpvix.githubapi.dto.response.Response;
import br.com.jcpvix.githubapi.exception.ApiException;
import br.com.jcpvix.githubapi.model.File;
import br.com.jcpvix.githubapi.model.Page;
import br.com.jcpvix.githubapi.service.GithubService;

@RestController
@RequestMapping("/api/v1")
public class GithubapiController {

	private final GithubService githubScraperService;
	
	@Autowired
	public GithubapiController(GithubService githubScraperService) {
		this.githubScraperService = githubScraperService;
	}
	
	@GetMapping(path="/files", produces = "application/json")
	public Response<Map<String, List<File>>> getAllFiles(@RequestParam String url) throws ApiException {
		try {
			Page page = githubScraperService.execute(url);
			
			Map<String, List<File>> groupByExtension = page.getFiles().stream().collect(Collectors.groupingBy(File::getExtension));

			return Response.<Map<String, List<File>>>ok(groupByExtension);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

}