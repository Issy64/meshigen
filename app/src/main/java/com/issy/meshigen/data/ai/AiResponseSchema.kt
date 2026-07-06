package com.issy.meshigen.data.ai

import com.google.firebase.ai.type.Schema

internal object AiResponseSchema {
    val selectedSchema: Schema = Schema.obj(
        mapOf(
            "selected" to Schema.array(
                Schema.obj(
                    mapOf(
                        "id" to Schema.integer(),
                        "reason" to Schema.string()
                    )
                )
            )
        )
    )
}
