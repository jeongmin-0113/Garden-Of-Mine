package com.example.gardenofmine.config

import com.example.gardenofmine.BuildKonfig

/**
 * ===========================================
 * 앱 설정 관리
 * ===========================================
 *
 * BuildKonfig를 통해 빌드 시 주입된 설정 값들을 관리합니다.
 * API 키는 secrets.properties 파일에서 읽어옵니다.
 *
 * 설정 방법:
 * 1. 프로젝트 루트에 secrets.properties 파일 생성
 * 2. PLANT_ID_API_KEY=your_api_key 형식으로 키 입력
 * 3. Gradle Sync 실행
 *
 * 주의: secrets.properties는 .gitignore에 포함되어 있어
 *       GitHub에 업로드되지 않습니다.
 */
object AppConfig {

    /**
     * Plant.id API 키
     * https://admin.kindwise.com 에서 발급받을 수 있습니다.
     */
    val plantIdApiKey: String
        get() = BuildKonfig.PLANT_ID_API_KEY

    /**
     * API 키가 설정되어 있는지 확인
     */
    val isApiKeyConfigured: Boolean
        get() = plantIdApiKey.isNotBlank() && plantIdApiKey != "your_api_key_here"
}
