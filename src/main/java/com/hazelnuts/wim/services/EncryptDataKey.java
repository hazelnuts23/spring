package com.hazelnuts.wim.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;
import software.amazon.awssdk.services.kms.model.KmsException;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

import java.nio.charset.StandardCharsets;


public class EncryptDataKey {
    @Autowired
    private Environment env;

    public KmsClient kmsClient;
    public String keyId;
    public String data;

    public EncryptDataKey(){
        this.keyId = "arn:aws:kms:ap-southeast-1:546322307258:key/42dc4ebc-a307-4a3b-a6b0-6cd57dd5b9b3";
        Region region = Region.AP_SOUTHEAST_1;
        this.kmsClient = KmsClient.builder()
                .region(region)
                .build();
    }

    public byte[] encryptData(String data) {
        try {
            SdkBytes myBytes = SdkBytes.fromByteArray(data.getBytes(StandardCharsets.UTF_8));
            EncryptRequest encryptRequest = EncryptRequest.builder()
                    .keyId(this.keyId)
                    .plaintext(myBytes)
                    .build();

            EncryptResponse response = this.kmsClient.encrypt(encryptRequest);
            String algorithm = response.encryptionAlgorithm().toString();

            // Get the encrypted data.
            SdkBytes encryptedData = response.ciphertextBlob();
            return encryptedData.asByteArray();

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public SdkBytes decryptData(SdkBytes encryptedData) {
        try {
            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(encryptedData)
                    .keyId(this.keyId)
                    .build();

            DecryptResponse decryptResponse = this.kmsClient.decrypt(decryptRequest);
            return decryptResponse.plaintext();

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
    public void setKmsClient(KmsClient kmsClient) {
        this.kmsClient = kmsClient;
    }
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getKeyId() {
        return keyId;
    }
    public String getData() {
        return data;
    }

    public KmsClient getKmsClient() {
        return kmsClient;
    }
}

