package com.issy.meshigen.data.ai

import com.issy.meshigen.data.local.entity.GourmetEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class GourmetPromptFormatterTest {

    @Test
    fun toLightweightLine_singleGourmet_returnsExpectedFormat() {
        val gourmet = gourmetWith(id = 1, name = "焼うどん", area = "小倉北区・鳥町", category = "麺類", description = "小倉発祥のB級グルメ")
        val result = GourmetPromptFormatter.toLightweightLine(gourmet)
        assertEquals("1|焼うどん|小倉北区・鳥町|麺類|小倉発祥のB級グルメ", result)
    }

    @Test
    fun toLightweightList_multipleGourmets_returnsNewlineSeparated() {
        val gourmets = listOf(
            gourmetWith(id = 1, name = "焼うどん", area = "小倉北区", category = "麺類", description = "desc1"),
            gourmetWith(id = 2, name = "焼きカレー", area = "門司区", category = "カレー", description = "desc2"),
        )
        val result = GourmetPromptFormatter.toLightweightList(gourmets)
        assertEquals("1|焼うどん|小倉北区|麺類|desc1\n2|焼きカレー|門司区|カレー|desc2", result)
    }

    @Test
    fun toLightweightLine_pipeInName_replacedWithSpace() {
        val gourmet = gourmetWith(id = 3, name = "焼|うどん", area = "小倉北区", category = "麺類", description = "説明")
        val result = GourmetPromptFormatter.toLightweightLine(gourmet)
        assertEquals("3|焼 うどん|小倉北区|麺類|説明", result)
    }

    @Test
    fun toLightweightLine_newlineInDescription_replacedWithSpace() {
        val gourmet = gourmetWith(id = 4, name = "焼うどん", area = "小倉北区", category = "麺類", description = "説明\n補足")
        val result = GourmetPromptFormatter.toLightweightLine(gourmet)
        assertEquals("4|焼うどん|小倉北区|麺類|説明 補足", result)
    }

    @Test
    fun toLightweightList_emptyList_returnsEmptyString() {
        val result = GourmetPromptFormatter.toLightweightList(emptyList())
        assertEquals("", result)
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
