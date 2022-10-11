package uc.mei.is;

//xml protocol buffer  um é um formato de texto e outro é binário

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import javax.xml.XMLConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import org.w3c.dom.Element;
public class App {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;
        try {

            // Normal JAXB RI
            //jaxbContext = JAXBContext.newInstance(Fruit.class);

            // EclipseLink MOXy needs jaxb.properties at the same package with Fruit.class
            // Alternative, I prefer define this via eclipse JAXBContextFactory manually.
            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[]{Alunos.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Aluno a = new Aluno(21, "Alberto", 201134441110L);
            Aluno p = new Aluno(22, "Patricia",201134441116L);
            Aluno l = new Aluno(21  , "Luis", 201134441210L);

            ArrayList<Aluno> lista = new ArrayList<Aluno>();
            lista.add(a);
            lista.add(p);
            lista.add(l);
            Alunos as = new Alunos(lista);
            // output to a xml file

            jaxbMarshaller.marshal(as, new File("C:\\Users\\jpedr\\Desktop\\Mestrado\\1_ano\\IS\\simplejaxb\\aluno.xml"));

            // output to console
            // jaxbMarshaller.marshal(o, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}