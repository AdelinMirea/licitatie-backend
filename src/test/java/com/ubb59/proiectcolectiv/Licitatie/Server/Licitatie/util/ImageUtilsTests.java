package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.util;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ImageUtilsTests {

    private String imageName = "testImage.JPG";

    @After
    public void after() throws IOException {
        for (File f : new File(ClassLoader.getSystemClassLoader().getResource("images").getPath()).listFiles()) {
            if (!f.getName().equals("testImage.JPG")) {
                f.delete();
            }
        }
    }

    @Test
    public void getEncodedImageFromImageName() throws IOException {
        ImageUtils.getEncodedImageFromImageName(imageName);
    }

    @Test
    public void addImage() throws IOException {
        String encodedImage = ImageUtils.getEncodedImageFromImageName(imageName);
        String addedImageName = ImageUtils.addImage(encodedImage);
        URL path = ClassLoader.getSystemClassLoader().getResource("images");
        File image = new File(
                ClassLoader.getSystemClassLoader().getResource("images/" + addedImageName).getFile()
        );
        assertThat(image.exists(), is(true));
    }

    @Test
    public void removeImage() throws IOException {
        String encodedImage = ImageUtils.getEncodedImageFromImageName(imageName);
        String addedImageName = ImageUtils.addImage(encodedImage);
        File image = new File(
                ClassLoader.getSystemClassLoader().getResource("images/" + addedImageName).getFile()
        );
        assertThat(image.exists(), is(true));
        ImageUtils.removeImage(addedImageName);
        assertThat(image.exists(), is(false));
    }

}
