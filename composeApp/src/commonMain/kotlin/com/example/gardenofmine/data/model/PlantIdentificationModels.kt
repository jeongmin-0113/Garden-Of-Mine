package com.example.gardenofmine.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ===========================================
 * Plant.id API 데이터 모델
 * ===========================================
 *
 * Plant.id API v3의 요청/응답 데이터 클래스들입니다.
 * API 문서: https://github.com/flowerchecker/plant-id-examples/wiki
 */

// ===========================================
// 요청 모델
// ===========================================

/**
 * 식물 식별 요청
 *
 * @param images Base64 인코딩된 이미지 리스트 (data:image/jpeg;base64,... 형식)
 * @param latitude 위도 (선택, 정확도 ~3% 향상)
 * @param longitude 경도 (선택)
 * @param similar_images 유사 이미지 포함 여부
 */
@Serializable
data class PlantIdentificationRequest(
    val images: List<String>,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val similar_images: Boolean = true
)

// ===========================================
// 응답 모델
// ===========================================

/**
 * 식물 식별 응답 (최상위)
 */
@Serializable
data class PlantIdentificationResponse(
    val access_token: String? = null,
    val model_version: String? = null,
    val input: Input? = null,
    val result: Result? = null,
    val status: String? = null,
    val sla_compliant_client: Boolean? = null,
    val sla_compliant_system: Boolean? = null,
    val created: Double? = null,
    val completed: Double? = null
)

/**
 * 요청 입력 정보 (에코백)
 */
@Serializable
data class Input(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val similar_images: Boolean? = null,
    val images: List<String>? = null,
    val datetime: String? = null
)

/**
 * 식별 결과
 */
@Serializable
data class Result(
    val is_plant: IsPlant? = null,
    val classification: Classification? = null
)

/**
 * 식물 여부 판단 결과
 *
 * @param probability 식물일 확률 (0.0 ~ 1.0)
 * @param binary 식물 여부 (threshold 기준)
 * @param threshold 판단 기준값
 */
@Serializable
data class IsPlant(
    val probability: Double? = null,
    val binary: Boolean? = null,
    val threshold: Double? = null
)

/**
 * 분류 결과
 */
@Serializable
data class Classification(
    val suggestions: List<Suggestion>? = null
)

/**
 * 식물 종 제안
 *
 * @param id 고유 식별자
 * @param name 학명
 * @param probability 일치 확률 (0.0 ~ 1.0)
 * @param similar_images 유사한 참조 이미지들
 * @param details 상세 정보 (common_names, taxonomy 등)
 */
@Serializable
data class Suggestion(
    val id: String? = null,
    val name: String = "Unknown",
    val probability: Double = 0.0,
    val similar_images: List<SimilarImage>? = null,
    val details: SuggestionDetails? = null
)

/**
 * 유사 이미지 정보
 */
@Serializable
data class SimilarImage(
    val id: String? = null,
    val url: String = "",
    val similarity: Double? = null,
    val url_small: String? = null,
    @SerialName("license_name")
    val licenseName: String? = null,
    @SerialName("license_url")
    val licenseUrl: String? = null,
    val citation: String? = null
)

/**
 * 식물 상세 정보
 */
@Serializable
data class SuggestionDetails(
    val common_names: List<String>? = null,  // 일반명 (예: "장미", "Rose")
    val taxonomy: Taxonomy? = null,           // 분류학적 정보
    val url: String? = null,                  // 상세 정보 URL
    val description: Description? = null      // 설명
)

/**
 * 분류학적 정보
 */
@Serializable
data class Taxonomy(
    val kingdom: String? = null,   // 계 (예: Plantae)
    val phylum: String? = null,    // 문
    @SerialName("class")
    val taxonomyClass: String? = null,  // 강
    val order: String? = null,     // 목
    val family: String? = null,    // 과 (예: Rosaceae)
    val genus: String? = null      // 속 (예: Rosa)
)

/**
 * 식물 설명
 */
@Serializable
data class Description(
    val value: String? = null,
    val citation: String? = null,
    @SerialName("license_name")
    val licenseName: String? = null,
    @SerialName("license_url")
    val licenseUrl: String? = null
)
