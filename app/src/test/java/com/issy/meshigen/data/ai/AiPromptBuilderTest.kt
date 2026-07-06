package com.issy.meshigen.data.ai

import com.issy.meshigen.data.local.entity.GourmetEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AiPromptBuilderTest {

    @Test
    fun build_singleGourmet_returnsExpectedPrompt() {
        val gourmet = gourmetWith(id = 1, name = "焼うどん", area = "小倉北区・鳥町", category = "麺類", description = "小倉発祥")
        val moodText = "さっぱりしたものが食べたい"

        val result = AiPromptBuilder.build(moodText, listOf(gourmet))

        val expected = """
            あなたは北九州のB級グルメに詳しい案内人です。

            以下のグルメリスト（id|name|area|category|description）の中から、
            ユーザーの気分に合うものを1〜3件選んでください。
            返却順は、ユーザーの気分との適合度が高い順にしてください。

            ## グルメリスト
            1|焼うどん|小倉北区・鳥町|麺類|小倉発祥

            ## ユーザーの気分
            さっぱりしたものが食べたい
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun build_emptyMoodText_stillBuildsValidPrompt() {
        val gourmet = gourmetWith(id = 1, name = "焼うどん", area = "小倉北区", category = "麺類", description = "説明")

        val result = AiPromptBuilder.build("", listOf(gourmet))

        assertTrue(result.contains("## ユーザーの気分\n"))
    }

    private fun gourmetWith(
        id: Int,
        name: String,
        area: String,
        category: String,
        description: String,
    ) = GourmetEntity(
        id = id,
        name = name,
        area = area,
        category = category,
        description = description,
        searchKeyword = "",
    )
}
