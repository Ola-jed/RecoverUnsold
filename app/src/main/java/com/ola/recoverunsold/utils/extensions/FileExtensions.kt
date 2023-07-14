package com.ola.recoverunsold.utils.extensions

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Create a file from an Uri
 * A tricky process
 */
fun Uri.createFile(context: Context): File {
    var fileName = ""
    val contentResolver = context.contentResolver
    this.let { returnUri ->
        contentResolver.query(returnUri, null, null, null)
    }?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }

    this.let { returnUri ->
        contentResolver.getType(returnUri)
    }

    val iStream: InputStream = contentResolver.openInputStream(this)!!
    val outputDir: File = context.cacheDir!!
    val outputFile = File(outputDir, fileName)
    copyStreamToFile(iStream, outputFile)
    iStream.close()
    return outputFile
}

/**
 * Copy the content of a stream to a file
 */
private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
    inputStream.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024)
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
}