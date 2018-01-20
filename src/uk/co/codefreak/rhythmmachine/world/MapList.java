package uk.co.codefreak.rhythmmachine.world;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class MapList implements Serializable {

    private Path path = Paths.get("src/resources/maps");
    private ArrayList<Map> maps;

    public MapList() {
        maps = new ArrayList<>();
        findMaps();
    }

    private int findMaps() {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.map")) {
            int mapsFound = 0;
            Iterator it = stream.iterator();
            while(it.hasNext()) {
                String pathString = it.next().toString();
                Path pathPath = Paths.get(pathString);
                Map map = new MapSerialize().unserialize(pathPath);
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

    public int mapsTotal() {
        return maps.size();
    }

}
