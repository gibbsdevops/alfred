package com.gibbsdevops.alfred.config;

import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

    private static final Logger LOG = LoggerFactory.getLogger(MvcConfig.class);

    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        addDefaultHttpMessageConverters(converters);

        // remove any existing Jackson converters
        converters.stream().filter(c -> c instanceof MappingJackson2HttpMessageConverter)
                .collect(Collectors.toList()).stream().forEach(c -> converters.remove(c));

        converters.add(converter());
        LOG.info("Configured message converters");
    }

    @Bean
    MappingJackson2HttpMessageConverter converter() {
        try {
            return new MappingJackson2HttpMessageConverter(AlfredObjectMapperFactory.get());
        } catch (Exception e) {
            throw new RuntimeException("Unable to configure json message converter", e);
        }
    }

}
