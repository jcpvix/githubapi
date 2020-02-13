package br.com.jcpvix.githubapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(description = "All details about the Files. ")
public class File {
	
	@ApiModelProperty(notes = "The file path")
	private String path;
	private String name;
	private String extension;
	private Integer size;
	private Integer lines;
	
	public File(String path, String name, String extension, Integer size, Integer lines) {
		this.path = path;
		this.name = name;
		this.extension = extension;
		this.size = size;
		this.lines = lines;
	}
}
