package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Fan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FanDTO implements Serializable {

    private Long id;

    private String nomi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomi() {
        return nomi;
    }

    public void setNomi(String nomi) {
        this.nomi = nomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FanDTO)) {
            return false;
        }

        FanDTO fanDTO = (FanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FanDTO{" +
            "id=" + getId() +
            ", nomi='" + getNomi() + "'" +
            "}";
    }
}
