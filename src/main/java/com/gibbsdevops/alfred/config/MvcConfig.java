package com.gibbsdevops.alfred.config;

import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.xml.transform.Source;
import java.util.List;

@EnableWebMvc
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MvcConfig.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(stringConverter);
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<Source>());
        converters.add(new AllEncompassingFormHttpMessageConverter());

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
