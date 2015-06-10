package com.gibbsdevops.alfred.config.worker;

import com.gibbsdevops.alfred.config.common.AlfredConfigProperties;
import com.gibbsdevops.alfred.config.common.JmsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.lang.management.ManagementFactory;

@Configuration
@Import(JmsConfig.class)
public class WorkerMain {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerMain.class);

    public static void main(String[] args) throws Exception {

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.registerShutdownHook();
        context.register(AlfredConfigProperties.class);
        context.refresh();
        context.start();

        context.setConfigLocation("com.gibbsdevops.alfred.config.worker");
        context.refresh();

        long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        long bootDuration = System.currentTimeMillis() - jvmStartTime;
        LOG.info("Application startup complete in {}ms", bootDuration);

    }

}
