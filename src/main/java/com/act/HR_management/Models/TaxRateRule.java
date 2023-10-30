package com.act.HR_management.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TaxRateRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "incomeTaxRuleId")
    private IncomeTaxRule incomeTaxRule;

    private double incomeFrom;

    private double incomeTo;

    private double taxRatePercentage;
}
