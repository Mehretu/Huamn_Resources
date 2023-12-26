package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.HolidayDto;
import com.act.HR_management.Models.Holiday;
import com.act.HR_management.Services.HolidayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holiday")
public class HolidayController {
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping("/create")
    public ResponseEntity<Holiday> create(@RequestBody HolidayDto holidayDto){
        Holiday holiday = holidayService.createHoliday(holidayDto);
        return new ResponseEntity<>(holiday, HttpStatus.CREATED);
    }

    @GetMapping("/getAllinYear")
    public ResponseEntity<List<Holiday>> getHolidaysInYear(@RequestParam int year){
        List<Holiday> holidays = holidayService.getHolidaysByYear(year);
        return new ResponseEntity<>(holidays,HttpStatus.OK);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Holiday>> getAll(){
        List<Holiday> holidays = holidayService.getAllHolidays();
        return new ResponseEntity<>(holidays,HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Holiday> update(@PathVariable Long id, @RequestBody HolidayDto holidayDto){
        Holiday holiday = holidayService.update(id, holidayDto);
        return new ResponseEntity<>(holiday,HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        holidayService.deleteHoliday(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
