package com.sam.repo;

import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateCode {

  public static void main(String[] args)
          throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException,
          InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {

      try {
          String stringKey = "example";
          MessageDigest digest = MessageDigest.getInstance("SHA-256");
          byte[] key = digest.digest(
                  "5791".getBytes(StandardCharsets.UTF_8));
          //byte[] key = HashUtils.SHA256(stringKey);
          byte[] input = "this is a test".getBytes();
          byte[] output = null;

          SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
          Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // this is actually aes 256 ecb and NOT aes-128 as we passed a 32bytes key
          cipher.init(Cipher.ENCRYPT_MODE, keySpec);
          output = cipher.doFinal(input);
          System.out.println(Base64Utils.encodeToString(output));


      } catch (Exception ex) {
          throw new RuntimeException("Unable to AES-Encrypt: " + ex.getMessage());
      }
  }
}
