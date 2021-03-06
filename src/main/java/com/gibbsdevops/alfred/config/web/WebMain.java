package com.gibbsdevops.alfred.config.web;

import com.gibbsdevops.alfred.config.common.AlfredConfigProperties;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;

public class WebMain {

    private static final Logger LOG = LoggerFactory.getLogger(WebMain.class);

    public static void main(String[] args) throws Exception {

        String devResources = "src/main/resources/webapp";
        String testResources = "src/test/resources/webapp";
        boolean devMode = new File(devResources).exists();

        AnnotationConfigWebApplicationContext configContext = new AnnotationConfigWebApplicationContext();
        configContext.register(AlfredConfigProperties.class);
        configContext.refresh();
        configContext.start();
        AlfredConfigProperties config = configContext.getBean(AlfredConfigProperties.class);
        int httpPort = config.getHttpPort();
        configContext.close();

        /* Spring Config */
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.registerShutdownHook();
        context.setConfigLocation("com.gibbsdevops.alfred.config.web");

        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(404, "/not-found");

        /* API Handler */
        ServletContextHandler apiHandler = new ServletContextHandler();
        apiHandler.setErrorHandler(null);
        apiHandler.setContextPath("/api");
        apiHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/");
        apiHandler.addEventListener(new ContextLoaderListener(context));
        apiHandler.setResourceBase("src/main/resources/api");
        apiHandler.setErrorHandler(errorHandler);

        /* Resource Handler */
        List<Resource> resourceList = Lists.newArrayList();

        if (devMode) {
            LOG.info("Adding dev resources");
            resourceList.add(Resource.newResource(devResources));
            resourceList.add(Resource.newResource(testResources));
            resourceList.add(Resource.newResource("target/classes/webapp"));
        } else {
            resourceList.add(Resource.newClassPathResource("/webapp"));
        }

        ResourceCollection resources = new ResourceCollection();
        resources.setResources(resourceList.toArray(new Resource[]{}));

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setBaseResource(resources);

        // http://download.eclipse.org/jetty/9.2.6.v20141205/apidocs/org/eclipse/jetty/servlet/DefaultServlet.html
        webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.etags", "true");
        if (devMode) {
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        } else {
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "true");
        }

        /* Handler List */
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{
                apiHandler,
                webAppContext,
                new DefaultHandler()
        });

        /* Setup Server */
        Server server = new Server(httpPort);
        server.setHandler(handlers);
        server.setStopAtShutdown(true);

        try {
            server.start();
        } catch (Exception ex) {
            LOG.error("Failed to start application server", ex);
            System.exit(1);
        }

        long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        long bootDuration = System.currentTimeMillis() - jvmStartTime;
        LOG.info("Application startup complete in {}ms", bootDuration);

        server.join();
    }
}
