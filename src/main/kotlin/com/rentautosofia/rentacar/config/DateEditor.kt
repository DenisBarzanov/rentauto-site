package com.rentautosofia.rentacar.config

import java.beans.PropertyEditorSupport
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateEditor : PropertyEditorSupport() {

    override fun setAsText(value: String) {
        try {
            setValue(SimpleDateFormat("dd-MM-yyyy").parse(value))
        } catch (e: ParseException) {
            setValue(null)
        }
    }

    override fun getAsText(): String {
        var s = ""
        if (value != null) {
            s = SimpleDateFormat("dd-MM-yyyy").format(value as Date)
        }
        return s
    }
}