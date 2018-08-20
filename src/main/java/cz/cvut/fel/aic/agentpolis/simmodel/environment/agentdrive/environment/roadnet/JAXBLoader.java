package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.generated.NetType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.generated.ObjectFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util.Utils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class JAXBLoader {

    private final static Logger logger = Logger.getLogger(JAXBLoader.class);

    public static void main(String[] args) throws FileNotFoundException {
        JAXBLoader loader = new JAXBLoader();

        File xmlFile = Utils.getResourceFile("nets/simple/simple.net.xml");
        File xsdFile = Utils.getResourceFile("SUMO_files_schemes/net_file.xsd");

        if (validateXMLSchema(xmlFile, xsdFile)) {
            System.out.println("Validation success");
        }
        loader.loadNetType(xmlFile, xsdFile);

    }

    public NetType loadNetType(File xmlFile, File xsdFile) {

        JAXBContext jaxbContext;
        NetType net = null;
        try {
            jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

            Unmarshaller jaxbUnmarshaller = null;

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema scheme = sf.newSchema(xsdFile);

            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbUnmarshaller.setSchema(scheme);

            JAXBElement<NetType> JAXBNet = (JAXBElement<NetType>) jaxbUnmarshaller.unmarshal(xmlFile);
            net = JAXBNet.getValue();


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return net;
    }


    public static boolean validateXMLSchema(File xsdFile, File xmlFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));

        } catch (IOException e) {
            logger.warn("Exception: " + e.getMessage());
            return false;
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return true;
    }

}
