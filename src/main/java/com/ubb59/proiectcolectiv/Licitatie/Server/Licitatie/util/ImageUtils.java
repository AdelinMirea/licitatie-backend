package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ImageUtils {

    private static final String EXTENSION = "JPG";
    private static final String BASE_PATH = "images/";

    private static File getImageFromName(String name) throws IOException {
        String path = BASE_PATH + name;
        File image = new File(
                URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(path).getPath(), "UTF-8")
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

    public static String imageToBase64String(File image) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(image);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/image");
        file.transferTo(convFile);
        return convFile;
    }

    public static List<String> saveMultipartFiles(MultipartFile[] files){
        List<String> encodedImages =  Arrays.asList(files)
                .parallelStream()
                .map(multipartFile -> {
                    try {
                        File image = ImageUtils.convertMultipartFileToFile(multipartFile);
                        return ImageUtils.imageToBase64String(image);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<String> imageNames = new ArrayList<>();
        encodedImages.forEach(image -> {
            try {
                String fileName = addImage(image);
                imageNames.add(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return imageNames;
    }

    private static File base64StringToImage(String encodedImage, String imageName) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        String path = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(BASE_PATH).getPath(), "UTF-8");
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
            File tempImage = new File(URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(BASE_PATH).getPath() + fileName, "UTF-8"));
            exists = tempImage.exists();
        } while (exists);
        File image = base64StringToImage(encodedImage, fileName);
        if (!isFileImage(image)) {
            throw new IOException("File is not an image");
        }
        return fileName;
    }

    public static void removeImage(String fileName) throws IOException {
        File image = new File(URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(BASE_PATH).getPath() + fileName, "UTF-8"));
        boolean exists = image.exists();
        if (!exists) {
            throw new IOException("Image does not exist");
        }
        if (!image.delete()) {
            throw new IOException("Image was not deleted");
        }
    }
}
