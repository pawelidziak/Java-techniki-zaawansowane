package sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClassLoader extends ClassLoader {

    List<String> loadedClassStrings = new ArrayList<>();
    Map<String, Class> loadedClasses = new HashMap<>();

    MyClassLoader() {
    }

    // parametr: paczka . clasa
    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
        if (name.startsWith(Configuration.NAME_OF_FOLDER)) {
            return getClass(name);
        }
        return super.loadClass(name);
    }

    private Class getClass(String name) throws ClassNotFoundException {
        String file = name.replace('.', File.separatorChar) + ".class";

        byte[] bytes = null;
        try {
            bytes = loadClassFileData(file);
            if (bytes == null) {
                throw new ClassNotFoundException();
            }
            Class c = defineClass(name, bytes, 0, bytes.length);
            if (c == null) {
                throw new ClassFormatError();
            }
            resolveClass(c);
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] loadClassFileData(String name) throws IOException {
        try {
            Path path = Paths.get(Configuration.PATH_TO_FOLDER.replace(Configuration.NAME_OF_FOLDER, "") + name);
            return Files.readAllBytes(path);
        } catch (Exception e) {
            return null;
        }
    }

}