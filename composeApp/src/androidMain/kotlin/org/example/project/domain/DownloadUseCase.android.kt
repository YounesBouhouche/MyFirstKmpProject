package org.example.project.domain

import android.content.Context
import android.net.Uri
import com.ketch.Ketch
import com.ketch.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import java.io.File
import androidx.core.net.toUri

actual class DownloadUseCase(private val ketch: Ketch, private val context: Context) {
    actual suspend operator fun invoke(
        url: String,
        path: String,
        onUpdate: (Float) -> Unit,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit
    ) {
        val uri = path.toUri()
        ketch.observeDownloadById(
            ketch.download(
                url,
                context.filesDir.path,
                uri.lastPathSegment ?: "temp-download-file",
            )
        )
            .flowOn(Dispatchers.IO)
            .filterNotNull()
            .collect { model ->
                when (model.status) {
                    Status.SUCCESS -> {
                        val tempFilePath = model.path
                        val tempFile = File(tempFilePath, model.fileName).absoluteFile
                        val inputStream = tempFile.inputStream()
                        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                            inputStream.copyTo(outputStream)
                            inputStream.close()
                        }
                        tempFile.delete()
                        onSuccess()
                    }
                    Status.QUEUED -> {

                    }
                    Status.STARTED -> {
                        onUpdate(model.progress / model.total.toFloat())
                    }
                    Status.PROGRESS -> {
                        onUpdate(model.progress / model.total.toFloat())
                    }
                    Status.CANCELLED -> {

                    }
                    Status.FAILED -> {

                    }
                    Status.PAUSED -> {

                    }
                    Status.DEFAULT -> {

                    }
                }
            }
    }
}