package uk.co.codefreak.rhythmmachine;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TrackList {

    private Path path = Paths.get("X:\\Josh\\Desktop\\");
    private ArrayList<Track> tracks;
    private TrackSerialize serialize;

    public TrackList() {
        // Changing the path is as simple as putting a file path in a settings file and then reading and setting before calling findTracks()
        tracks = new ArrayList<>();
        serialize = new TrackSerialize();
        findTracks();
    }

    public int findTracks() {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.track")) {
            int tracksFound = 0;
            Iterator it = stream.iterator();
            while(it.hasNext()) {
                String pathString = it.next().toString();
                Path pathPath = Paths.get(pathString);
                Track track = serialize.unserialize(pathPath);
                tracks.add(track);
                tracksFound++;
            }
            return tracksFound;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
