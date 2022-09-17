package uc.mei.is;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "aluno")
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Aluno {
    @XmlAttribute(name = "id")
    long id;
    @XmlElement(name = "name")
    String nome;
    @XmlElement(name = "age")
    int age;

    public Aluno(){}
    public Aluno(int age, String nome, long id) {
        this.age = age;
        this.nome = nome;
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAge(int numero){
        this.age = age;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
