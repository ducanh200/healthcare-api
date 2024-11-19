package com.example.healthcare_api.entities;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "result_medicine")
public class ResultMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private Result result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;

    private String description;
}