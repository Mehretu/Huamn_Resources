package com.act.HR_management.Utility;

import com.act.HR_management.DTO.AttendanceReportDto;
import com.act.HR_management.DTO.PayrolImportDto;
import com.act.HR_management.Models.Enums.AttendanceStatus;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelImport {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static <T> List<T> processExcelFile(MultipartFile file, Class<T> dtoClass) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            List<T> dtoList = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();


                if (row.getRowNum() == 0) {
                    continue;
                }

                T dto = createDtoInstance(dtoClass);

                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();

                    setDtoField(dto, columnIndex, getCellValueAsString(cell));
                }

                dtoList.add(dto);
            }

            return dtoList;
        }
    }

    private static <T> T createDtoInstance(Class<T> dtoClass) {
        try {
            return dtoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of the DTO class", e);
        }
    }

    private static <T> void setDtoField(T dto, int columnIndex, String cellValue) {


        if (dto instanceof AttendanceReportDto) {
            AttendanceReportDto attendanceDto = (AttendanceReportDto) dto;
            switch (columnIndex) {
                case 0:
                    attendanceDto.setDate(getLocalDate(cellValue));
                    break;
                case 1:
                    attendanceDto.setInTime(getLocalDateTime(cellValue));
                    break;
                case 2:
                    attendanceDto.setOutTime(getLocalDateTime(cellValue));
                    break;
                case 3:
                    attendanceDto.setEmployeeName(cellValue);
                    break;
                case 4:
                    attendanceDto.setDepartmentName(cellValue);
                    break;
                case 5:
                    AttendanceStatus status = convertToAttendanceStatus(cellValue);
                    attendanceDto.setAttendanceStatus(status);
                    break;
            }
        } else if (dto instanceof PayrolImportDto) {
           PayrolImportDto importDto = (PayrolImportDto) dto;
           switch (columnIndex) {
               case 0:
                   importDto.setEmployeeId(cellValue);
                   break;
               case 1:
                   importDto.setStartDate(getLocalDate(cellValue));
                   break;
               case 2:
                   importDto.setEndDate(getLocalDate(cellValue));
                   break;
               case 3:
                   importDto.setFirstName(cellValue);
                   break;

               case 4:
                   importDto.setLastName(cellValue);
                   break;
               case 5:
                   importDto.setBaseSalary(Double.parseDouble(cellValue));
                   break;
               case 6:
                   importDto.setGrossSalary(Double.parseDouble(cellValue));
                   break;
               case 7:
                   importDto.setIncomeTax(Double.parseDouble(cellValue));
                   break;
               case 8:
                   importDto.setPension(Double.parseDouble(cellValue));
                   break;
               case 9:
                   importDto.setEmployerPension(Double.parseDouble(cellValue));
                   break;
               case 10:
                   importDto.setBenefits(Double.parseDouble(cellValue));
                   break;
               case 11:
                   importDto.setTotalDeduction(Double.parseDouble(cellValue));
               case 12:
                   importDto.setNetPayment(Double.parseDouble(cellValue));
                   break;
           }
        }
    }

    private static LocalDate getLocalDate(String cellValue) {

        try {

            return LocalDate.parse(cellValue, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            System.err.println("Error parsing LocalDate: " + cellValue);
            return null;
        }
    }

    private static LocalDateTime getLocalDateTime(String cellValue) {
        try {
            return LocalDateTime.parse(cellValue, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            System.err.println("Error parsing LocalDateTime: " + cellValue);
            return null;
        }
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    public static AttendanceStatus convertToAttendanceStatus(String cellValue){
        for (AttendanceStatus status: AttendanceStatus.values()){
            if (status.toString().equalsIgnoreCase(cellValue)){
                return status;
            }
        }
        return AttendanceStatus.ABSENT;
    }
}
