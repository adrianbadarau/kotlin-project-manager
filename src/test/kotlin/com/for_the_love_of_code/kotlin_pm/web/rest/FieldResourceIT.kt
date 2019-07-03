package com.for_the_love_of_code.kotlin_pm.web.rest

import com.for_the_love_of_code.kotlin_pm.KotlinPmApp
import com.for_the_love_of_code.kotlin_pm.domain.Field
import com.for_the_love_of_code.kotlin_pm.repository.FieldRepository
import com.for_the_love_of_code.kotlin_pm.repository.search.FieldSearchRepository
import com.for_the_love_of_code.kotlin_pm.web.rest.errors.ExceptionTranslator

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.util.Collections

import com.for_the_love_of_code.kotlin_pm.web.rest.TestUtil.createFormattingConversionService
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.hamcrest.Matchers.hasItem
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
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
@SpringBootTest(classes = [KotlinPmApp::class])
class FieldResourceIT {

    @Autowired
    private lateinit var fieldRepository: FieldRepository

    /**
     * This repository is mocked in the com.for_the_love_of_code.kotlin_pm.repository.search test package.
     *
     * @see com.for_the_love_of_code.kotlin_pm.repository.search.FieldSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockFieldSearchRepository: FieldSearchRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restFieldMockMvc: MockMvc

    private lateinit var field: Field

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val fieldResource = FieldResource(fieldRepository, mockFieldSearchRepository)
        this.restFieldMockMvc = MockMvcBuilders.standaloneSetup(fieldResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        field = createEntity(em)
    }

    @Test
    @Transactional
    fun createField() {
        val databaseSizeBeforeCreate = fieldRepository.findAll().size

        // Create the Field
        restFieldMockMvc.perform(
            post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field))
        ).andExpect(status().isCreated)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1)
        val testField = fieldList[fieldList.size - 1]
        assertThat(testField.name).isEqualTo(DEFAULT_NAME)
        assertThat(testField.data).isEqualTo(DEFAULT_DATA)

        // Validate the Field in Elasticsearch
    }

    @Test
    @Transactional
    fun createFieldWithExistingId() {
        val databaseSizeBeforeCreate = fieldRepository.findAll().size

        // Create the Field with an existing ID
        field.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldMockMvc.perform(
            post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field))
        ).andExpect(status().isBadRequest)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate)

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = fieldRepository.findAll().size
        // set the field null
        field.name = null

        // Create the Field, which fails.

        restFieldMockMvc.perform(
            post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field))
        ).andExpect(status().isBadRequest)

        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllFields() {
        // Initialize the database
        fieldRepository.saveAndFlush(field)

        // Get all the fieldList
        restFieldMockMvc.perform(get("/api/fields?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
    }
    
    @Test
    @Transactional
    fun getField() {
        // Initialize the database
        fieldRepository.saveAndFlush(field)

        val id = field.id
        assertNotNull(id)

        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
    }

    @Test
    @Transactional
    fun getNonExistingField() {
        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateField() {
        // Initialize the database
        fieldRepository.saveAndFlush(field)

        val databaseSizeBeforeUpdate = fieldRepository.findAll().size

        // Update the field
        val id = field.id
        assertNotNull(id)
        val updatedField = fieldRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedField are not directly saved in db
        em.detach(updatedField)
        updatedField.name = UPDATED_NAME
        updatedField.data = UPDATED_DATA

        restFieldMockMvc.perform(
            put("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedField))
        ).andExpect(status().isOk)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)
        val testField = fieldList[fieldList.size - 1]
        assertThat(testField.name).isEqualTo(UPDATED_NAME)
        assertThat(testField.data).isEqualTo(UPDATED_DATA)

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(1)).save(testField)
    }

    @Test
    @Transactional
    fun updateNonExistingField() {
        val databaseSizeBeforeUpdate = fieldRepository.findAll().size

        // Create the Field

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc.perform(
            put("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field))
        ).andExpect(status().isBadRequest)

        // Validate the Field in the database
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field)
    }

    @Test
    @Transactional
    fun deleteField() {
        // Initialize the database
        fieldRepository.saveAndFlush(field)

        val databaseSizeBeforeDelete = fieldRepository.findAll().size

        val id = field.id
        assertNotNull(id)

        // Delete the field
        restFieldMockMvc.perform(
            delete("/api/fields/{id}", id)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database is empty
        val fieldList = fieldRepository.findAll()
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    fun searchField() {
        // Initialize the database
        fieldRepository.saveAndFlush(field)
        `when`(mockFieldSearchRepository.search(queryStringQuery("id:" + field.id)))
            .thenReturn(Collections.singletonList(field))
        // Search the field
        restFieldMockMvc.perform(get("/api/_search/fields?query=id:" + field.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        TestUtil.equalsVerifier(Field::class.java)
        val field1 = Field()
        field1.id = 1L
        val field2 = Field()
        field2.id = field1.id
        assertThat(field1).isEqualTo(field2)
        field2.id = 2L
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
        fun createEntity(em: EntityManager): Field {
            val field = Field()
            field.name = DEFAULT_NAME
            field.data = DEFAULT_DATA

        return field
        }
        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Field {
            val field = Field()
            field.name = UPDATED_NAME
            field.data = UPDATED_DATA

        return field
        }
    }
}
