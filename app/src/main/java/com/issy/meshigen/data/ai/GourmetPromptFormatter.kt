package com.issy.meshigen.data.ai

import com.issy.meshigen.data.local.entity.GourmetEntity

internal object GourmetPromptFormatter {

    fun toLightweightLine(gourmet: GourmetEntity): String {
        val id = "${gourmet.id}"
        val name = sanitize(gourmet.name)
        val area = sanitize(gourmet.area)
        val category = sanitize(gourmet.category)
        val description = sanitize(gourmet.description)
        return "$id|$name|$area|$category|$description"
    }

    fun toLightweightList(gourmets: List<GourmetEntity>): String =
        gourmets.joinToString(separator = "\n") { toLightweightLine(it) }

    private fun sanitize(value: String): String =
        value.replace("|", " ").replace("\n", " ")
}