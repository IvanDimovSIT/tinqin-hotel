package com.tinqinacademy.hotel.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "id_card_no", nullable = true)
    private String idCardNo;

    @Column(name = "id_card_issue_authority", nullable = true)
    private String idCardIssueAuthority;

    @Column(name = "id_card_issue_date", nullable = true)
    private LocalDate idCardIssueDate;

    @Column(name = "id_card_validity", nullable = true)
    private LocalDate idCardValidity;

    @Column(name = "phone_number", nullable = true, length = 16)
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
}
