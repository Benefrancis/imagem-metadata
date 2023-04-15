package br.com.benefrancis;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
                       - Poderíamos pegar a tag com name = "GPS" e verificar se a fotografia foi tirada no mesmo
                        endereço ou nas proximidades da casa da pessoa

                       - Poderíamos integrar com sistemas de Inteligência Artificial:
                        https://cloud.google.com/vision?hl=pt-br
                        */

                        ExifSubIFDDirectory d = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                        LocalDateTime dia = null;
                        if (d != null)
                            dia = LocalDateTime
                                    .ofInstant(d.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
                                            .toInstant(), ZoneId.of("America/Sao_Paulo"));

                        for (Tag tag : directory.getTags()) {
                            //Coloquei o horário com +3 para bater com o horário exato em que a foto foi tirada:
                            //Se dia = "-" então deve-se duvidar da origem
                            System.out.format("[%s] [%s] [%s] - %s = %s%n",
                                    dia != null ? dia.plusHours(3).format(DateTimeFormatter.ISO_DATE_TIME) : "-",
                                    tag.getTagType(),
                                    directory.getName(),
                                    tag.getTagName(),
                                    tag.getDescription()
                            );
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