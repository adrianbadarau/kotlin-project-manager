package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Comment
import com.lenovo.coe.repository.CommentRepository
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
 * Test class for the CommentResource REST controller.
 *
 * @see CommentResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class CommentResourceIT {

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Mock
    private lateinit var commentRepositoryMock: CommentRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restCommentMockMvc: MockMvc

    private lateinit var comment: Comment

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val commentResource = CommentResource(commentRepository)
        this.restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        commentRepository.deleteAll()
        comment = createEntity()
    }

    @Test
    fun createComment() {
        val databaseSizeBeforeCreate = commentRepository.findAll().size

        // Create the Comment
        restCommentMockMvc.perform(
            post("/api/comments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(comment))
        ).andExpect(status().isCreated)

        // Validate the Comment in the database
        val commentList = commentRepository.findAll()
        assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1)
        val testComment = commentList[commentList.size - 1]
        assertThat(testComment.body).isEqualTo(DEFAULT_BODY)
    }

    @Test
    fun createCommentWithExistingId() {
        val databaseSizeBeforeCreate = commentRepository.findAll().size

        // Create the Comment with an existing ID
        comment.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentMockMvc.perform(
            post("/api/comments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(comment))
        ).andExpect(status().isBadRequest)

        // Validate the Comment in the database
        val commentList = commentRepository.findAll()
        assertThat(commentList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkBodyIsRequired() {
        val databaseSizeBeforeTest = commentRepository.findAll().size
        // set the field null
        comment.body = null

        // Create the Comment, which fails.

        restCommentMockMvc.perform(
            post("/api/comments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(comment))
        ).andExpect(status().isBadRequest)

        val commentList = commentRepository.findAll()
        assertThat(commentList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllComments() {
        // Initialize the database
        commentRepository.save(comment)

        // Get all the commentList
        restCommentMockMvc.perform(get("/api/comments?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comment.id)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
    }
    
    @Suppress("unchecked")
    fun getAllCommentsWithEagerRelationshipsIsEnabled() {
        val commentResource = CommentResource(commentRepositoryMock)
        `when`(commentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restCommentMockMvc.perform(get("/api/comments?eagerload=true"))
            .andExpect(status().isOk)

        verify(commentRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllCommentsWithEagerRelationshipsIsNotEnabled() {
        val commentResource = CommentResource(commentRepositoryMock)
        `when`(commentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restCommentMockMvc.perform(get("/api/comments?eagerload=true"))
            .andExpect(status().isOk)

        verify(commentRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    fun getComment() {
        // Initialize the database
        commentRepository.save(comment)

        val id = comment.id
        assertNotNull(id)

        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
    }

    @Test
    fun getNonExistingComment() {
        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateComment() {
        // Initialize the database
        commentRepository.save(comment)

        val databaseSizeBeforeUpdate = commentRepository.findAll().size

        // Update the comment
        val id = comment.id
        assertNotNull(id)
        val updatedComment = commentRepository.findById(id).get()
        updatedComment.body = UPDATED_BODY

        restCommentMockMvc.perform(
            put("/api/comments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedComment))
        ).andExpect(status().isOk)

        // Validate the Comment in the database
        val commentList = commentRepository.findAll()
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate)
        val testComment = commentList[commentList.size - 1]
        assertThat(testComment.body).isEqualTo(UPDATED_BODY)
    }

    @Test
    fun updateNonExistingComment() {
        val databaseSizeBeforeUpdate = commentRepository.findAll().size

        // Create the Comment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMockMvc.perform(
            put("/api/comments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(comment))
        ).andExpect(status().isBadRequest)

        // Validate the Comment in the database
        val commentList = commentRepository.findAll()
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteComment() {
        // Initialize the database
        commentRepository.save(comment)

        val databaseSizeBeforeDelete = commentRepository.findAll().size

        val id = comment.id
        assertNotNull(id)

        // Delete the comment
        restCommentMockMvc.perform(
            delete("/api/comments/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val commentList = commentRepository.findAll()
        assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Comment::class)
        val comment1 = Comment()
        comment1.id = "id1"
        val comment2 = Comment()
        comment2.id = comment1.id
        assertThat(comment1).isEqualTo(comment2)
        comment2.id = "id2"
        assertThat(comment1).isNotEqualTo(comment2)
        comment1.id = null
        assertThat(comment1).isNotEqualTo(comment2)
    }

    companion object {

        private const val DEFAULT_BODY: String = "AAAAAAAAAA"
        private const val UPDATED_BODY = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Comment {
            val comment = Comment(
                body = DEFAULT_BODY
            )

            return comment
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Comment {
            val comment = Comment(
                body = UPDATED_BODY
            )

            return comment
        }
    }
}
