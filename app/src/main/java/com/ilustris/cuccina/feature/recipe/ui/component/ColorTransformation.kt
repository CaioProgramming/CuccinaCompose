package com.ilustris.cuccina.feature.recipe.ui.component

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ColorTransformation(
    private val markedText: List<String>,
    private val placeHolderText: String? = null,
    private val markColor: Color,
    private val weight: FontWeight = FontWeight.Normal
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            if (text.isNotEmpty()) annotatedStringWithColors(text) else text,
            OffsetMapping.Identity
        )
    }

    private fun annotatedStringWithColors(text: AnnotatedString): AnnotatedString {
        val builder = AnnotatedString.Builder()
        builder.append(text)
        Log.i(javaClass.simpleName, "annotatedStringWithColors: markedText -> $markedText")
        markedText.forEach { markText ->
            val markStart = text.indexOf(markText)
            val markEnd = text.indexOf(markText) + markText.length
            if (markStart == -1 || markEnd == -1) return@forEach
            builder.addStyle(
                SpanStyle(
                    color = markColor,
                    fontWeight = weight
                ),
                start = markStart,
                end = markEnd
            )
        }
        placeHolderText?.let {
            builder.addStyle(
                SpanStyle(
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                ),
                start = text.indexOf(placeHolderText),
                end = (text.indexOf(placeHolderText) + placeHolderText.length)
            )
        }
        return builder.toAnnotatedString()
    }

}