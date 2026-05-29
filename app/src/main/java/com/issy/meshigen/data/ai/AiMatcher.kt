package com.issy.meshigen.data.ai

import com.issy.meshigen.data.local.entity.GourmetEntity

/**
 * AIから候補を取得する責務境界。
 *
 * 実装は後続Issueで差し替える前提:
 *   - StubAiMatcher: 常にEmptyを返すテスト用空実装（#73-1）
 *   - GeminiAiMatcher: Firebase AI Logic SDK経由で本実装（#73-3）
 */
interface AiMatcher {
    suspend fun selectCandidates(
        moodText: String,
        gourmets: List<GourmetEntity>,
    ): AiMatchingResult
}