import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

  /*AUTHOR;GENRE;TITLE;SERIES;SERNO;FILE;SIZE;LIBID;DEL;EXT;DATE;LANG;KEYWORDS;<CR><LF>
    Разделитель полей записи (вместо ';') - <0x04>
    Завершают запись символы <CR><LF> - <0x0D,0x0A> */

public class Reader {
    private static List<String> list = new ArrayList<>();
    private static Set<String> authorsSet = new TreeSet<>();
    private static List<String> booksList = new ArrayList<>();


    public static void main(String[] args) throws SQLException {
        readFromArchive();
    }

    private static void readFromArchive() throws SQLException {
        try (ZipFile zipFile = new ZipFile("F:\\JavaProjects\\LibrusecLibArchive\\librusec_local_fb2.inpx")) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".inp")) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
                        while (br.ready()) {
                            list.add(br.readLine());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        lineEditor();
    }

    private static void lineEditor() throws SQLException {
        String[] buf;
        String[] authorsBuf;
        for (String i : list) {
            buf = i.split("\u0004",2);
            authorsBuf = buf[0].split(":");
            booksList.add(buf[1]);
            for (String s : authorsBuf) {
                authorsSet.add(s.replaceAll("[.,]", " ").
                        replaceAll("\\s+", " ").trim());
            }
        }
        WorkWithDB.updateTables(authorsSet, booksList);
    }
}

