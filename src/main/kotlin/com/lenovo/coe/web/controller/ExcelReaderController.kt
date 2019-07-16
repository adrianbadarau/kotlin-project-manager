package com.lenovo.coe.web.controller

import com.lenovo.coe.service.util.excel.ExcelParser
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * ExcelReaderControllerResource controller
 */
@RestController
@RequestMapping("/api/excel-reader")
class ExcelReaderController {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * GET readCustomTableTemplate
     */
    @PostMapping("/read-custom-table-template")
    fun readCustomTableTemplate(@RequestParam template: MultipartFile): ArrayList<HashMap<String, String?>> {
        val excelParser = ExcelParser(template.inputStream)
        val sheet = excelParser.getItemsFromSheet(0)
        return sheet
    }

}
