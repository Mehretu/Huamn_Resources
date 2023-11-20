package com.act.HR_management.Repos;

import com.act.HR_management.Models.Attendance;
import com.act.HR_management.Models.Enums.AttendanceStatus;
import com.act.HR_management.Models.Department;
import com.act.HR_management.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
   List<Attendance> findByEmployee_Department(Department department);


    List<Attendance> findByEmployee_FirstNameOrEmployee_LastNameAndRecordDateBetween(String firstName,String lastName,LocalDate startDate,LocalDate endDate);

    List<Attendance> findAllByRecordDateBetween(LocalDate startDate, LocalDate endDate);

    List<Attendance> findAllByRecordDateBetweenAndAttendanceStatus(LocalDate startDate, LocalDate endDate, AttendanceStatus attendanceStatus);

    List<Attendance> findAllByAttendanceStatus(AttendanceStatus attendanceStatus);

 List<Attendance> findAllByEmployeeAndRecordDate(Employee employee, LocalDate targetDate);
}
