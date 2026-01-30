package com.example.gardenofmine.data.api

import com.example.gardenofmine.data.model.PlantIdentificationRequest
import com.example.gardenofmine.data.model.PlantIdentificationResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * ===========================================
 * Plant.id API 클라이언트
 * ===========================================
 *
 * Plant.id API v3를 사용하여 식물을 식별합니다.
 * API 문서: https://github.com/flowerchecker/plant-id-examples/wiki
 *
 * 주요 기능:
 * - 이미지 기반 식물 식별
 * - 유사 이미지 검색
 *
 * @param apiKey Plant.id API 키 (https://admin.kindwise.com 에서 발급)
 */
class PlantIdApi(private val apiKey: String) {

    private val json = Json {
        ignoreUnknownKeys = true  // API 응답에 정의되지 않은 필드 무시
        isLenient = true          // 유연한 JSON 파싱
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    /**
     * 이미지를 기반으로 식물을 식별합니다.
     *
     * @param base64Images Base64로 인코딩된 이미지 리스트
     * @param latitude 위도 (선택, 정확도 향상에 도움)
     * @param longitude 경도 (선택, 정확도 향상에 도움)
     * @return 식별 결과 또는 오류
     */
    suspend fun identifyPlant(
        base64Images: List<String>,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<PlantIdentificationResponse> {
        return try {
            // Base64 이미지에 data URI 접두사 추가 (API 요구사항)
            val formattedImages = base64Images.map { base64 ->
                if (base64.startsWith("data:")) {
                    base64
                } else {
                    "data:image/jpeg;base64,$base64"
                }
            }

            val response = client.post("$BASE_URL/identification") {
                header("Api-Key", apiKey)
                contentType(ContentType.Application.Json)
                setBody(
                    PlantIdentificationRequest(
                        images = formattedImages,
                        latitude = latitude,
                        longitude = longitude,
                        similar_images = true
                    )
                )
            }

            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                val parsed = json.decodeFromString<PlantIdentificationResponse>(responseBody)
                Result.success(parsed)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("API 오류 (${response.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("요청 실패: ${e.message}", e))
        }
    }

    companion object {
        /** Plant.id API v3 기본 URL */
        private const val BASE_URL = "https://plant.id/api/v3"
    }
}
