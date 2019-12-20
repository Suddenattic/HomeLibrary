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
        Map<String, Integer> authorsMap = new TreeMap<>();
        List<Integer> numbersList = new ArrayList<>();
        int count = 0;
        String[] buf;
        String[] subBuf;
        try (ZipFile zipFile = new ZipFile("F:\\JavaProjects\\LibrusecLibArchive\\librusec_local_fb2.inpx")) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while(entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".inp")) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8));
                         BufferedWriter bw = new BufferedWriter(
                                 new FileWriter("F:\\JavaProjects\\LibrusecLibArchive\\lib.txt"))){
                        while(br.ready()) {
                            buf = (br.readLine().replaceAll("\u0004+", "\u0004")
                                    .replaceAll("\\s+", " ").split("\u0004"));
                            for(int i = 0; i < buf.length; i++)
                                buf[i] = buf[i].trim();
                            if(buf[0].endsWith(":"))
                                buf[0] = buf[0].substring(0, buf[0].length() - 1);
                            subBuf = buf[0].split(":");
                            for(int i = 0; i < subBuf.length; i ++) {
                                    authorsSet.add(subBuf[i].replaceAll("[.,]", " ").
                                            replaceAll("\\s+", " ").
                                            replaceAll(" :", ":").trim());
                            }
                            /*if(!authorsMap.containsKey(buf[0])) {
                                authorsMap.put(buf[0], count++);
                            }*/
//                            WorkWithDB.insertIntoDB(buf);
                        }

                       /* for(String i : authorsSet)
                            authorsMap.put(i, authorsSet.size() - (authorsSet.size() - count++));*/

                        /*for(Map.Entry<String, Integer> pair : authorsMap.entrySet()) {
                            bw.write(pair.getKey() + " - " + pair.getValue());
                            bw.write("\r\n");
                        }*/
                        for(String i : authorsSet) {
                            bw.write(i);
                            bw.write("\r\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(authorsSet.size());
    }
 }


