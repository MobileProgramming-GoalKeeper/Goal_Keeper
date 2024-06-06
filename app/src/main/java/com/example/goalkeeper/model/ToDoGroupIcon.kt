package com.example.goalkeeper.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class ToDoGroupIcon(val imgVector: ImageVector) {
    FAVORITE(Icons.Filled.Favorite),
    STAR(Icons.Filled.Star),
    SHOPPING_CART(Icons.Filled.ShoppingCart),
    THUMB_UP(Icons.Filled.ThumbUp),
    BUILD(Icons.Filled.Build),
    WARNING(Icons.Filled.Warning),
    LOCATION_ON(Icons.Filled.LocationOn),
    PENCIL(Icons.Filled.Create);
}