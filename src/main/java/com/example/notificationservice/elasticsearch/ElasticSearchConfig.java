package com.example.notificationservice.elasticsearch;

import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import javax.net.ssl.SSLContext;

public class ElasticSearchConfig {

    @Configuration
    public class ElasticSearchConfiguration extends ElasticsearchConfiguration {

        @Override
        public ClientConfiguration clientConfiguration() {
            return ClientConfiguration.builder()
                    .connectedToLocalhost()
                    .usingSsl(buildSslContext())
                    .withBasicAuth("elastic", "changeme")
                    .build();
        }

        private static SSLContext buildSslContext() {
            try {
                return new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
