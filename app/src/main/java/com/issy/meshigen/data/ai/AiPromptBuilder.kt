package com.issy.meshigen.data.ai

import com.issy.meshigen.data.local.entity.GourmetEntity

internal object AiPromptBuilder {

    const val SYSTEM_INSTRUCTION =
        "候補リストにあるグルメだけから選び、必ずidで返してください。リストにないグルメを創作しないでください。"

    private val PROMPT_TEMPLATE = """
        あなたは北九州のB級グルメに詳しい案内人です。

        以下のグルメリスト（id|name|area|category|description）の中から、
        ユーザーの気分に合うものを1〜3件選んでください。
        返却順は、ユーザーの気分との適合度が高い順にしてください。

        ## グルメリスト
        {軽量フォーマットで全候補を挿入}

        ## ユーザーの気分
        {ユーザーの入力テキスト}
    """.trimIndent()

    fun build(moodText: String, gourmets: List<GourmetEntity>): String =
        PROMPT_TEMPLATE
            .replace("{軽量フォーマットで全候補を挿入}", GourmetPromptFormatter.toLightweightList(gourmets))
            .replace("{ユーザーの入力テキスト}", moodText)
}
