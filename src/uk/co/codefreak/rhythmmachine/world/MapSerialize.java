package uk.co.codefreak.rhythmmachine.world;

import java.io.*;
import java.nio.file.*;
import java.util.Iterator;

public class MapSerialize implements Serializable {

    private Map map;
    private Path path = Paths.get("src/resources/maps");

    public MapSerialize() {
        // Remember to replace with filepath from settings file.
    }

    public MapSerialize(Map mapIn) {
        serializeSpecific(mapIn);
    }

    /**
     *
     * @param filePath the file path for the map, including the file's name and extension. E.g. "C:\\Josh\\Desktop\\test.map".
     * @param filePathTo the file path in which the new serialized map is to be saved, without file name or extension.
     * @param fileNameTo the name of the file that is to be written.
     *
     */
    public void serializeSpecific(String filePath, String filePathTo, String fileNameTo) {
        try {
            map = new Map(filePath);
            FileOutputStream fileOut = new FileOutputStream(filePathTo+fileNameTo);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(map);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data has been successfully created. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param mapIn a direct map object from the application.
     *
     */
    public void serializeSpecific(Map mapIn) {
        // Remember to replace with filepath from settings file.
        try {
            FileOutputStream fileOut = new FileOutputStream("src/resources/maps/t.map");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(mapIn);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data has been successfully created. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param filePathTo the file path in which the new serialized map is to be saved, without file name or extension.
     * @param fileNameTo the name of the file that is to be written.
     * @return number of maps serialized, or -1 if uncaught exception
     *
     */
    public int serializeAll(String filePathTo, String fileNameTo) {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.smap")) {
            int mapsSerialized = 0;
            Iterator it = stream.iterator();
            while(it.hasNext()) {
                try {
                    this.map = new Map(it.next().toString());
                    FileOutputStream fileOut = new FileOutputStream(filePathTo+fileNameTo+mapsSerialized+".map");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(this.map);
                    out.close();
                    fileOut.close();
                    System.out.printf("Serialized data has been successfully created. \n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mapsSerialized++;
            }
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
    public Map unserialize(Path filename) {
        Map e;
        try {
            FileInputStream fileIn = new FileInputStream(filename.toString());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (Map) in.readObject();
            in.close();
            fileIn.close();
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
