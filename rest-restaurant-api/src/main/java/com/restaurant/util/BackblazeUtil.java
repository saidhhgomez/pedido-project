package com.restaurant.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.restaurant.config.BackblazeConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BackblazeUtil {

    private static final AmazonS3 s3Client = initializeClient();

    private static AmazonS3 initializeClient() {
        System.out.println("Inicializando AmazonS3 Client para Backblaze...");
        
        String keyIdLeido = BackblazeConfig.getKeyId();
        String regionLeida = BackblazeConfig.getRegion();
        
        System.out.println("DEBUG: Key ID leída: " + keyIdLeido); 
        System.out.println("DEBUG: Región leída: " + regionLeida);
        System.out.println("DEBUG: Endpoint calculado: " + BackblazeConfig.getEndpoint());

        AWSCredentials credentials = new BasicAWSCredentials(
                keyIdLeido,
                BackblazeConfig.getApplicationKey()
        );

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                BackblazeConfig.getEndpoint(),
                                BackblazeConfig.getRegion()
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .enablePathStyleAccess() 
                .build();
    }

 // LISTAR ARCHIVOS
    public static List<String> listFiles(String prefix) {

        String bucket = BackblazeConfig.getBucketName();
        
        String effectivePrefix = "";
        
        if (!prefix.isEmpty()) {
            effectivePrefix = prefix.startsWith(BackblazeConfig.getFolder())
                ? prefix
                : BackblazeConfig.getFolder() + prefix;
        } 

        List<String> files = new ArrayList<>();

        try {
            ListObjectsV2Result result = s3Client.listObjectsV2(bucket, effectivePrefix); 

            for (S3ObjectSummary summary : result.getObjectSummaries()) {
                files.add(summary.getKey());
            }

            return files;

        } catch (Exception e) {
            System.err.println("Error al listar archivos: " + e.getMessage()); 
            throw new RuntimeException(e);
        }
    }

    // SUBIR
    public static String uploadFile(String key, File file) {
        
        String bucket = BackblazeConfig.getBucketName();
        String prefix = BackblazeConfig.getFolder();

        String finalKey = key.startsWith(prefix) ? key : prefix + key;

        PutObjectRequest request = new PutObjectRequest(bucket, finalKey, file);

        s3Client.putObject(request);

        System.out.println("Archivo subido: " + finalKey);
        
        return finalKey;
    }

    // DESCARGAR
    public static File downloadFile(String key) {

        String bucket = BackblazeConfig.getBucketName();

        String fullKey = key.startsWith(BackblazeConfig.getFolder())
                ? key
                : BackblazeConfig.getFolder() + key;

        try {
            S3Object object = s3Client.getObject(bucket, fullKey);
            InputStream in = object.getObjectContent();

            File temp = File.createTempFile("b2_", "_" + new File(key).getName());
            FileOutputStream out = new FileOutputStream(temp);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();
            out.close();

            return temp;

        } catch (Exception e) {
            System.err.println("Error al descargar archivo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // ELIMINAR ARCHIVO
    public static void deleteFile(String key) {

        String bucket = BackblazeConfig.getBucketName();

        String fullKey = key.startsWith(BackblazeConfig.getFolder())
                ? key
                : BackblazeConfig.getFolder() + key;

        try {
            s3Client.deleteObject(bucket, fullKey);
            System.out.println("Archivo eliminado: " + fullKey);

        } catch (Exception e) {
            System.err.println("Error al eliminar archivo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // VERIFICAR EXISTENCIA
    public static boolean fileExists(String key) {
        
        String bucket = BackblazeConfig.getBucketName();

        String fullKey = key.startsWith(BackblazeConfig.getFolder())
                ? key
                : BackblazeConfig.getFolder() + key;

        try {
            return s3Client.doesObjectExist(bucket, fullKey);
        } catch (Exception e) {
            System.err.println("Error al verificar existencia de archivo: " + e.getMessage());
            return false;
        }
    }
}