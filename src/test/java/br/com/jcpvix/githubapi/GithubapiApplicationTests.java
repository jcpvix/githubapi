package br.com.jcpvix.githubapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import br.com.jcpvix.githubapi.model.File;
import br.com.jcpvix.githubapi.model.Page;
import br.com.jcpvix.githubapi.repository.PageRepository;

@SpringBootTest
@AutoConfigureMockMvc
class GithubapiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PageRepository pageRepository;
	
	@Test
	void shouldReturnOkJson() throws Exception {

		File file = new File()
				.setName("README.md")
				.setPath("https://github.com/githubapi/testgithubapirepo/README.md")
				.setExtension("rm")
				.setLines(2)
				.setSize(12251);
		
		HashSet<File> files = new HashSet<File>(Arrays.asList(file));
		
		Page page = new Page()
				.setId("1")
				.setUrl("https://github.com/test/repo")
				.setFiles(files);
		
		given(pageRepository.findByUrl(any(String.class))).willReturn(page);
		
		ResultActions response = mockMvc.perform(get("/api/v1/files")
		        .contentType("application/json")
		        .param("url", "https://github.com/githubapi/testgithubapirepo"));
		
		response.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
	}
	
	@Test
	void shouldReturnNotFoundJson() throws Exception {

		given(pageRepository.findByUrl(any(String.class))).willReturn(null);
		
		ResultActions response = mockMvc.perform(get("/api/v1/files")
		        .contentType("application/json")
		        .param("url", "https://github.com/githubapi/testgithubapirepo"));
		
		response.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
	}

}
