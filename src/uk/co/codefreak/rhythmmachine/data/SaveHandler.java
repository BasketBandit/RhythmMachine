package uk.co.codefreak.rhythmmachine.data;

import java.io.*;

public class SaveHandler implements Serializable {

    // Save = true, Load = false
    public SaveHandler(String name, Flags flags) {
        saveGame(name, flags);
    }

    public SaveHandler(String name) {
        loadGame(name);
    }

    private void saveGame(String name, Flags flags) {
        try {
            File file = new File(System.getenv("APPDATA")+"\\RHYTHMMACHINE\\saves\\");
            file.mkdirs();

            FileOutputStream output = new FileOutputStream(System.getenv("APPDATA")+"\\RHYTHMMACHINE\\saves\\"+name+".save");
            ObjectOutputStream ob = new ObjectOutputStream(output);
            ob.writeObject(flags);
            ob.close();
            output.close();
        } catch(IOException e) {
            System.out.println("Uh oh. :( (Save IO)");
            e.printStackTrace();
        }

    }

    private Flags loadGame(String name) {
        try {
            Flags flags;
            ObjectInputStream ob = new ObjectInputStream(new FileInputStream(System.getenv("APPDATA")+"\\RHYTHMMACHINE\\saves\\"+name+".save"));
            flags = (Flags) ob.readObject();
            ob.close();
            return flags;
        } catch (IOException i) {
            System.out.println("Uh oh. :( (Load IO)");
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Save no found. :( ");
            c.printStackTrace();
            return null;
        }
    }
}
