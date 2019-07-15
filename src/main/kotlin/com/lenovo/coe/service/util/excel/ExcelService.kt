package com.lenovo.coe.service.util.excel

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.ArrayList

@Service
class ExcelService {
    private val location: Path

    init {
        this.location = Paths.get(STORAGE_PATH)
    }

    @Throws(IOException::class)
    fun createExcel(data: List<Map<String, Any?>>, fileName: String): File {
        val file = location.resolve(fileName + FILE_TERMINATION).toFile()
        file.createNewFile()
        if (file.exists()) {
            val outputStream = FileOutputStream(file)
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet()
            val columnCount = data[0].size
            createHeaderColumns(sheet, data[0])
            for (item in data) {
                addRowToSheet(sheet, item)
            }
            for (i in 0 until columnCount) {
                sheet.autoSizeColumn(i)
            }

            workbook.write(outputStream)
            outputStream.close()
            workbook.close()
        }

        return file
    }

    @Throws(IOException::class)
    fun readXlsxFile(fileIS: InputStream): List<List<Any>> {
        val items = ArrayList<List<Any>>()
        val workbook = XSSFWorkbook(fileIS)
        val sheet = workbook.getSheetAt(0)
        var row: XSSFRow
        var cell: XSSFCell
        val rows = sheet.rowIterator()
        rows.next() as XSSFRow
        while (rows.hasNext()) {
            row = rows.next() as XSSFRow
            val cells = row.cellIterator()
            val item = ArrayList<Any>()
            while (cells.hasNext()) {
                cell = cells.next() as XSSFCell
                if (cell.cellType == CellType.STRING) {
                    item.add(cell.stringCellValue)
                } else if (cell.cellType == CellType.NUMERIC) {
                    item.add(java.lang.Double.valueOf(cell.numericCellValue).toLong())
                }
            }
            items.add(item)
        }

        return items
    }

    private fun createHeaderColumns(sheet: XSSFSheet, rowData: Map<String, Any?>) {
        var i = 0
        val header = sheet.createRow(i)
        for ((key) in rowData) {
            val cell = header.createCell(i)
            cell.setCellValue(key)
            i++
        }
    }

    private fun addRowToSheet(sheet: XSSFSheet, rowData: Map<String, Any?>) {
        var i = 0
        val newRow = sheet.createRow(sheet.lastRowNum + 1)
        for ((_, value) in rowData) {
            val cell = newRow.createCell(i)
            if (value != null) {
                cell.setCellValue(value.toString())
            } else {
                cell.setCellValue("")
            }
            i++
        }
    }

    companion object {
        val STORAGE_PATH = "storage" + File.separator + "attachments" + File.separator + "excel"
        val FILE_TERMINATION = ".xlsx"
    }
}
