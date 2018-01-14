package uk.co.codefreak.rhythmmachine;

import java.io.*;
import java.nio.file.*;
import java.util.Iterator;

public class TrackSerialize {

    private Track track;
    private Path path = Paths.get("X:\\Josh\\Desktop\\");

    public TrackSerialize() {
        // Remember to replace with filepath from settings file.
    }

    public TrackSerialize(Track trackIn) {
        serializeSpecific(trackIn);
    }

    /**
     *
     * @param filePath the file path for the track, including the file's name and extension. E.g. "C:\\Josh\\Desktop\\test.track".
     * @param filePathTo the file path in which the new serialized track is to be saved, without file name or extension.
     * @param fileNameTo the name of the file that is to be written.
     *
     */
    public void serializeSpecific(String filePath, String filePathTo, String fileNameTo) {
        try {
            track = new Track(filePath);
            FileOutputStream fileOut = new FileOutputStream(filePathTo+fileNameTo);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(track);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved to your desktop. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param trackIn a direct track object from the application.
     *
     */
    public void serializeSpecific(Track trackIn) {
        // Remember to replace with filepath from settings file.
        try {
            FileOutputStream fileOut = new FileOutputStream("X:\\Josh\\Desktop\\testX.track");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(trackIn);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved to your desktop. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param filePathTo the file path in which the new serialized track is to be saved, without file name or extension.
     * @param fileNameTo the name of the file that is to be written.
     * @return number of tracks serialized, or -1 if uncaught exception
     *
     */
    public int serializeAll(String filePathTo, String fileNameTo) {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.Untrack")) {
            int tracksSerialized = 0;
            Iterator it = stream.iterator();
            while(it.hasNext()) {
                try {
                    track = new Track(it.next().toString());
                    FileOutputStream fileOut = new FileOutputStream(filePathTo+fileNameTo+tracksSerialized+".track");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(track);
                    out.close();
                    fileOut.close();
                    System.out.printf("Serialized data is saved to your desktop. \n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tracksSerialized++;
            }
            return tracksSerialized;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param filename the filename of the track to be unserialized.
     * @return unserialized track object if success, else return null
     *
     */
    public Track unserialize(Path filename) {
        Track e;
        try {
            FileInputStream fileIn = new FileInputStream(filename.toString());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (Track) in.readObject();
            in.close();
            fileIn.close();
            return e;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Track no found. :( ");
            c.printStackTrace();
            return null;
        }
    }
}
