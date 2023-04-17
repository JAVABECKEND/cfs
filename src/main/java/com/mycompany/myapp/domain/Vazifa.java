package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Vazifa.
 */
@Entity
@Table(name = "vazifa")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vazifa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = { "togaraks" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Ustoz ustoz;

    @OneToOne
    @JoinColumn(unique = true)
    private Fan fan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vazifa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Vazifa title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Vazifa description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ustoz getUstoz() {
        return this.ustoz;
    }

    public void setUstoz(Ustoz ustoz) {
        this.ustoz = ustoz;
    }

    public Vazifa ustoz(Ustoz ustoz) {
        this.setUstoz(ustoz);
        return this;
    }

    public Fan getFan() {
        return this.fan;
    }

    public void setFan(Fan fan) {
        this.fan = fan;
    }

    public Vazifa fan(Fan fan) {
        this.setFan(fan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vazifa)) {
            return false;
        }
        return id != null && id.equals(((Vazifa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vazifa{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
