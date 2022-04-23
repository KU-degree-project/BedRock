package com.bed.android.bedrock.mlkit

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions

object TextRecognizeUtil {

    private const val TAG = "TextRecognizeUtil"
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    fun getTextFromBitmapByRecognizer(bitmap: Bitmap, callback: (List<String>) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image).addOnSuccessListener { result ->
            val list = mutableListOf<String>()
            result.textBlocks.forEach { block ->
                list.add(block.text)
            }

            callback(list)
        }.addOnFailureListener {
            callback(listOf(it.toString()))
        }
    }
}