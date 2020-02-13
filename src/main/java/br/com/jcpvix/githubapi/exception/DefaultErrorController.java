package br.com.jcpvix.githubapi.exception;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jcpvix.githubapi.dto.response.Response;
import br.com.jcpvix.githubapi.dto.response.ResponseError;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping({DefaultErrorController.ERROR_PATH})
public class DefaultErrorController extends AbstractErrorController {

	static final String ERROR_PATH = "/error";

    public DefaultErrorController(final ErrorAttributes errorAttributes) {
        super(errorAttributes, Collections.emptyList());
    }

    @RequestMapping
    public ResponseEntity<?> error(HttpServletRequest request) {
        HttpStatus status = this.getStatus(request);
        Map<String, Object> attributeMap = this.getErrorAttributes(request, false);
        
        String error = ""; 
        String path = "";
        		
        if (attributeMap != null) {
        	error = (String) attributeMap.get("error");
            path = (String) attributeMap.get("path");
        }
        
        ResponseError responseError = new ResponseError()
        		.setTimestamp(new Date())
        		.setMessage(error)
        		.setDetails(path);        
        
		return new ResponseEntity<>(Response.error(responseError), status);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}