package uc.mei.is;

//xml protocol buffer  um é um formato de texto e outro é binário

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;

public class App {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;
        try {

            // Normal JAXB RI
            //jaxbContext = JAXBContext.newInstance(Fruit.class);

            // EclipseLink MOXy needs jaxb.properties at the same package with Fruit.class
            // Alternative, I prefer define this via eclipse JAXBContextFactory manually.
            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[]{Aluno.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Aluno o = new Aluno(2019403050, "Sofia");

            // output to a xml file
            jaxbMarshaller.marshal(o, new File("C:\\Users\\jpedr\\Desktop\\Mestrado\\1_ano\\IS\\simplejaxb\\aluno.xml"));

            // output to console
            // jaxbMarshaller.marshal(o, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}