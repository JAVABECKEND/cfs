package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Ustoz.
 */
@Entity
@Table(name = "ustoz")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ustoz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ism")
    private String ism;

    @Column(name = "familya")
    private String familya;

    @ManyToMany(mappedBy = "ustozs")
    @JsonIgnoreProperties(value = { "ustozs" }, allowSetters = true)
    private Set<Togarak> togaraks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ustoz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsm() {
        return this.ism;
    }

    public Ustoz ism(String ism) {
        this.setIsm(ism);
        return this;
    }

    public void setIsm(String ism) {
        this.ism = ism;
    }

    public String getFamilya() {
        return this.familya;
    }

    public Ustoz familya(String familya) {
        this.setFamilya(familya);
        return this;
    }

    public void setFamilya(String familya) {
        this.familya = familya;
    }

    public Set<Togarak> getTogaraks() {
        return this.togaraks;
    }

    public void setTogaraks(Set<Togarak> togaraks) {
        if (this.togaraks != null) {
            this.togaraks.forEach(i -> i.removeUstoz(this));
        }
        if (togaraks != null) {
            togaraks.forEach(i -> i.addUstoz(this));
        }
        this.togaraks = togaraks;
    }

    public Ustoz togaraks(Set<Togarak> togaraks) {
        this.setTogaraks(togaraks);
        return this;
    }

    public Ustoz addTogarak(Togarak togarak) {
        this.togaraks.add(togarak);
        togarak.getUstozs().add(this);
        return this;
    }

    public Ustoz removeTogarak(Togarak togarak) {
        this.togaraks.remove(togarak);
        togarak.getUstozs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ustoz)) {
            return false;
        }
        return id != null && id.equals(((Ustoz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ustoz{" +
            "id=" + getId() +
            ", ism='" + getIsm() + "'" +
            ", familya='" + getFamilya() + "'" +
            "}";
    }
}
