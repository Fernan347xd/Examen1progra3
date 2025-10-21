package GestionProyectos.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XmlPersister<T> {
    private final String path;
    private final Class<T> type;

    public XmlPersister(String path, Class<T> type) {
        this.path = path;
        this.type = type;
    }

    public T load() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(type);
        FileInputStream is = new FileInputStream(path);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        T result = (T) unmarshaller.unmarshal(is);
        is.close();
        return result;
    }

    public void store(T data) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(type);
        FileOutputStream os = new FileOutputStream(path);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(data, os);
        os.flush();
        os.close();
    }
}
