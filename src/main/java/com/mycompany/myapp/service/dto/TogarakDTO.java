package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Nomi;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Togarak} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TogarakDTO implements Serializable {

    private Long id;

    private Nomi togarakNomi;

    private Set<UstozDTO> ustozs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Nomi getTogarakNomi() {
        return togarakNomi;
    }

    public void setTogarakNomi(Nomi togarakNomi) {
        this.togarakNomi = togarakNomi;
    }

    public Set<UstozDTO> getUstozs() {
        return ustozs;
    }

    public void setUstozs(Set<UstozDTO> ustozs) {
        this.ustozs = ustozs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TogarakDTO)) {
            return false;
        }

        TogarakDTO togarakDTO = (TogarakDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, togarakDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TogarakDTO{" +
            "id=" + getId() +
            ", togarakNomi='" + getTogarakNomi() + "'" +
            ", ustozs=" + getUstozs() +
            "}";
    }
}
