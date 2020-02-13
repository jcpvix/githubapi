package br.com.jcpvix.githubapi.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "page")
public class Page {

	@Id
	private String id;
	
	@Indexed(unique = true)
	private String url;
	
    private Set<File> files;
	
}
