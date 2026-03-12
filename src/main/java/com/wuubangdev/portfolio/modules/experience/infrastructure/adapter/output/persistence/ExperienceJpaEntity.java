package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "experiences")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExperienceJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String role;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String logoUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private Integer displayOrder;
}
