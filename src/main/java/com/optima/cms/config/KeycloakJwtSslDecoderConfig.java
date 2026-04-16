package com.optima.cms.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import java.time.Duration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Custom {@link JwtDecoder} for HTTPS to Keycloak (OIDC discovery + JWKS) when the JDK does not trust the IdP cert (PKIX).
 * <p>
 * Activate with either {@code keycloak.resource-server.ssl.bundle} (Spring {@link SslBundles}) or
 * {@code keycloak.resource-server.truststore.location} + password — default {@code classpath:ssl/truststore.jks}
 * (see {@code src/main/resources/ssl/README.txt}).
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(OAuth2ResourceServerAutoConfiguration.class)
@ConditionalOnProperty(name = "keycloak.resource-server.enabled", havingValue = "true")
public class KeycloakJwtSslDecoderConfig {

	static class KeycloakJwtTlsCondition extends AnyNestedCondition {

		KeycloakJwtTlsCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnProperty(name = "keycloak.resource-server.ssl.bundle")
		static class HasSslBundle {
		}

		@ConditionalOnProperty(name = "keycloak.resource-server.truststore.location")
		static class HasTruststore {
		}
	}

	@Bean
	@Conditional(KeycloakJwtTlsCondition.class)
	JwtDecoder jwtDecoder(
			@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
			ObjectProvider<SslBundles> sslBundles,
			@Value("${keycloak.resource-server.ssl.bundle:}") String sslBundleName,
			ResourceLoader resourceLoader,
			@Value("${keycloak.resource-server.truststore.location:}") String truststoreLocation,
			@Value("${keycloak.resource-server.truststore.password:}") String truststorePassword,
			@Value("${keycloak.resource-server.truststore.type:JKS}") String truststoreType)
			throws Exception {
		RestTemplate restTemplate;
		if (StringUtils.hasText(sslBundleName)) {
			SslBundles bundles = sslBundles.getIfAvailable();
			if (bundles == null) {
				throw new IllegalStateException(
						"keycloak.resource-server.ssl.bundle is set but no SslBundles bean; define spring.ssl.bundle.* for that name.");
			}
			restTemplate = restTemplateForSslContext(bundles.getBundle(sslBundleName).createSslContext());
		} else if (StringUtils.hasText(truststoreLocation)) {
			Resource resource = resourceLoader.getResource(truststoreLocation);
			if (!resource.exists()) {
				throw new IllegalStateException(
						"keycloak.resource-server.truststore.location not found: " + truststoreLocation);
			}
			SSLContext sslContext = sslContextFromTruststore(resource, truststorePassword, truststoreType);
			restTemplate = restTemplateForSslContext(sslContext);
		} else {
			throw new IllegalStateException("TLS customization enabled but neither ssl.bundle nor truststore.location resolved.");
		}
		return NimbusJwtDecoder.withIssuerLocation(issuerUri)
				.restOperations(restTemplate)
				.build();
	}

	private static RestTemplate restTemplateForSslContext(SSLContext sslContext) {
		HttpClient httpClient = HttpClient.newBuilder()
				.sslContext(sslContext)
				.connectTimeout(Duration.ofSeconds(20))
				.build();
		return new RestTemplate(new JdkClientHttpRequestFactory(httpClient));
	}

	private static SSLContext sslContextFromTruststore(Resource truststore, String password, String type)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException {
		KeyStore keyStore = KeyStore.getInstance(type);
		char[] pw = StringUtils.hasText(password) ? password.toCharArray() : new char[0];
		try (InputStream is = truststore.getInputStream()) {
			keyStore.load(is, pw);
		}
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tmf.getTrustManagers(), null);
		return sslContext;
	}
}
