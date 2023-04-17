package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Language;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Javob.
 */
@Entity
@Table(name = "javob")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Javob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "kursi")
    private Integer kursi;

    @Column(name = "guruh")
    private String guruh;

    @Column(name = "javob")
    private String javob;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @JsonIgnoreProperties(value = { "togaraks" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Ustoz ustoz;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Javob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Javob firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Javob lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getKursi() {
        return this.kursi;
    }

    public Javob kursi(Integer kursi) {
        this.setKursi(kursi);
        return this;
    }

    public void setKursi(Integer kursi) {
        this.kursi = kursi;
    }

    public String getGuruh() {
        return this.guruh;
    }

    public Javob guruh(String guruh) {
        this.setGuruh(guruh);
        return this;
    }

    public void setGuruh(String guruh) {
        this.guruh = guruh;
    }

    public String getJavob() {
        return this.javob;
    }

    public Javob javob(String javob) {
        this.setJavob(javob);
        return this;
    }

    public void setJavob(String javob) {
        this.javob = javob;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Javob language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Ustoz getUstoz() {
        return this.ustoz;
    }

    public void setUstoz(Ustoz ustoz) {
        this.ustoz = ustoz;
    }

    public Javob ustoz(Ustoz ustoz) {
        this.setUstoz(ustoz);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Javob)) {
            return false;
        }
        return id != null && id.equals(((Javob) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Javob{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", kursi=" + getKursi() +
            ", guruh='" + getGuruh() + "'" +
            ", javob='" + getJavob() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
