package org.example.project.domain

import org.asynchttpclient.AsyncCompletionHandler
import org.asynchttpclient.AsyncHandler
import org.asynchttpclient.Dsl
import org.asynchttpclient.HttpResponseBodyPart
import org.asynchttpclient.Response
import java.io.FileOutputStream

actual class DownloadUseCase() {
    actual suspend operator fun invoke(
        url: String,
        path: String,
        onUpdate: (Float) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val client = Dsl.asyncHttpClient()
        val stream = FileOutputStream(path)
        client.prepareGet(url).execute(object: AsyncCompletionHandler<FileOutputStream>() {
            override fun onBodyPartReceived(content: HttpResponseBodyPart?): AsyncHandler.State {
                stream.channel.write(content?.bodyByteBuffer)
                return AsyncHandler.State.CONTINUE
            }
            override fun onCompleted(response: Response?): FileOutputStream {
                return stream
            }
        })
    }
}