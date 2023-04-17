package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Language;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Javob} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JavobDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer kursi;

    private String guruh;

    private String javob;

    private Language language;

    private UstozDTO ustoz;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getKursi() {
        return kursi;
    }

    public void setKursi(Integer kursi) {
        this.kursi = kursi;
    }

    public String getGuruh() {
        return guruh;
    }

    public void setGuruh(String guruh) {
        this.guruh = guruh;
    }

    public String getJavob() {
        return javob;
    }

    public void setJavob(String javob) {
        this.javob = javob;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public UstozDTO getUstoz() {
        return ustoz;
    }

    public void setUstoz(UstozDTO ustoz) {
        this.ustoz = ustoz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JavobDTO)) {
            return false;
        }

        JavobDTO javobDTO = (JavobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, javobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JavobDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", kursi=" + getKursi() +
            ", guruh='" + getGuruh() + "'" +
            ", javob='" + getJavob() + "'" +
            ", language='" + getLanguage() + "'" +
            ", ustoz=" + getUstoz() +
            "}";
    }
}
