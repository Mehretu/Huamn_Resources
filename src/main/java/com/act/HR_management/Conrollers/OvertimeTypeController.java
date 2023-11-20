package com.act.HR_management.Conrollers;

import com.act.HR_management.Models.OvertimeType;
import com.act.HR_management.Services.OvertimeTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/overtTimeType")
public class OvertimeTypeController {

    private final OvertimeTypeService overtimeTypeService;

    public OvertimeTypeController(OvertimeTypeService overtimeTypeService){
        this.overtimeTypeService = overtimeTypeService;
    }


    @PostMapping("/create")
    public ResponseEntity<OvertimeType> create(@RequestBody OvertimeType overtimeType){
        OvertimeType overtimeType1 = overtimeTypeService.create(overtimeType);
        return new ResponseEntity<>(overtimeType1, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OvertimeType>> getAll(){
        List<OvertimeType> overtimeTypeList = overtimeTypeService.getAll();
        return new ResponseEntity<>(overtimeTypeList,HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<OvertimeType> update(@RequestBody OvertimeType overtimeType){
        OvertimeType overtimeType1 = overtimeTypeService.update(overtimeType);
        return new ResponseEntity<>(overtimeType1,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        overtimeTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
