package uc.mei.is;

import jakarta.xml.bind.annotation.*;

@XmlRootElement
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Aluno {
    @XmlValue
    int age;
    @XmlElement(name = "n")
    String nome;
    public Aluno(){}
    public Aluno(int age, String nome) {
        this.age = age;
        this.nome = nome;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
