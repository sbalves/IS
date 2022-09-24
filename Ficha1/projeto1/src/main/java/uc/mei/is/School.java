package uc.mei.is;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;

@XmlRootElement(name = "School")
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})

@XmlAccessorType(XmlAccessType.FIELD)
public class School {
    //@XmlAttribute

    @XmlElement(name = "Professors")
    ArrayList<Professor> listProfessors;
    public School(){}
    public School(ArrayList<Professor> listProfessors) {
        this.listProfessors = listProfessors;
    }

}
