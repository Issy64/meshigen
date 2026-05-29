package com.issy.meshigen.data.ai

import com.issy.meshigen.data.local.entity.GourmetEntity

/**
 * 常に[AiMatchingResult.Empty]を返すスタブ実装。
 *
 * 後続Issue #73-3 で[GeminiAiMatcher]に差し替える前提で、
 * このIssueでは型境界の足場として機能させる。
 *
 * 「Emptyを返す」性質は、フォールバック分岐（カテゴリ提案）の
 * 動作確認にも利用できる。
 */
class StubAiMatcher : AiMatcher {
    override suspend fun selectCandidates(
        moodText: String,
        gourmets: List<GourmetEntity>,
    ): AiMatchingResult = AiMatchingResult.Empty
}