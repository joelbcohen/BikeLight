package io.callista.bikelight.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import io.callista.bikelight.R
import io.callista.bikelight.presentation.MainActivity

class MainComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? = when (type) {
        ComplicationType.SHORT_TEXT -> buildShortText(tapAction = null)
        ComplicationType.SMALL_IMAGE -> buildSmallImage(tapAction = null)
        else -> null
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        val tapAction = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return when (request.complicationType) {
            ComplicationType.SHORT_TEXT -> buildShortText(tapAction)
            ComplicationType.SMALL_IMAGE -> buildSmallImage(tapAction)
            else -> throw IllegalArgumentException("Unsupported type: ${request.complicationType}")
        }
    }

    private fun icon() = Icon.createWithResource(this, R.drawable.ic_bikelight)

    private fun smallImage() = SmallImage.Builder(icon(), SmallImageType.PHOTO).build()

    private fun buildShortText(tapAction: PendingIntent?) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder("Bike").build(),
            contentDescription = PlainComplicationText.Builder("Bike Light").build()
        )
            .setTitle(PlainComplicationText.Builder("Light").build())
            .setSmallImage(smallImage())
            .apply { tapAction?.let { setTapAction(it) } }
            .build()

    private fun buildSmallImage(tapAction: PendingIntent?) =
        SmallImageComplicationData.Builder(
            smallImage = smallImage(),
            contentDescription = PlainComplicationText.Builder("Bike Light").build()
        )
            .apply { tapAction?.let { setTapAction(it) } }
            .build()
}
