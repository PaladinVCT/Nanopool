package by.lebedev.nanopoolmonitoring.utils

import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods

@BindingMethods(
    BindingMethod(
        type = Toolbar::class,
        attribute = "onNavigateUpClick",
        method = "setNavigationOnClickListener"
    ),
    BindingMethod(
        type = Toolbar::class,
        attribute = "onMenuClick",
        method = "setOnMenuItemClickListener"
    )
)
class ViewBindingMethods