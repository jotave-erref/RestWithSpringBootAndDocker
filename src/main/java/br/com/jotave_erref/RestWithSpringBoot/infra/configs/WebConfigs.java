package br.com.jotave_erref.RestWithSpringBoot.infra.configs;

import br.com.jotave_erref.RestWithSpringBoot.serialization.converter.YamlJackson2HttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfigs implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        /*
        VIA QUERY PARAM http://localhost:8080/api/books/1?mediaType=xml
        configurer.favorParameter(true) //Habilita a negociação por parâmetro
                .parameterName("mediaType").ignoreAcceptHeader(true) //Ignora o cabeçalho Accept se estiver definido
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);
        */

        //VIA HEADER http://localhost:8080/api/books/1
        configurer.favorParameter(false) //aceita parametros
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("xml", MEDIA_TYPE_APPLICATION_YML);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJackson2HttpMessageConverter());
    }
}
