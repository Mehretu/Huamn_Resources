package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.BonusDto;
import com.act.HR_management.Models.Bonus;
import com.act.HR_management.Services.BonusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Bonus")
@Tag(name = "Bonus")
public class BonusController {
    private final BonusService bonusService;

    public BonusController(BonusService bonusService) {
        this.bonusService = bonusService;
    }

    @PostMapping("/create")
    public ResponseEntity<Bonus> create(@RequestBody BonusDto bonusDto){
        Bonus bonus = bonusService.create(bonusDto);
        return new ResponseEntity<>(bonus, HttpStatus.CREATED);
    }

    @GetMapping("/ListBonusesForEmployee/{employeeId}")
    public ResponseEntity<List<Bonus>> ListBonusesOfEmployee(@PathVariable Long employeeId){
        List<Bonus> bonusList = bonusService.getTotalBonuseforEmployees(employeeId);
        return new ResponseEntity<>(bonusList,HttpStatus.OK);
    }

}
