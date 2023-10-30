package com.act.HR_management.Services;

import com.act.HR_management.Models.Holiday;
import com.act.HR_management.Repos.HolidayRepository;
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

    public Holiday createHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    public void deleteHoliday(Long holidayId) {
        holidayRepository.deleteById(holidayId);
    }

    public List<Holiday> getHolidaysInRange(LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findByDateBetween(startDate, endDate);
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
