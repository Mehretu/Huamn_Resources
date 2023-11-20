package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.TaxRateRuleDto;
import com.act.HR_management.Models.TaxRateRule;
import com.act.HR_management.Services.TaxRateRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taxRateRule")
public class TaxRateRuleController {
    private final TaxRateRuleService taxRateRuleService;

    public TaxRateRuleController(TaxRateRuleService taxRateRuleService) {
        this.taxRateRuleService = taxRateRuleService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaxRateRule> create(@RequestBody TaxRateRuleDto taxRateRuleDto){
        TaxRateRule taxRateRule = taxRateRuleService.create(taxRateRuleDto);
        return new ResponseEntity<>(taxRateRule, HttpStatus.CREATED);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<TaxRateRule>> getAll(){
        List<TaxRateRule> taxRateRules = taxRateRuleService.getAllTaxRateRules();
        return new ResponseEntity<>(taxRateRules,HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<TaxRateRule> update(@PathVariable Long id, @RequestBody TaxRateRule updated){
        TaxRateRule updatedTaxRateRule = taxRateRuleService.update(id,updated);
        return new ResponseEntity<>(updatedTaxRateRule,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taxRateRuleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
