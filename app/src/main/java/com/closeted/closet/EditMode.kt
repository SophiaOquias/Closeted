package com.closeted.closet

enum class EditMode {
    NORMAL, // Default mode
    DELETE,   // Delete mode (for deleting items in the closet)
    SELECT, // Select mode (For putting closet items in laundry or laundry items in closet)
    SELECT_ALL // Select all mode (applicable to Laundry fragment only)
}