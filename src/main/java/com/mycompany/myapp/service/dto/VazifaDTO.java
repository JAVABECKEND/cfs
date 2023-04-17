package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Vazifa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VazifaDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private UstozDTO ustoz;

    private FanDTO fan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UstozDTO getUstoz() {
        return ustoz;
    }

    public void setUstoz(UstozDTO ustoz) {
        this.ustoz = ustoz;
    }

    public FanDTO getFan() {
        return fan;
    }

    public void setFan(FanDTO fan) {
        this.fan = fan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VazifaDTO)) {
            return false;
        }

        VazifaDTO vazifaDTO = (VazifaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vazifaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VazifaDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", ustoz=" + getUstoz() +
            ", fan=" + getFan() +
            "}";
    }
}
