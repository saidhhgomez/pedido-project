package com.restaurant.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BackblazeConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = BackblazeConfig.class
                .getClassLoader()
                .getResourceAsStream("backblaze.properties")) {

            if (input == null) {
                throw new RuntimeException("No se encontró backblaze.properties en resources");
            }

            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Error cargando backblaze.properties", e);
        }
    }
    
    public static String getPublicFileEndpoint() {
        String endpoint = props.getProperty("b2.publicFileEndpoint");
        if (endpoint == null || endpoint.isEmpty()) {
            throw new RuntimeException("Falta la propiedad 'b2.publicFileEndpoint' en backblaze.properties.");
        }
        return endpoint;
    }

    public static String getKeyId() {
        return props.getProperty("b2.keyId");
    }

    public static String getApplicationKey() {
        return props.getProperty("b2.applicationKey");
    }

    public static String getBucketName() {
        return props.getProperty("b2.bucketName");
    }

    public static String getRegion() {
        return props.getProperty("b2.region");
    }

    public static String getEndpoint() {
        return "https://s3." + getRegion() + ".backblazeb2.com";
    }
    
    public static String getFolder() {
        return props.getProperty("b2.folder", "");
    }
    
    public static String getPrefix() {
        return props.getProperty("b2.prefix", "");
    }
}