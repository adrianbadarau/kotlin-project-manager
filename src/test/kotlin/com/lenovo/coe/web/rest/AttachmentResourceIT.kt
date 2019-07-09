package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Attachment
import com.lenovo.coe.repository.AttachmentRepository
import com.lenovo.coe.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator


import org.assertj.core.api.Assertions.assertThat
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
 * Test class for the AttachmentResource REST controller.
 *
 * @see AttachmentResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class AttachmentResourceIT {

    @Autowired
    private lateinit var attachmentRepository: AttachmentRepository

    @Mock
    private lateinit var attachmentRepositoryMock: AttachmentRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restAttachmentMockMvc: MockMvc

    private lateinit var attachment: Attachment

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val attachmentResource = AttachmentResource(attachmentRepository)
        this.restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        attachmentRepository.deleteAll()
        attachment = createEntity()
    }

    @Test
    fun createAttachment() {
        val databaseSizeBeforeCreate = attachmentRepository.findAll().size

        // Create the Attachment
        restAttachmentMockMvc.perform(
            post("/api/attachments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(attachment))
        ).andExpect(status().isCreated)

        // Validate the Attachment in the database
        val attachmentList = attachmentRepository.findAll()
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1)
        val testAttachment = attachmentList[attachmentList.size - 1]
        assertThat(testAttachment.filename).isEqualTo(DEFAULT_FILENAME)
    }

    @Test
    fun createAttachmentWithExistingId() {
        val databaseSizeBeforeCreate = attachmentRepository.findAll().size

        // Create the Attachment with an existing ID
        attachment.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentMockMvc.perform(
            post("/api/attachments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(attachment))
        ).andExpect(status().isBadRequest)

        // Validate the Attachment in the database
        val attachmentList = attachmentRepository.findAll()
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkFilenameIsRequired() {
        val databaseSizeBeforeTest = attachmentRepository.findAll().size
        // set the field null
        attachment.filename = null

        // Create the Attachment, which fails.

        restAttachmentMockMvc.perform(
            post("/api/attachments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(attachment))
        ).andExpect(status().isBadRequest)

        val attachmentList = attachmentRepository.findAll()
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllAttachments() {
        // Initialize the database
        attachmentRepository.save(attachment)

        // Get all the attachmentList
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.id)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)))
    }
    
    @Suppress("unchecked")
    fun getAllAttachmentsWithEagerRelationshipsIsEnabled() {
        val attachmentResource = AttachmentResource(attachmentRepositoryMock)
        `when`(attachmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restAttachmentMockMvc.perform(get("/api/attachments?eagerload=true"))
            .andExpect(status().isOk)

        verify(attachmentRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllAttachmentsWithEagerRelationshipsIsNotEnabled() {
        val attachmentResource = AttachmentResource(attachmentRepositoryMock)
        `when`(attachmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restAttachmentMockMvc.perform(get("/api/attachments?eagerload=true"))
            .andExpect(status().isOk)

        verify(attachmentRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    fun getAttachment() {
        // Initialize the database
        attachmentRepository.save(attachment)

        val id = attachment.id
        assertNotNull(id)

        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME))
    }

    @Test
    fun getNonExistingAttachment() {
        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateAttachment() {
        // Initialize the database
        attachmentRepository.save(attachment)

        val databaseSizeBeforeUpdate = attachmentRepository.findAll().size

        // Update the attachment
        val id = attachment.id
        assertNotNull(id)
        val updatedAttachment = attachmentRepository.findById(id).get()
        updatedAttachment.filename = UPDATED_FILENAME

        restAttachmentMockMvc.perform(
            put("/api/attachments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedAttachment))
        ).andExpect(status().isOk)

        // Validate the Attachment in the database
        val attachmentList = attachmentRepository.findAll()
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate)
        val testAttachment = attachmentList[attachmentList.size - 1]
        assertThat(testAttachment.filename).isEqualTo(UPDATED_FILENAME)
    }

    @Test
    fun updateNonExistingAttachment() {
        val databaseSizeBeforeUpdate = attachmentRepository.findAll().size

        // Create the Attachment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc.perform(
            put("/api/attachments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(attachment))
        ).andExpect(status().isBadRequest)

        // Validate the Attachment in the database
        val attachmentList = attachmentRepository.findAll()
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteAttachment() {
        // Initialize the database
        attachmentRepository.save(attachment)

        val databaseSizeBeforeDelete = attachmentRepository.findAll().size

        val id = attachment.id
        assertNotNull(id)

        // Delete the attachment
        restAttachmentMockMvc.perform(
            delete("/api/attachments/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val attachmentList = attachmentRepository.findAll()
        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Attachment::class)
        val attachment1 = Attachment()
        attachment1.id = "id1"
        val attachment2 = Attachment()
        attachment2.id = attachment1.id
        assertThat(attachment1).isEqualTo(attachment2)
        attachment2.id = "id2"
        assertThat(attachment1).isNotEqualTo(attachment2)
        attachment1.id = null
        assertThat(attachment1).isNotEqualTo(attachment2)
    }

    companion object {

        private const val DEFAULT_FILENAME: String = "AAAAAAAAAA"
        private const val UPDATED_FILENAME = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Attachment {
            val attachment = Attachment(
                filename = DEFAULT_FILENAME
            )

            return attachment
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Attachment {
            val attachment = Attachment(
                filename = UPDATED_FILENAME
            )

            return attachment
        }
    }
}
