package com.act.HR_management.Services;

import com.act.HR_management.Models.OvertimeType;
import com.act.HR_management.Repos.OvertimeRepository;
import com.act.HR_management.Repos.OvertimeTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvertimeTypeService {

    private final OvertimeTypeRepository overtimeTypeRepository;
    public OvertimeTypeService(OvertimeTypeRepository overtimeTypeRepository){
        this.overtimeTypeRepository = overtimeTypeRepository;
    }

    public OvertimeType create(OvertimeType overtimeType){
        OvertimeType overtimeType1 = new OvertimeType();
        overtimeType1.setName(overtimeType.getName());
        overtimeType1.setRate(overtimeType.getRate());
        return overtimeTypeRepository.save(overtimeType1);

    }
    public List<OvertimeType> getAll(){
        return overtimeTypeRepository.findAll();
    }
    public OvertimeType update(OvertimeType overtimeType){
        OvertimeType existing = overtimeTypeRepository.findById(overtimeType.getId()).
                orElseThrow(()-> new EntityNotFoundException("There is no overtime type found with this id: "+overtimeType.getId()));
        existing.setName(overtimeType.getName());
        existing.setRate(overtimeType.getRate());
        return overtimeTypeRepository.save(existing);
    }

    public void delete(Long id){
        OvertimeType overtimeType = overtimeTypeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("There is no Overtime type found with this id: "+id));
        overtimeTypeRepository.delete(overtimeType);
    }



}
