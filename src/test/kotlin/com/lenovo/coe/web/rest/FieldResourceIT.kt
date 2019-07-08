package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Field
import com.lenovo.coe.repository.FieldRepository
import com.lenovo.coe.service.FieldService
import com.lenovo.coe.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator


import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
 * Test class for the FieldResource REST controller.
 *
 * @see FieldResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class FieldResourceIT {

    @Autowired
    private lateinit var fieldRepository: FieldRepository

    @Autowired
    private lateinit var fieldService: FieldService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restFieldMockMvc: MockMvc

    private lateinit var field: Field

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val fieldResource = FieldResource(fieldService)
        this.restFieldMockMvc = MockMvcBuilders.standaloneSetup(fieldResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        fieldRepository.deleteAll()
        field = createEntity()
    }

    @Test
    fun createField() {
        val databaseSizeBeforeCreate = fieldRepository.findAll().size

        // Create the Field
        restFieldMockMvc.perform(
            post("/api/fields")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(field))
        ).andExpect(status().isCreated)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1)
        val testField = fieldList[fieldList.size - 1]
        assertThat(testField.name).isEqualTo(DEFAULT_NAME)
        assertThat(testField.data).isEqualTo(DEFAULT_DATA)
    }

    @Test
    fun createFieldWithExistingId() {
        val databaseSizeBeforeCreate = fieldRepository.findAll().size

        // Create the Field with an existing ID
        field.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldMockMvc.perform(
            post("/api/fields")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(field))
        ).andExpect(status().isBadRequest)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = fieldRepository.findAll().size
        // set the field null
        field.name = null

        // Create the Field, which fails.

        restFieldMockMvc.perform(
            post("/api/fields")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(field))
        ).andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllFields() {
        // Initialize the database
        fieldRepository.save(field)

        // Get all the fieldList
        restFieldMockMvc.perform(get("/api/fields?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
    }
    
    @Test
    fun getField() {
        // Initialize the database
        fieldRepository.save(field)

        val id = field.id
        assertNotNull(id)

        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
    }

    @Test
    fun getNonExistingField() {
        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateField() {
        // Initialize the database
        fieldService.save(field)

        val databaseSizeBeforeUpdate = fieldRepository.findAll().size

        // Update the field
        val id = field.id
        assertNotNull(id)
        val updatedField = fieldRepository.findById(id).get()
        updatedField.name = UPDATED_NAME
        updatedField.data = UPDATED_DATA

        restFieldMockMvc.perform(
            put("/api/fields")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedField))
        ).andExpect(status().isOk)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
        val testField = fieldList[fieldList.size - 1]
        assertThat(testField.name).isEqualTo(UPDATED_NAME)
        assertThat(testField.data).isEqualTo(UPDATED_DATA)
    }

    @Test
    fun updateNonExistingField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size

        // Create the Field

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc.perform(
            put("/api/fields")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(field))
        ).andExpect(status().isBadRequest)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteField() {
        // Initialize the database
        fieldService.save(field)

        val databaseSizeBeforeDelete = fieldRepository.findAll().size

        val id = field.id
        assertNotNull(id)

        // Delete the field
        restFieldMockMvc.perform(
            delete("/api/fields/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Field::class)
        val field1 = Field()
        field1.id = "id1"
        val field2 = Field()
        field2.id = field1.id
        assertThat(field1).isEqualTo(field2)
        field2.id = "id2"
        assertThat(field1).isNotEqualTo(field2)
        field1.id = null
        assertThat(field1).isNotEqualTo(field2)
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_DATA: String = "AAAAAAAAAA"
        private const val UPDATED_DATA = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Field {
            val field = Field(
                name = DEFAULT_NAME,
                data = DEFAULT_DATA
            )

            return field
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Field {
            val field = Field(
                name = UPDATED_NAME,
                data = UPDATED_DATA
            )

            return field
        }
    }
}
