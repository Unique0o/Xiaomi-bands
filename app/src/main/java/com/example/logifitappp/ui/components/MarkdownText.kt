package com.example.logifitappp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.halilibo.richtext.commonmark.CommonmarkAstNodeParser
import com.halilibo.richtext.markdown.BasicMarkdown
import com.halilibo.richtext.ui.BasicRichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.RichTextThemeProvider
import com.halilibo.richtext.ui.string.RichTextStringStyle

@Composable
fun MarkdownText(
    boldTextTypography: TextStyle = MaterialTheme.typography.titleMedium,
    normalTextTypography: TextStyle = MaterialTheme.typography.headlineMedium,
    text: String,
    textAlign: TextAlign = TextAlign.Unspecified
) {
    RichTextThemeProvider(
        textStyleProvider = {
            normalTextTypography.copy(
                color = MaterialTheme.colorScheme.surfaceTint,
                textAlign = textAlign
            )
        }
    ) {
        BasicRichText(style = RichTextStyle(
            stringStyle = RichTextStringStyle(
                boldStyle = boldTextTypography.toSpanStyle().copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        )) {
            val parser = remember { CommonmarkAstNodeParser() }
            val astNode = remember(parser) { parser.parse(text) }

            BasicMarkdown(astNode)
        }
    }
}