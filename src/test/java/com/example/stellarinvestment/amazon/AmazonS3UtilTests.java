package com.example.stellarinvestment.amazon;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

class AmazonS3UtilTests {

    @Test
    public void testListFolder() {
        String folderName = "project-images/33";
        List<String> listKeys = AmazonS3Util.listFolder(folderName);
        listKeys.forEach(System.out::println);
    }

    @Test
    public void testUploadFile() throws FileNotFoundException {
        String folderName = "tes-upload";
        String fileName = "asset-20240321104716.png";
        String filePath = "C:\\Users\\Пользователь\\OneDrive\\Рабочий стол\\4 COURSE\\RESEARCH\\project_images\\" + fileName;

        InputStream inputStream = new FileInputStream(filePath);

        AmazonS3Util.uploadFile(folderName, fileName, inputStream);
    }

    @Test
    public void testDeleteFile() {
        String fileName = "test-upload/JAX-WS-Tomcat.zip";
        AmazonS3Util.deleteFile(fileName);
    }

    @Test
    public void testRemoveFolder() {
        String folderName = "test-upload";
        AmazonS3Util.removeFolder(folderName);
    }
}
