package com.ola.recoverunsold.utils.resources

import androidx.annotation.StringRes
import com.ola.recoverunsold.App

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }
}