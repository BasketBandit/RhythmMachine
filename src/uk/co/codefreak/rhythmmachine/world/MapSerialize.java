package uk.co.codefreak.rhythmmachine.world;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MapSerialize implements Serializable {

    // Classloader allowing access to the resource folder after build
    private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    private Map map;
    private Path path = Paths.get("src/resources/maps");

    public MapSerialize() {
        // Remember to replace with filepath from settings file.
    }

    /**
     *
     * @param filePathOut the file path in which the new serialized map is to be saved, without file name or extension.
     * @return number of maps serialized, or -1 if uncaught exception
     *
     */
    public int serialize(String filePathOut) {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.smap")) {

            int mapsSerialized = 0;
            Iterator it = stream.iterator();

            // Create a new BufferedWrite which takes a file writer which creates a list of all the map path locations.
            BufferedWriter mapList = new BufferedWriter(new FileWriter(filePathOut+"world"+".maps"));

            while(it.hasNext()) {
                try {
                    String location = it.next().toString();
                    String mapString[] = location.split(Pattern.quote("\\"));
                    String mapString2 = mapString[3];
                    String mapSplit[] = mapString2.split(Pattern.quote("."));
                    String mapName = mapSplit[0];

                    this.map = new Map(location);
                    FileOutputStream fileOut = new FileOutputStream(filePathOut+mapName+".map");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(this.map);
                    out.close();
                    fileOut.close();

                    // Write the next map location to the file, then new line.
                    mapList.write("maps/"+mapName+".map");
                    mapList.newLine();

                    System.out.printf("Serialized data has been successfully created. \n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mapsSerialized++;
            }

            // Finish writing to the file.
            mapList.write("END");
            mapList.close();

            return mapsSerialized;

        } catch(IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param filename the filename of the map to be unserialized.
     * @return unserialized map object if success, else return null
     *
     */
    public Map unserialize(String filename) {
        Map e;
        try {
            ObjectInputStream ob = new ObjectInputStream(classloader.getResourceAsStream(filename));
            e = (Map) ob.readObject();
            ob.close();

            return e;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Map no found. :( ");
            c.printStackTrace();
            return null;
        }
    }
}
