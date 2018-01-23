package uk.co.codefreak.rhythmmachine.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class MapList implements Serializable {

    // Classloader allowing access to the resource folder after build
    // Has to have the transient modifier because ClassLoader doesn't implement Serializable.
    private transient ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    private Map[] maps;

    public MapList() {
        findMaps();
    }

    private void findMaps() {
        try {
            BufferedReader count = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream("maps/world.maps")));
            int mapCount = 0;

            while(!count.readLine().equals("END")) {
                mapCount++;
            }

            maps = new Map[mapCount];

            BufferedReader in = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream("maps/world.maps")));

            for(int i = 0; i < mapCount; i++) {
                Map map = new MapSerialize().unserialize("maps/"+in.readLine()+".map");
                maps[i] = map;
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Map getMap(String name) {
        for(Map map : maps) {
            if(map.getName().equals(name)) {
                return map;
            }
        }
        return null;
    }

    public int mapsTotal() {
        return maps.length;
    }

}
