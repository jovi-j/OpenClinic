package com.jovij.OpenClinic.Model.Generics;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onPrePersist(){
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate(){
        this.setUpdatedAt(LocalDateTime.now());
    }


}
