/*C:\Users\kagel\Desktop\JavaTest\JavaTest.rar*/

import com.github.junrar.Archive;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class Reader {
    public static void main(String[] args) throws IOException {
        reader();
    }

    private static void reader() {
        List<String> list = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile("C:\\Users\\kagel\\Desktop\\JavaTest\\JavaTest.zip")) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while(entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    final String fileName = entry.getName();
                    if (fileName.endsWith(".txt")) {
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
                            while(br.ready())
                                list.add(br.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            for(String i : list)
                System.out.println(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }


