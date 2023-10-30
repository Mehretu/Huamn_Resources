package com.act.HR_management.DTO;

import com.act.HR_management.DTO.AttendanceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceUpdateResult {
    private List<AttendanceDto> upadatedAttendanceList;

}
