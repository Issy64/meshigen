package com.issy.meshigen.data.ai

/**
 * AI呼び出しの結果。
 *
 * 成功・空・パース失敗・通信失敗を型レベルで分け、呼び出し側で
 * when による網羅的な分岐を可能にする。
 */
sealed interface AiMatchingResult {
    /** AIが1〜3件の候補を返した正常系。 */
    data class Success(val candidates: List<AiCandidate>) : AiMatchingResult
    /** AIが空配列を返した。フォールバック（カテゴリ提案）へ。 */
    data object Empty : AiMatchingResult
    /** JSONパース失敗。スキーマ不一致・破損レスポンス等。 */
    data object ParseError : AiMatchingResult
    /** ネットワーク・SDK例外。原因をthrowableで保持する。 */
    data class NetworkError(val cause: Throwable) : AiMatchingResult
}