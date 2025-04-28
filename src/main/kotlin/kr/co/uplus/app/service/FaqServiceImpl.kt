package kr.co.uplus.app.service

import kr.co.uplus.app.dto.request.CreateFaqRequest
import kr.co.uplus.app.dto.request.UpdateFaqRequest
import kr.co.uplus.app.dto.response.FaqResponse
import kr.co.uplus.app.dto.toEntity
import kr.co.uplus.app.dto.toResponse
import kr.co.uplus.app.dto.applyUpdate
import kr.co.uplus.app.repository.FaqRepository
import kr.co.uplus.app.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.HashMap

/**
 * Implementation of the FaqService interface.
 */
@Service
class FaqServiceImpl(
    private val faqRepository: FaqRepository
) : FaqService {

    override fun createFaq(request: CreateFaqRequest): FaqResponse {
        logger.info("Creating new FAQ: {}", request)
        val faqEntity = request.toEntity()
        val savedEntity = faqRepository.save(faqEntity)
        return savedEntity.toResponse()
    }

    override fun getAllFaqsByOsType(osType: String): ResponseEntity<Any> {
        logger.info("Retrieving all FAQs for OS type: {}", osType)
        val faqs = faqRepository.findByOsType(osType).map { it.toResponse() }
        
        if (faqs.isEmpty()) {
            logger.info("No FAQs found for OS type: {}", osType)
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(mapOf(
                    "code" to "SUCCESS_0004",
                    "message" to "요청이 성공적으로 처리되었지만 반환할 내용이 없습니다."
                ))
        }
        
        val response = HashMap<String, Any>()
        response["code"] = "SUCCESS_0001"
        response["message"] = "요청이 성공적으로 처리되었습니다."
        response["data"] = mapOf("faqs" to faqs)
        
        return ResponseEntity.ok(response)
    }

    override fun getFaqByIdAndOsType(id: Long?, osType: String): ResponseEntity<Any> {
        logger.info("Retrieving FAQ with id: {} and OS type: {}", id, osType)
        
        if (id == null) {
            return getAllFaqsByOsType(osType)
        }
        
        val faq = faqRepository.findByIdAndOsType(id, osType)
        
        if (faq == null) {
            logger.warn("FAQ with id {} and OS type {} not found", id, osType)
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(mapOf(
                    "code" to "SUCCESS_0004",
                    "message" to "요청이 성공적으로 처리되었지만 반환할 내용이 없습니다."
                ))
        }
        
        val response = HashMap<String, Any>()
        response["code"] = "SUCCESS_0001"
        response["message"] = "요청이 성공적으로 처리되었습니다."
        response["data"] = mapOf("faqs" to listOf(faq.toResponse()))
        
        return ResponseEntity.ok(response)
    }

    override fun updateFaq(id: Long, request: UpdateFaqRequest): ResponseEntity<FaqResponse> {
        logger.info("Updating FAQ with id: {}", id)
        return faqRepository.findById(id).map {
            val updatedEntity = it.applyUpdate(request)
            val savedEntity = faqRepository.save(updatedEntity)
            ResponseEntity.ok(savedEntity.toResponse())
        }.orElse(ResponseEntity.notFound().build())
    }

    override fun deleteFaq(id: Long): ResponseEntity<Void> {
        logger.info("Deleting FAQ with id: {}", id)
        val faq = faqRepository.findById(id)
        return if (faq.isPresent) {
            faqRepository.delete(faq.get())
            ResponseEntity.noContent().build()
        } else {
            logger.warn("FAQ with id {} not found", id)
            ResponseEntity.notFound().build()
        }
    }
}