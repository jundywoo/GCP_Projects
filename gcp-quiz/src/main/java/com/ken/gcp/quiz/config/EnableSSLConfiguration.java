package com.ken.gcp.quiz.config;

import java.io.IOException;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class EnableSSLConfiguration {

	@Bean
	public EmbeddedServletContainerFactory servletContainer( //
			@Value("${server.enable.redirect:false}") final boolean redirect, //
			@Value("${server.port:80}") int serverPort, //
			@Value("${server.port.ssl:443}") int serverPortSSL, //
			@Value("${server.truststoreType:JKS}") String keystoreType, //
			@Value("${server.truststore}") Resource keystoreFile, //
			@Value("${server.truststore.password}") String keystorePassword, //
			@Value("${server.truststore.alias}") String keystoreAlias) throws IOException {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				if (redirect) {
					SecurityConstraint securityConstraint = new SecurityConstraint();
					securityConstraint.setUserConstraint("CONFIDENTIAL");
					SecurityCollection collection = new SecurityCollection();
					collection.addPattern("/*");
					securityConstraint.addCollection(collection);

					context.addConstraint(securityConstraint);
				}
			}
		};

		final String keystoreFilePath = keystoreFile.getFile().getAbsolutePath();

		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("https");
		connector.setPort(serverPortSSL);
		connector.setRedirectPort(serverPort);
		connector.setSecure(true);

		connector.setAttribute("protocol", "org.apache.coyote.http11.Http11Protocol");
		connector.setAttribute("connectionTimeout", "20000");
		connector.setAttribute("compression", "force");
		connector.setAttribute("SSLEnabled", true);
		connector.setAttribute("sslProtocol", "TLS");
		connector.setAttribute("sslEnabledProtocols", "TLSv1,TLSv1.1,TLSv1.2");
		connector.setAttribute("ciphers",
				"TLS_DHE_RSA_WITH_AES_128_CBC_SHA256," + "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256,"
						+ "TLS_DHE_DSS_WITH_AES_128_CBC_SHA," + "TLS_RSA_WITH_AES_128_CBC_SHA,"
						+ "TLS_RSA_WITH_AES_128_CBC_SHA256," + "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,"
						+ "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");
		connector.setAttribute("clientAuth", false);
		connector.setAttribute("keystoreFile", keystoreFilePath);
		connector.setAttribute("keystoreType", keystoreType);
		connector.setAttribute("keystorePass", keystorePassword);
		connector.setAttribute("keystoreAlias", keystoreAlias);

		tomcat.addAdditionalTomcatConnectors(connector);

		return tomcat;
	}

}
