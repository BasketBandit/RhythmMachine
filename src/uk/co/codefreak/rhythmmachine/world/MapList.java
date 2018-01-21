package uk.co.codefreak.rhythmmachine.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class MapList implements Serializable {

    private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    private ArrayList<Map> maps;

    public MapList() {
        maps = new ArrayList<>();
        findMaps();
    }

    private int findMaps() {
        try {

            BufferedReader count = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream("maps/world.maps")));
            int mapCount = 0;

            while(!count.readLine().equals("END")) {
                mapCount++;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream("maps/world.maps")));

            for(int i = 0; i < mapCount; i++) {
                Map map = new MapSerialize().unserialize(in.readLine());
                maps.add(map);
            }

            return mapCount;
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
