package io.callista.bikelight.complication

import android.app.PendingIntent
import android.content.Intent
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import io.callista.bikelight.presentation.MainActivity

class MainComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) return null
        return buildData(tapAction = null)
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        val tapAction = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return buildData(tapAction)
    }

    private fun buildData(tapAction: PendingIntent?) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder("Bike").build(),
            contentDescription = PlainComplicationText.Builder("Bike Light").build()
        )
            .setTitle(PlainComplicationText.Builder("Light").build())
            .apply { tapAction?.let { setTapAction(it) } }
            .build()
}
