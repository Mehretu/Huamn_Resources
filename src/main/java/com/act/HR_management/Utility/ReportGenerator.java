package com.act.HR_management.Utility;

import com.act.HR_management.DTO.AttendanceReportDto;
import com.act.HR_management.DTO.PayrollResponseDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
public class ReportGenerator {
    public static byte[] generateExcelReport(List<?> reportData, List<String> headers) throws IOException {
        if (CollectionUtils.isEmpty(reportData) || CollectionUtils.isEmpty(headers)) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Report");


            Row headerRow = sheet.createRow(0);
            CellStyle headerCellStyle = createHeaderCellStyle(workbook);

            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerCellStyle);
            }


            int rowNum = 1;
            for (Object rowData : reportData) {
                Row dataRow = sheet.createRow(rowNum++);

                if (rowData instanceof PayrollResponseDto) {
                    PayrollResponseDto payrollReport = (PayrollResponseDto) rowData;
                    dataRow.createCell(0).setCellValue(payrollReport.getEmployee().getEmployeeId());
                    dataRow.createCell(1).setCellValue(payrollReport.getStartDate() != null ? payrollReport.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")):"");
                    dataRow.createCell(2).setCellValue(payrollReport.getEndDate() != null ? payrollReport.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")):"");
                    dataRow.createCell(3).setCellValue(payrollReport.getEmployee().getFirstName()+" "+payrollReport.getEmployee().getLastName());
                    dataRow.createCell(4).setCellValue(payrollReport.getEmployee().getBaseSalary());
                    dataRow.createCell(5).setCellValue(payrollReport.getGrossSalary());
                    dataRow.createCell(6).setCellValue(payrollReport.getIncomeTax());
                    dataRow.createCell(7).setCellValue(payrollReport.getPension());
                    dataRow.createCell(8).setCellValue(payrollReport.getEmployerPension());
                    dataRow.createCell(9).setCellValue(payrollReport.getEmployee().getBenefits());
                    dataRow.createCell(10).setCellValue(payrollReport.getTotalDeduction());
                    dataRow.createCell(11).setCellValue(payrollReport.getNetPayment());


                }

                else if (rowData instanceof AttendanceReportDto) {
                    AttendanceReportDto dto = (AttendanceReportDto) rowData;
                    dataRow.createCell(0).setCellValue(dto.getDate() != null ? dto.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")): "");
                    dataRow.createCell(1).setCellValue(dto.getInTime() != null ? dto.getInTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "");
                    dataRow.createCell(2).setCellValue(dto.getOutTime() != null ? dto.getOutTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")): "");
                    dataRow.createCell(3).setCellValue(dto.getEmployeeName());
                    dataRow.createCell(4).setCellValue(dto.getDepartmentName());
                    dataRow.createCell(5).setCellValue(dto.getAttendanceStatus().toString());

                }
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    public static <T> void exportToPdf(List<T> reportData, List<String> headers, OutputStream outputStream) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfPTable table = new PdfPTable(headers.size());

            for (String header : headers) {
                table.addCell(header);
            }


            for (T report : reportData) {
                if (report instanceof AttendanceReportDto) {
                    AttendanceReportDto attendanceReport = (AttendanceReportDto) report;
                    table.addCell(attendanceReport.getEmployeeName());
                    table.addCell(attendanceReport.getDepartmentName());
                    table.addCell(attendanceReport.getDate() != null ? attendanceReport.getDate().toString() : "");
                    table.addCell(attendanceReport.getInTime() != null ? attendanceReport.getInTime().toString() : "");
                    table.addCell(attendanceReport.getOutTime() != null ? attendanceReport.getOutTime().toString() : "");
                    table.addCell(attendanceReport.getAttendanceStatus().toString());
                }
                else if (report instanceof  PayrollResponseDto){
                    PayrollResponseDto parollReport = (PayrollResponseDto) report;
                    table.addCell(parollReport.getEmployee().getEmployeeId());
                    table.addCell(parollReport.getStartDate() != null ? parollReport.getStartDate().toString(): "");
                    table.addCell(parollReport.getEndDate() != null ? parollReport.getEndDate().toString() : "");
                    table.addCell(parollReport.getEmployee().getFirstName()+" "+parollReport.getEmployee().getLastName());
                    table.addCell(String.valueOf(parollReport.getEmployee().getBaseSalary()));
                    table.addCell(String.valueOf(parollReport.getEmployee().getBenefits()));

                    table.addCell(String.valueOf(parollReport.getGrossSalary()));
                    table.addCell(String.valueOf(parollReport.getIncomeTax()));
                    table.addCell(String.valueOf(parollReport.getPension()));
                    table.addCell(String.valueOf(parollReport.getEmployerPension()));
                    table.addCell(String.valueOf(parollReport.getNetPayment()));
                    table.addCell(String.valueOf(parollReport.getTotalDeduction()));
                }
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
