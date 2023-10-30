package com.act.HR_management.Utility;
import com.act.HR_management.DTO.AttendanceReportDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class Export {
    public static byte[] exportMonthlyAttendanceReportAsExcel(List<AttendanceReportDto> reportData) throws IOException {
        if (CollectionUtils.isEmpty(reportData)) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Monthly Attendance Report");


            Row headerRow = sheet.createRow(0);
            String[] headers = {"Record Date", "In Time", "Out Time", "Employee Name", "Department Name","Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }


            int rowNum = 1;
            for (AttendanceReportDto dto : reportData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getDate() != null ? dto.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")): "");
                row.createCell(1).setCellValue(dto.getInTime() != null ? dto.getInTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "");
                row.createCell(2).setCellValue(dto.getOutTime() != null ? dto.getOutTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")): "");
                row.createCell(3).setCellValue(dto.getEmployeeName());
                row.createCell(4).setCellValue(dto.getDepartmentName());
                row.createCell(5).setCellValue(dto.getAttendanceStatus().toString());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    public static void exportToPdf(List<AttendanceReportDto> reportData, OutputStream outputStream){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(6);
            table.addCell("Employee Name");
            table.addCell("Department");
            table.addCell("Date");
            table.addCell("In Time");
            table.addCell("Out Time");
            table.addCell("Status");

            for (AttendanceReportDto report : reportData){
                table.addCell(report.getEmployeeName());
                table.addCell(report.getDepartmentName());
                table.addCell(report.getDate() != null ? report.getDate().toString() : "");
                table.addCell(report.getInTime() != null ? report.getInTime().toString() : "");
                table.addCell(report.getOutTime() != null ? report.getOutTime().toString(): "");
                table.addCell(report.getAttendanceStatus().toString());
            }
            document.add(table);
            document.close();
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }
}