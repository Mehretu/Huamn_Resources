package com.act.HR_management.Services;

import com.act.HR_management.DTO.HolidayDto;
import com.act.HR_management.Models.Holiday;
import com.act.HR_management.Repos.HolidayRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public Holiday createHoliday(HolidayDto holidayDto) {
        Holiday holiday = holidayDto.toEntity();

        return holidayRepository.save(holiday);
    }

    public Holiday update(Long id, HolidayDto holidayDto){
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("There is no holiday found with this id: "+id));
        holiday.setName(holidayDto.getName());
        holiday.setDate(holidayDto.getDate());
        holiday.setDescription(holidayDto.getDescription());
        return holiday;
    }

    public void deleteHoliday(Long holidayId) {
        holidayRepository.deleteById(holidayId);
    }

    public List<Holiday> getHolidaysInRange(LocalDate startDate, LocalDate endDate) {
        List<Holiday> holidays = holidayRepository.findByDateBetween(startDate, endDate);
        return holidays;
    }

    public List<Holiday> getHolidaysByYear(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return getHolidaysInRange(startOfYear, endOfYear);
    }


    public List<LocalDate> getAllHolidayDates() {
        List<Holiday> holidays = holidayRepository.findAll();
        List<LocalDate> holidayDates = new ArrayList<>();

        for (Holiday holiday : holidays) {
            holidayDates.add(holiday.getDate());
        }

        return holidayDates;
    }
}
