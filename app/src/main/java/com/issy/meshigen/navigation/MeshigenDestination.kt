package com.issy.meshigen.navigation

object MeshigenDestination {
    const val HOME_ROUTE = "home"
    const val COLLECTION_ROUTE = "collection"
    const val GOURMET_ID_ARG = "gourmetId"
    const val DETAIL_ROUTE = "detail/{$GOURMET_ID_ARG}"

    fun createDetailRoute(gourmetId: String): String {
        return "detail/$gourmetId"
    }
}
