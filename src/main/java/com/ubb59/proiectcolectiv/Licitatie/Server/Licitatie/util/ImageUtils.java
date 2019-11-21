package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    private static final String EXTENSION = "JPG";
    private static final String BASE_PATH = "images/";

    private static File getImageFromName(String name) throws IOException {
        String path = BASE_PATH + name;
        File image = new File(
                ClassLoader.getSystemClassLoader().getResource(path).getFile().replace("%20", " ")
        );
        boolean exists = image.exists();
        if (!exists) {
            throw new IOException("Image does not exist");
        }
        if (isFileImage(image)) {
            return image;
        } else {
            throw new IOException("File is not an image");
        }
    }

    private static boolean isFileImage(File image) {
        String mimeType = new MimetypesFileTypeMap().getContentType(image);
        String type = mimeType.split("/")[0];
        return type.equals("image");
    }

    private static String imageToBase64String(File image) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(image);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private static File base64StringToImage(String encodedImage, String imageName) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        String path = ClassLoader.getSystemClassLoader().getResource(BASE_PATH).getPath().replace("%20", " ");
        File image = new File(path, imageName);
        if (image.createNewFile()) {
            FileUtils.writeByteArrayToFile(image, decodedBytes);
            return image;
        } else {
            throw new IOException("Image could not be created");
        }
    }

    public static String getEncodedImageFromImageName(String name) throws IOException {
        File image = getImageFromName(name);
        return imageToBase64String(image);
    }

    public static String addImage(String encodedImage) throws IOException {
        boolean exists;
        String fileName;
        do {
            fileName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(32), EXTENSION);
            File tempImage = new File(ClassLoader.getSystemClassLoader().getResource(BASE_PATH).getPath().replace("%20", " ") + fileName);
            exists = tempImage.exists();
        } while (exists);
        File image = base64StringToImage(encodedImage, fileName);
        if (!isFileImage(image)) {
            throw new IOException("File is not an image");
        }
        return fileName;
    }

    public static void removeImage(String fileName) throws IOException {
        File image = new File(ClassLoader.getSystemClassLoader().getResource(BASE_PATH).getPath().replace("%20", " ") + fileName);
        boolean exists = image.exists();
        if (!exists) {
            throw new IOException("Image does not exist");
        }
        if (!image.delete()) {
            throw new IOException("Image was not deleted");
        }
    }
}
