package br.com.jotave_erref.RestWithSpringBoot.domain;

import br.com.jotave_erref.RestWithSpringBoot.domain.dto.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.UpdatePersonData;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
@Entity
@Table(name = "person")
public class Person extends RepresentationModel<Person> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    @Column
    private String gender;

    public Person(){}

    public Person(DetailPersonData data) {
        this.firstName = data.firstName();
        this.lastName = data.lastName();
        this.address = data.address();
        this.gender = data.gender();

    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(address, person.address) && Objects.equals(gender, person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, address, gender);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public void update(UpdatePersonData data) {
        if(data.address() != null){
            this.address = data.address();
        }

        if(data.gender() != null){
            this.gender = data.gender();
        }
    }
}
