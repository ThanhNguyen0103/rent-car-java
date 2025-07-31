package com.example.rentCar.domain.res;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.rentCar.utils.annotation.ApiMessage;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FomatApiResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        ApiResponse<Object> res = new ApiResponse<>();

        ApiMessage message = returnType.getMethod().getAnnotation(ApiMessage.class);
        int status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        ///////
        if (body instanceof String) {
            return body;
        }

        // success

        if (status >= 400) {
            return body;
        } else {
            res.setData(body);
            res.setMessage(message != null ? message.value() : "");
            res.setStatusCode(status);
        }
        // error

        return res;

    }

}
