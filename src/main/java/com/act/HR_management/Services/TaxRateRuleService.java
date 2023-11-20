package com.act.HR_management.Services;

import com.act.HR_management.DTO.TaxRateRuleDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.TaxRateRule;
import com.act.HR_management.Repos.TaxRateRuleRepository;
import com.act.HR_management.Utility.TaxRateRuleEnum;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TaxRateRuleService {

    private final TaxRateRuleRepository taxRateRuleRepository;

    public TaxRateRuleService(TaxRateRuleRepository taxRateRuleRepository) {
        this.taxRateRuleRepository = taxRateRuleRepository;
    }

    public TaxRateRule create(TaxRateRuleDto taxRateRuleDto){
        TaxRateRule taxRateRule = taxRateRuleDto.toEntity();
        return taxRateRuleRepository.save(taxRateRule);
    }
    public List<TaxRateRule> getAllTaxRateRules(){
        List<TaxRateRule> taxRateRuleList = taxRateRuleRepository.findAll();
        return taxRateRuleList;
    }
    public TaxRateRule update(Long id,TaxRateRule updated){
        TaxRateRule existing = taxRateRuleRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("TaxRateRule not found with this id: "+id));
        existing.setIncomeFrom(updated.getIncomeFrom());
        existing.setIncomeTo(updated.getIncomeTo());
        existing.setTaxRatePercentage(updated.getTaxRatePercentage());
        return taxRateRuleRepository.save(existing);

    }

    public void delete(Long id){
        TaxRateRule taxRateRule = taxRateRuleRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("TaxRateRule not foun with this id: "+id));
        taxRateRuleRepository.delete(taxRateRule);
    }


    public void onInit() {
        Arrays.stream(TaxRateRuleEnum.values()).forEach(taxRateRuleEnum -> {
            TaxRateRuleDto taxRateRuleDto = new TaxRateRuleDto();
            taxRateRuleDto.setIncomeFrom(taxRateRuleEnum.getFrom());
            taxRateRuleDto.setIncomeTo(taxRateRuleEnum.getTo());
            taxRateRuleDto.setTaxRatePercentage(taxRateRuleEnum.getPercentage());
            create(taxRateRuleDto);
        });
    }

}
