package com.example.gardenofmine.ui.screen.search

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenofmine.config.AppConfig
import com.example.gardenofmine.data.api.PlantIdApi
import com.example.gardenofmine.data.model.Suggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * ===========================================
 * Search 화면 UI 상태
 * ===========================================
 *
 * @param selectedImageBytes 선택된 이미지의 바이트 배열
 * @param imageBitmap 화면에 표시할 이미지 비트맵
 * @param isLoading API 요청 중 여부
 * @param identificationResult 식별 결과 목록
 * @param errorMessage 오류 메시지
 * @param isPlant 식물 여부 판단 결과
 */
data class SearchUiState(
    val selectedImageBytes: ByteArray? = null,
    val imageBitmap: ImageBitmap? = null,
    val isLoading: Boolean = false,
    val identificationResult: List<Suggestion>? = null,
    val errorMessage: String? = null,
    val isPlant: Boolean? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SearchUiState

        if (selectedImageBytes != null) {
            if (other.selectedImageBytes == null) return false
            if (!selectedImageBytes.contentEquals(other.selectedImageBytes)) return false
        } else if (other.selectedImageBytes != null) return false
        if (imageBitmap != other.imageBitmap) return false
        if (isLoading != other.isLoading) return false
        if (identificationResult != other.identificationResult) return false
        if (errorMessage != other.errorMessage) return false
        if (isPlant != other.isPlant) return false

        return true
    }

    override fun hashCode(): Int {
        var result = selectedImageBytes?.contentHashCode() ?: 0
        result = 31 * result + (imageBitmap?.hashCode() ?: 0)
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + (identificationResult?.hashCode() ?: 0)
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        result = 31 * result + (isPlant?.hashCode() ?: 0)
        return result
    }
}

/**
 * ===========================================
 * Search 화면 ViewModel
 * ===========================================
 *
 * 식물 이미지 선택 및 Plant.id API를 통한 식별 기능을 관리합니다.
 *
 * 주요 기능:
 * - 이미지 선택 및 미리보기
 * - Plant.id API를 통한 식물 식별
 * - 결과 표시 및 오류 처리
 */
class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    /**
     * 이미지가 선택되었을 때 호출
     * 이미지를 비트맵으로 변환하고 상태를 업데이트합니다.
     *
     * @param imageBytes 선택된 이미지의 바이트 배열
     */
    fun onImageSelected(imageBytes: ByteArray) {
        viewModelScope.launch {
            val bitmap = imageBytes.decodeToImageBitmap()
            _uiState.value = _uiState.value.copy(
                selectedImageBytes = imageBytes,
                imageBitmap = bitmap,
                identificationResult = null,
                errorMessage = null,
                isPlant = null
            )
        }
    }

    /**
     * 선택된 이미지로 식물 식별을 시작합니다.
     * API 키가 설정되어 있지 않으면 오류 메시지를 표시합니다.
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun identifyPlant() {
        val imageBytes = _uiState.value.selectedImageBytes ?: return

        // API 키 확인
        if (!AppConfig.isApiKeyConfigured) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "API 키가 설정되지 않았습니다.\n" +
                        "secrets.properties 파일에 PLANT_ID_API_KEY를 설정해주세요."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // 이미지를 Base64로 인코딩
            val base64Image = Base64.encode(imageBytes)
            val api = PlantIdApi(AppConfig.plantIdApiKey)

            api.identifyPlant(listOf(base64Image))
                .onSuccess { response ->
                    val suggestions = response.result?.classification?.suggestions
                    val isPlant = response.result?.is_plant?.binary

                    if (suggestions.isNullOrEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "식물을 식별할 수 없습니다. 다른 이미지를 시도해주세요."
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            identificationResult = suggestions,
                            isPlant = isPlant
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "식물 식별 중 오류가 발생했습니다."
                    )
                }
        }
    }

    /**
     * 결과를 초기화하고 처음 상태로 돌아갑니다.
     */
    fun clearResult() {
        _uiState.value = SearchUiState()
    }
}
