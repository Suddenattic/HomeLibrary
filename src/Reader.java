import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;
import java.util.*;

//F:\JavaProjects\LibrusecLibArchive\librusec_local_fb2.inpx

public class Reader {
    public static void main(String[] args) throws SQLException, ParseException {
        reader();
        //WorkWithDB.createDB();
    }

    private static void reader() throws ParseException {
        Set<String> authorsSet = new TreeSet<>();
        Map<Integer, String> authorsMap = new TreeMap<>();
        List<String> authorsList = new ArrayList<>();
        String[] buf;
        try (ZipFile zipFile = new ZipFile("F:\\JavaProjects\\LibrusecLibArchive\\librusec_local_fb2.inpx")) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while(entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
                        while(br.ready()) {
                            buf = (br.readLine().replaceAll("\u0004+", "\u0004")
                                    .replaceAll(",|:|\\s+", " ").split("\u0004"));
                            for(int i = 0; i < buf.length; i++)
                                buf[i] = buf[i].trim();
                            WorkWithDB.insertIntoDB(buf);
                        }
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }


