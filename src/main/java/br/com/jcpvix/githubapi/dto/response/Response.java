package br.com.jcpvix.githubapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    private T payload;
    private ResponseError error;

    public static <T> Response<T> ok(T payload) {
        Response<T> response = new Response<>();
        response
           .setPayload(payload);
        return response;
    }

    public static <T> Response<T> error(ResponseError error) {
        Response<T> response = new Response<>();
        response
           .setError(error);
        return response;
    }

}

