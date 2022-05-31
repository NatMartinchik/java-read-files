package com.martinchikn;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadFiles {
    @Test
    void zipPdf() throws Exception {
        ClassLoader classLoader = ReadFiles.class.getClassLoader();
        ZipFile zf = new ZipFile(new File("src/test/resources/Jupyter_Cheat_Sheet-2.zip"));
        try (ZipInputStream zis = new ZipInputStream(classLoader.getResourceAsStream("Jupyter_Cheat_Sheet-2.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("Jupyter_Cheat_Sheet-2.pdf");
                PDF pdf = new PDF(zis);
                org.assertj.core.api.Assertions.assertThat(pdf.text).contains("Jupyter");
                assertThat(pdf.author).contains("Erica Wine");
                assertThat(pdf.numberOfPages).isEqualTo(10);
            }

        }
    }

    @Test
    void zipCsv() throws Exception {
        ClassLoader classLoader = ReadFiles.class.getClassLoader();
        ZipFile zf = new ZipFile(new File("src/test/resources/SampleCSVFile_2kb.zip"));
        try (ZipInputStream zis = new ZipInputStream(classLoader.getResourceAsStream("SampleCSVFile_2kb.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("SampleCSVFile_2kb.csv");
                try (InputStream is = zf.getInputStream(entry)) {
                    try (CSVReader reader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        List<String[]> content = reader.readAll();
                        Assertions.assertThat(content.get(0)).contains(
                                "Eldon Base for stackable storage shelf, platinum"
                        );
                        Assertions.assertThat(content.get(1)).contains(
                                "Barry French"
                        );
                    }
                }
            }
        }
    }

    @Test
    void zipXls() throws Exception {
        ClassLoader classLoader = ReadFiles.class.getClassLoader();
        ZipFile zf = new ZipFile(new File("src/test/resources/SampleXLSFile_19kb.zip"));
        try (ZipInputStream zis = new ZipInputStream(classLoader.getResourceAsStream("SampleXLSFile_19kb.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("SampleXLSFile_19kb.xls");
                try (InputStream is = zf.getInputStream(entry)) {
                    XLS xls = new XLS(is);
                    xls.excel.getSheetAt(0)
                            .getRow(4)
                            .getCell(1)
                            .getStringCellValue().contains("'Holmes HEPA Air Purifier");
                }
            }
        }
    }
}
