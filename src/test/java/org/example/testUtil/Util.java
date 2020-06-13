package org.example.testUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Util {
    public static byte[] giveImageAsByteArray() {
        File file = new File("C:\\Users\\jaapw\\Desktop\\markplace 2.0\\images\\image.jpg");
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            System.out.println("oops");
        }
        return null;
    }
}
