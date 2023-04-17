package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Ustoz} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UstozDTO implements Serializable {

    private Long id;

    private String ism;

    private String familya;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsm() {
        return ism;
    }

    public void setIsm(String ism) {
        this.ism = ism;
    }

    public String getFamilya() {
        return familya;
    }

    public void setFamilya(String familya) {
        this.familya = familya;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UstozDTO)) {
            return false;
        }

        UstozDTO ustozDTO = (UstozDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ustozDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UstozDTO{" +
            "id=" + getId() +
            ", ism='" + getIsm() + "'" +
            ", familya='" + getFamilya() + "'" +
            "}";
    }
}
