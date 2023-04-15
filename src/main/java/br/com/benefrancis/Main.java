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

public class Main {

    public static void main(String[] args) throws ImageProcessingException, IOException {

        File diretorio = new File("imagens");

        List<File> files;

        if (diretorio.isDirectory()) {

            files = Arrays.asList(diretorio.listFiles());

            for (File f : files) {

                System.out.println(f.getName() + "\n");

                if (f.isFile()) {
                    Metadata metadata = ImageMetadataReader.readMetadata(f);

                    for (Directory directory : metadata.getDirectories()) {
                        for (Tag tag : directory.getTags()) {
                            System.out.format("[%s] - %s = %s%n",
                                    directory.getName(), tag.getTagName(), tag.getDescription());
                        }
                        if (directory.hasErrors()) {
                            for (String error : directory.getErrors()) {
                                System.err.format("ERROR: %s", error);
                            }
                        }
                    }
                }
            }


        }


    }
}