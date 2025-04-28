package kr.co.uplus.app.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.co.uplus.app.dto.request.CreateFaqRequest
import kr.co.uplus.app.dto.request.UpdateFaqRequest
import kr.co.uplus.app.dto.response.FaqResponse
import kr.co.uplus.app.service.FaqService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/faq")
@Tag(name = "[App]Content", description = "Content related APIs")
class FaqController(
    private val faqService: FaqService
) {

    @GetMapping
    @Operation(
        summary = "FAQ를 조회하는 API",
        description = "OS 타입에 따른 FAQ를 조회합니다. ID가 제공되면 특정 FAQ를 조회하고, 그렇지 않으면 모든 FAQ를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "FAQ 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Any::class),
                    examples = [ExampleObject(
                        name = "SuccessResponse",
                        value = """
                        {
                          "code": "SUCCESS_0001",
                          "message": "요청이 성공적으로 처리되었습니다.",
                          "data": {
                            "faqs": [
                              {
                                "id": 1,
                                "question": "질문",
                                "answer": "답변",
                                "faqType": 1,
                                "faqCategory": "전화",
                                "isFavorite": true,
                                "imageUrl": "",
                                "appLink": "",
                                "startTime": "1970-01-01T00:00:01.000Z"
                              }
                            ]
                          }
                        }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "204",
                description = "No Content",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Any::class),
                    examples = [ExampleObject(
                        name = "NoContentResponse",
                        value = """
                        {
                          "code": "SUCCESS_0004",
                          "message": "요청이 성공적으로 처리되었지만 반환할 내용이 없습니다."
                        }
                        """
                    )]
                )]
            )
        ]
    )
    fun getFaqs(
        @Parameter(description = "조회할 FAQ ID", example = "1")
        @RequestParam(required = false) id: Long?,
        
        @Parameter(description = "OS 타입", schema = Schema(type = "string", allowableValues = ["iOS", "AOS"]))
        @RequestParam(required = true) os: String
    ): ResponseEntity<Any> {
        return faqService.getFaqByIdAndOsType(id, os)
    }

    @PostMapping
    @Operation(summary = "새로운 FAQ를 생성하는 API")
    fun createFaq(@Valid @RequestBody request: CreateFaqRequest): FaqResponse {
        return faqService.createFaq(request)
    }

    @PutMapping("/{id}")
    @Operation(summary = "기존 FAQ를 수정하는 API")
    fun updateFaq(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateFaqRequest
    ): ResponseEntity<FaqResponse> {
        return faqService.updateFaq(id, request)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "FAQ를 삭제하는 API")
    fun deleteFaq(@PathVariable id: Long): ResponseEntity<Void> {
        return faqService.deleteFaq(id)
    }
}