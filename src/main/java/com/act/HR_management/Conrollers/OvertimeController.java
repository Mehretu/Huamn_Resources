package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.OvertimeDto;
import com.act.HR_management.Models.Overtime;
import com.act.HR_management.Services.OvertimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/overtime")
public class OvertimeController {
    private final OvertimeService overtimeService;

    public OvertimeController(OvertimeService overtimeService) {
        this.overtimeService = overtimeService;
    }
    @PostMapping("/create")
    public ResponseEntity<Overtime> create (@RequestBody OvertimeDto overtimeDto){
        Overtime overtime = overtimeService.create(overtimeDto);
        return new ResponseEntity<>(overtime, HttpStatus.CREATED);
    }
    @GetMapping("/getOvertimeForEmployee/{employeeId}")
    public ResponseEntity<List<Overtime>> getOvertimeForEmployee(@PathVariable Long employeeId,
                                                                 @RequestParam int year,
                                                                 @RequestParam int month){
        List<Overtime> overtimes = overtimeService.getOvertimeforEmployee(employeeId,year,month);
        return new ResponseEntity<>(overtimes,HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Overtime>> getAll(){
        List<Overtime> overtimes = overtimeService.getAll();
        return new ResponseEntity<>(overtimes,HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Overtime> update(@PathVariable Long id, @RequestBody OvertimeDto overtimeDto){
        Overtime overtime = overtimeService.update(id, overtimeDto);
        return new ResponseEntity<>(overtime,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        overtimeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
