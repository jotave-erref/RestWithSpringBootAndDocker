package br.com.jotave_erref.RestWithSpringBoot.domain.person;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
@Entity
@Table(name = "person")
public class Person extends RepresentationModel<Person> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    @Column
    private String gender;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public Person(){}

    public Person(DetailPersonData data) {
        this.firstName = data.firstName();
        this.lastName = data.lastName();
        this.address = data.address();
        this.gender = data.gender();
        this.enabled = data.enabled();
    }

    public Person(String firstName, String lastName, String address, String gender, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean getEnabled() {
        return enabled;
    }

    public void update(UpdatePersonData data) {
        if(data.address() != null){
            this.address = data.address();
        }

        if(data.gender() != null){
            this.gender = data.gender();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return id == person.id && enabled == person.enabled && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(address, person.address) && Objects.equals(gender, person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, address, gender, enabled);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
