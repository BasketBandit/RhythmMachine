package uk.co.codefreak.rhythmmachine.world;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;

public class MapList implements Serializable {

    private Path path = Paths.get("src/maps");
    private ArrayList<Map> maps;
    private MapSerialize serialize;

    public MapList() {
        // Changing the path is as simple as putting a file path in a settings file and then reading and setting before calling findMaps()
        maps = new ArrayList<>();
        serialize = new MapSerialize();
        findMaps();
    }

    private int findMaps() {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.map")) {
            int mapsFound = 0;
            Iterator it = stream.iterator();
            while(it.hasNext()) {
                String pathString = it.next().toString();
                Path pathPath = Paths.get(pathString);
                Map map = serialize.unserialize(pathPath);
                maps.add(map);
                mapsFound++;
            }
            return mapsFound;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Map getMap(int map) {
        return maps.get(map);
    }

}
