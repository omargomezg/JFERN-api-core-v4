package com.southpurity.apicore.controller;

import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Collection;

@ControllerAdvice
public class JsonViewConfiguration extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType);
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {

        Class<?> viewClass = View.Anonymous.class;

        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            if (authorities.stream().anyMatch(o -> o.getAuthority().equals(RoleEnum.CUSTOMER.name()))) {
                viewClass = View.Customer.class;
            }
            if (authorities.stream().anyMatch(o -> o.getAuthority().equals(RoleEnum.STOCKER.name()))) {
                viewClass = View.Stocker.class;
            }
            if (authorities.stream().anyMatch(o -> o.getAuthority().equals(RoleEnum.ADMINISTRATOR.name()))) {
                viewClass = View.Administrator.class;
            }
        }
        bodyContainer.setSerializationView(viewClass);
    }
}
