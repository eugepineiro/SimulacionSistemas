package ar.edu.itba.ss;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonWriter {

    private static String PATH = "TP2/src/main/resources/postprocessing";

    private final ObjectMapper mapper;
    private final String filename;
    private Object object;

    public static void setPath(String path) {
        JsonWriter.PATH = path;
    }

    public JsonWriter(String filename) {
        this.mapper = new ObjectMapper();
        this.filename = filename;
    }

    public JsonWriter withObj(Object obj) {
        this.object = obj;

        return this;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void write() throws IOException {
        File dir = new File(PATH);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        File path = new File(String.format("%s/%s.json", PATH, this.filename));

        //Object to JSON in file
        this.mapper.writeValue(path, this.object);
    }

}
