package br.com.jcpvix.githubapi.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jcpvix.githubapi.model.File;
import br.com.jcpvix.githubapi.model.Page;
import br.com.jcpvix.githubapi.repository.PageRepository;
import br.com.jcpvix.githubapi.utils.PageContent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Service
public class GithubServiceImpl implements GithubService {

	private static final String BASE_URL = "https://github.com";
	private static final String ANCHOR_LINK = "<a class=\"js-navigation-open\" title=\"([^\"]*)\" id=\"([^\"]*)\"( [^=]*=\"[^\"]*\")? href=\"([^\"]*)\">([^<]*)";
	private static final String ANCHOR_CHECK_FILE_PAGE_TYPE = "<div class=\"text-mono[^\"]*\">\\s*(?:([0-9]+)\\s*lines\\s*\\(([0-9]+)\\s*sloc\\)[^\\/]*\\/span>\\s*)?([0-9\\.]+\\s*(?:Bytes|KB|MB)+)";

	@Autowired
	private PageRepository pageRepository;

	@Override
	public Page execute(String repositoryUrl) throws Exception {
		try {
			Page page = pageRepository.findByUrl(repositoryUrl);
	
			if (page == null) {
	
				Queue<Link> urls = new LinkedList<Link>();
	
				urls.add(new Link().setFileName("Repository Page").setHref(repositoryUrl));
	
				Set<File> files = new HashSet<File>();
	
				page = new Page().setUrl(repositoryUrl).setFiles(files);
	
				while (!urls.isEmpty()) {
	
					Link link = urls.remove();
	
					URL url = new URL(link.getHref());
	
					CharSequence pageContent = PageContent.getURLContent(url);
	
					Pattern patt = Pattern.compile(ANCHOR_CHECK_FILE_PAGE_TYPE,
							Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					Matcher m = patt.matcher(pageContent);
	
					if (m.find()) {
	
						String linesStr = m.group(1);
						Integer lines;
						try {
					        lines = Integer.parseInt(linesStr);
					    } catch (NumberFormatException nfe) {
					    	lines = null;
					    }
						
						String sizeStr = m.group(3);
						Integer size;
						try {
							String[] splited = sizeStr.split("\\s+");
							Float value = Float.parseFloat(splited[0]);
							
							int multiplier = 1;
							if (sizeStr.toLowerCase().endsWith("kb")) {
								multiplier = 1024;
							} else if (sizeStr.toLowerCase().endsWith("mb")) {
								multiplier = 1024 * 1024;
							}
							size = Math.round(value * multiplier);	
					    } catch (NumberFormatException nfe) {
					    	size = null;
					    }
	
						File file = new File().setName(link.getFileName()).setPath(link.getHref()).setLines(lines)
								.setSize(size).setExtension(FilenameUtils.getExtension(link.getFileName()));
	
						files.add(file);
					} else {
						patt = Pattern.compile(ANCHOR_LINK, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
						m = patt.matcher(pageContent);
						while (m.find()) {
							String href = m.group(4);
							String tagValue = m.group(5);
	
							Link lnk = new Link().setFileName(tagValue).setHref(BASE_URL + href);
	
							urls.add(lnk);
						}
					}
				}
	
				pageRepository.save(page);
			}
	
			return page;
		} catch (MalformedURLException e) {
			throw new Exception("Invalid URL");
		} catch (IOException e) {
			throw new Exception("URL was not found");
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@Accessors(chain = true)
	class Link {

		private String fileName;
		private String href;

	}
}
