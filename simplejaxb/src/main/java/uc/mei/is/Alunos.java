package uc.mei.is;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;

@XmlRootElement(name = "class")
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})

@XmlAccessorType(XmlAccessType.FIELD)
public class Alunos {
    //@XmlAttribute

    @XmlElement(name = "student")
    ArrayList<Aluno> listaalunos;
    public Alunos(){}
    public Alunos(ArrayList<Aluno> listaalunos) {
        this.listaalunos = listaalunos;
    }

    public void setListaalunos(ArrayList<Aluno> listaalunos) {
        this.listaalunos = listaalunos;
    }


}
