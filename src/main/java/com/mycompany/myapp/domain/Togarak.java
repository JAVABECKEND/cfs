package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Nomi;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Togarak.
 */
@Entity
@Table(name = "togarak")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Togarak implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "togarak_nomi")
    private Nomi togarakNomi;

    @ManyToMany
    @JoinTable(
        name = "rel_togarak__ustoz",
        joinColumns = @JoinColumn(name = "togarak_id"),
        inverseJoinColumns = @JoinColumn(name = "ustoz_id")
    )
    @JsonIgnoreProperties(value = { "togaraks" }, allowSetters = true)
    private Set<Ustoz> ustozs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Togarak id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Nomi getTogarakNomi() {
        return this.togarakNomi;
    }

    public Togarak togarakNomi(Nomi togarakNomi) {
        this.setTogarakNomi(togarakNomi);
        return this;
    }

    public void setTogarakNomi(Nomi togarakNomi) {
        this.togarakNomi = togarakNomi;
    }

    public Set<Ustoz> getUstozs() {
        return this.ustozs;
    }

    public void setUstozs(Set<Ustoz> ustozs) {
        this.ustozs = ustozs;
    }

    public Togarak ustozs(Set<Ustoz> ustozs) {
        this.setUstozs(ustozs);
        return this;
    }

    public Togarak addUstoz(Ustoz ustoz) {
        this.ustozs.add(ustoz);
        ustoz.getTogaraks().add(this);
        return this;
    }

    public Togarak removeUstoz(Ustoz ustoz) {
        this.ustozs.remove(ustoz);
        ustoz.getTogaraks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Togarak)) {
            return false;
        }
        return id != null && id.equals(((Togarak) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Togarak{" +
            "id=" + getId() +
            ", togarakNomi='" + getTogarakNomi() + "'" +
            "}";
    }
}
