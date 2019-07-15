package com.lenovo.coe.service.util.excel

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class ExcelParser(inputStream: InputStream) {
    val workbook = WorkbookFactory.create(inputStream)

    fun getGroupedItemsFromSheet(sheetName: String, groupColumnName: String): HashMap<String, ArrayList<HashMap<String, String?>>> {
        val incorrectItems = hashMapOf<String, ArrayList<HashMap<String, String?>>>()
        val items = getItemsFromSheet(sheetName)
        items.forEach {
            if (incorrectItems.get(it.get(groupColumnName).toString()) == null) {
                incorrectItems.put(it.get(groupColumnName).toString(), arrayListOf(it))
            } else {
                incorrectItems.get(it.get(groupColumnName).toString())!!.add(it)
            }
        }

        return incorrectItems
    }


    private fun getHeaderItemsFromSheet(sheet: Sheet): HashMap<Int, String> {
        val rowKeys = hashMapOf<Int, String>()
        // we loop over the first row to get the column names
        sheet.rowIterator().next().forEachIndexed { index, cell ->
            when (cell.cellType) {
                CellType.STRING -> {
                    rowKeys.put(index, cell.stringCellValue)
                }
                else -> {
                }
            }
        }
        // after we finish getting the header we then delete it
        sheet.removeRow(sheet.first())
        return rowKeys
    }

    fun getItemsFromSheet(sheetName: String): ArrayList<HashMap<String, String?>> {
        val sheet = workbook.getSheet(sheetName)
        val rowKeys = getHeaderItemsFromSheet(sheet)

        // now we put all the values into a list so that we can process later
        return mapExcelRowsToHeader(sheet, rowKeys)
    }

    private fun mapExcelRowsToHeader(sheet: Sheet, rowKeys: HashMap<Int, String>): ArrayList<HashMap<String, String?>> {
        val incorrectItems = ArrayList<HashMap<String, String?>>()
        sheet.rowIterator().forEachRemaining {
            val item = hashMapOf<String, String?>()
            for (key in rowKeys) {
                val value: String? = it.getCell(key.key)?.toString()
                item[key.value] = value
            }
            incorrectItems.add(item)
        }
        return incorrectItems
    }

    fun getItemsFromSheet(sheet: Int): ArrayList<HashMap<String, String?>> {
        val sheetObject = workbook.getSheetAt(0)
        return mapExcelRowsToHeader(sheetObject, getHeaderItemsFromSheet(sheetObject))
    }
}
