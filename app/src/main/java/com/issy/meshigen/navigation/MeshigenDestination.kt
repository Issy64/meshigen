package com.issy.meshigen.navigation

object MeshigenDestination {
    const val HOME_ROUTE = "home"
    const val COLLECTION_ROUTE = "collection"
    const val DETAIL_BASE_ROUTE = "detail"
    const val GOURMET_ID_ARG = "gourmetId"
    const val DETAIL_ROUTE = "$DETAIL_BASE_ROUTE/{$GOURMET_ID_ARG}"

    fun createDetailRoute(gourmetId: String): String {
        return "$DETAIL_BASE_ROUTE/$gourmetId"
    }
}
