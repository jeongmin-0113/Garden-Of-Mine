package com.example.gardenofmine.ui.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.gardenofmine.data.model.Suggestion
import com.example.gardenofmine.ui.screen.search.SearchViewModel
import gardenofmine.composeapp.generated.resources.Res
import gardenofmine.composeapp.generated.resources.gallery
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

/**
 * ===========================================
 * 식물 식별 화면 (Search Screen)
 * ===========================================
 *
 * Plant.id API를 사용하여 이미지에서 식물을 식별하는 화면입니다.
 *
 * 기능:
 * - 갤러리에서 이미지 선택
 * - 선택된 이미지 미리보기
 * - 식물 식별 요청 및 결과 표시
 *
 * 사용된 라이브러리:
 * - FileKit: 이미지 선택
 * - Coil: 이미지 로딩
 * - Ktor: API 통신
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel { SearchViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // FileKit 이미지 피커 설정
    val imagePickerLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image
    ) { file ->
        file?.let {
            scope.launch {
                val bytes = it.readBytes()
                viewModel.onImageSelected(bytes)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 화면 제목
        Text(
            text = "식물 식별",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 이미지 선택/표시 영역
        if (uiState.selectedImageBytes == null) {
            ImageSelectionArea(
                onGalleryClick = { imagePickerLauncher.launch() }
            )
        } else {
            SelectedImageArea(
                imageBitmap = uiState.imageBitmap,
                isLoading = uiState.isLoading,
                onIdentifyClick = { viewModel.identifyPlant() },
                onClearClick = { viewModel.clearResult() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 에러 메시지 표시
        uiState.errorMessage?.let { error ->
            ErrorCard(message = error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 식별 결과 표시
        uiState.identificationResult?.let { suggestions ->
            IdentificationResultSection(
                suggestions = suggestions,
                isPlant = uiState.isPlant
            )
        }
    }
}

// ===========================================
// 이미지 선택 영역
// ===========================================

/**
 * 이미지가 선택되지 않았을 때 표시되는 선택 영역
 */
@Composable
private fun ImageSelectionArea(
    onGalleryClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "식물 사진을 선택하세요",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onGalleryClick) {
                Icon(
                    painter = painterResource(Res.drawable.gallery),
                    contentDescription = "갤러리",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("이미지 선택")
            }
        }
    }
}

/**
 * 이미지가 선택된 후 표시되는 미리보기 및 액션 영역
 */
@Composable
private fun SelectedImageArea(
    imageBitmap: ImageBitmap?,
    isLoading: Boolean,
    onIdentifyClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 미리보기
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "선택된 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                CircularProgressIndicator()
            }

            // 로딩 오버레이
            if (isLoading) {
                LoadingOverlay()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 액션 버튼
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onClearClick,
                enabled = !isLoading
            ) {
                Text("다시 선택")
            }

            Button(
                onClick = onIdentifyClick,
                enabled = !isLoading
            ) {
                Text("식물 식별하기")
            }
        }
    }
}

/**
 * 로딩 중 오버레이
 */
@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "식물 식별 중...",
                color = Color.White
            )
        }
    }
}

// ===========================================
// 결과 표시 영역
// ===========================================

/**
 * 에러 메시지 카드
 */
@Composable
private fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

/**
 * 식별 결과 섹션
 */
@Composable
private fun IdentificationResultSection(
    suggestions: List<Suggestion>,
    isPlant: Boolean?
) {
    // 식물이 아닌 경우 경고
    if (isPlant == false) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = "이 이미지는 식물이 아닌 것 같습니다.",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    // 결과 제목
    Text(
        text = "식별 결과",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    // 결과 목록
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(suggestions) { suggestion ->
            PlantSuggestionCard(suggestion)
        }
    }
}

/**
 * 식물 제안 카드
 * 학명, 일반명, 일치율, 분류 정보를 표시합니다.
 */
@Composable
private fun PlantSuggestionCard(suggestion: Suggestion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 유사 이미지 썸네일
            suggestion.similar_images?.firstOrNull()?.let { similarImage ->
                AsyncImage(
                    model = similarImage.url_small ?: similarImage.url,
                    contentDescription = suggestion.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            // 식물 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 학명
                Text(
                    text = suggestion.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // 일반명
                suggestion.details?.common_names?.firstOrNull()?.let { commonName ->
                    Text(
                        text = commonName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 일치율
                val probability = (suggestion.probability * 100).toInt()
                Text(
                    text = "일치율: $probability%",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (probability >= 70)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 과(Family) 정보
                suggestion.details?.taxonomy?.family?.let { family ->
                    Text(
                        text = "과: $family",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
