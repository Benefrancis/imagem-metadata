package br.com.benefrancis;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws ImageProcessingException, IOException {

        File diretorio = new File("imagens");

        List<File> files;

        if (diretorio.isDirectory()) {

            files = Arrays.asList(Objects.requireNonNull(diretorio.listFiles()));

            for (File f : files) {

                System.out.println(f.getName() + "\n");

                if (f.isFile()) {
                    Metadata metadata = ImageMetadataReader.readMetadata(f);

                    for (Directory directory : metadata.getDirectories()) {
                        /*
                        Poderíamos pegar a tag com name = "GPS" e verificar se a fotografia foi tirada no mesmo
                        endereço da casa da pessoa
                        */
                        for (Tag tag : directory.getTags()) {
                            System.out.format("[%s] [%s] - %s = %s%n", tag.getTagType(), directory.getName(), tag.getTagName(), tag.getDescription());
                        }
                        System.out.println("\n");
                        if (directory.hasErrors()) {
                            for (String error : directory.getErrors()) {
                                System.err.format("ERROR: %s%n%n", error);
                            }
                        }
                    }
                }
            }
        }
    }
}