package com.issy.meshigen.data.ai

/**
 * AIが返す1件の候補。
 *
 * ID + 紹介文のみを保持し、表示用の名前・カテゴリ・説明は持たない。
 * これにより、AIが存在しないグルメを創作（幻覚）しても、後段で
 * IDに該当するEntityがDBに無ければ即座に検出できる。
 */
data class AiCandidate(
    val id: Int,
    val reason: String,
)