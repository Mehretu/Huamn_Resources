package com.act.HR_management.DTO;

import com.act.HR_management.Models.TaxRateRule;
import lombok.Data;

@Data
public class TaxRateRuleDto {
    private double incomeFrom;

    private double incomeTo;

    private double taxRatePercentage;


    public TaxRateRule toEntity(){
        TaxRateRule taxRateRule = new TaxRateRule();
        taxRateRule.setIncomeFrom(this.getIncomeFrom());
        taxRateRule.setIncomeTo(this.getIncomeTo());
        taxRateRule.setTaxRatePercentage(this.getTaxRatePercentage());
        return taxRateRule;
    }
}
