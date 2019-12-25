import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;
import java.util.*;

//F:\JavaProjects\LibrusecLibArchive\librusec_local_fb2.inpx

public class Reader {
//    private static Map<String, Integer> authorsMap = new TreeMap<>();
    private static Set<String> authorsSet = new TreeSet<>();
    private static Set<String> booksSet = new TreeSet<>();

    public static void main(String[] args) throws SQLException, ParseException {
        readFromArchive();
        WorkWithDB.updateTables(authorsSet, booksSet);
    }

    private static void readFromArchive() {
        String[] buf;
        String[] authorsBuf;
        String books = "";
        try (ZipFile zipFile = new ZipFile("F:\\JavaProjects\\LibrusecLibArchive\\librusec_local_fb2.inpx");
             BufferedWriter authorsBw = new BufferedWriter(
                     new FileWriter("F:\\JavaProjects\\LibrusecLibArchive\\libAuthors.txt"));
             BufferedWriter booksBw = new BufferedWriter(
                     new FileWriter("F:\\JavaProjects\\LibrusecLibArchive\\libBooks.txt"))) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".inp")) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
                        while (br.ready()) {
                            buf = (br.readLine().replaceAll("\u0004+", "\u0004")
                                    .replaceAll("\\s+", " ").split("\u0004"));
                            if(9 < buf.length) {
                                for (int i = 0; i < buf.length; i++) {
                                    buf[i] = buf[i].trim();
                                    if (i < 2 && (buf[i].endsWith(":")))
                                        buf[i] = buf[i].substring(0, buf[i].length() - 1);
                                }
                                authorsBuf = buf[0].split(":");
                                for (String s : authorsBuf) {
                                    authorsSet.add(s.replaceAll("[.,]", " ").
                                            replaceAll("\\s+", " ").
                                            replaceAll(" :", ":").trim());
                                }
                                books = "\u0004" + buf[2] + "\u0004" + buf[1] + "\u0004" + buf[4]
                                        + "\u0004" + buf[7] + "\u0004" + buf[8] + "\u0004" + buf[9];
                                booksSet.add(books.trim());
                            }
                        }
                    }
                }
            }
            /*for(String i : authorsSet) {
                authorsMap.put(i, count++);
            }
            for(Map.Entry<String, Integer> pair : authorsMap.entrySet()) {
                bw.write(pair.getKey() + " - " + pair.getValue());
                bw.write("\r\n");
            }*/
            for (String i : authorsSet) {
                authorsBw.write(i);
                authorsBw.write("\r\n");
            }
            /*for (String i : booksSet) {
                booksBw.write(i);
                booksBw.write("\r\n");
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }


