package com.dominio.bloommind.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dominio.bloommind.R

data class EmotionDetail(
    val nameResId: Int,
    val description: String,
    val advice: String,
    val imageResId: Int
)

object EmotionContentProvider {

    @Composable
    fun getEmotionDetail(emotionResId: Int): EmotionDetail {
        return when (emotionResId) {
            R.string.emotion_anxious -> EmotionDetail(
                nameResId = R.string.emotion_anxious,
                description = stringResource(id = R.string.desc_anxious),
                advice = stringResource(id = R.string.adv_anxious),
                imageResId = R.drawable.crazy
            )
            R.string.emotion_tired -> EmotionDetail(
                nameResId = R.string.emotion_tired,
                description = stringResource(id = R.string.desc_tired),
                advice = stringResource(id = R.string.adv_tired),
                imageResId = R.drawable.down
            )
            R.string.emotion_confused -> EmotionDetail(
                nameResId = R.string.emotion_confused,
                description = stringResource(id = R.string.desc_confused),
                advice = stringResource(id = R.string.adv_confused),
                imageResId = R.drawable.confused
            )
            R.string.emotion_guilty -> EmotionDetail(
                nameResId = R.string.emotion_guilty,
                description = stringResource(id = R.string.desc_guilty),
                advice = stringResource(id = R.string.adv_guilty),
                imageResId = R.drawable.blush
            )
            R.string.emotion_disappointed -> EmotionDetail(
                nameResId = R.string.emotion_disappointed,
                description = stringResource(id = R.string.desc_disappointed),
                advice = stringResource(id = R.string.adv_disappointed),
                imageResId = R.drawable.disappointed
            )
            R.string.emotion_stressed -> EmotionDetail(
                nameResId = R.string.emotion_stressed,
                description = stringResource(id = R.string.desc_stressed),
                advice = stringResource(id = R.string.adv_stressed),
                imageResId = R.drawable.tired
            )
            R.string.emotion_annoyed -> EmotionDetail(
                nameResId = R.string.emotion_annoyed,
                description = stringResource(id = R.string.desc_annoyed),
                advice = stringResource(id = R.string.adv_annoyed),
                imageResId = R.drawable.annoyed
            )
            R.string.emotion_worried -> EmotionDetail(
                nameResId = R.string.emotion_worried,
                description = stringResource(id = R.string.desc_worried),
                advice = stringResource(id = R.string.adv_worried),
                imageResId = R.drawable.shy
            )
            R.string.emotion_overwhelmed -> EmotionDetail(
                nameResId = R.string.emotion_overwhelmed,
                description = stringResource(id = R.string.desc_overwhelmed),
                advice = stringResource(id = R.string.adv_overwhelmed),
                imageResId = R.drawable.scared
            )
            R.string.emotion_sad -> EmotionDetail(
                nameResId = R.string.emotion_sad,
                description = stringResource(id = R.string.desc_sad),
                advice = stringResource(id = R.string.adv_sad),
                imageResId = R.drawable.sad
            )

            R.string.emotion_bored -> EmotionDetail(
                nameResId = R.string.emotion_bored,
                description = stringResource(id = R.string.desc_bored),
                advice = stringResource(id = R.string.adv_bored),
                imageResId = R.drawable.sleepy
            )
            R.string.emotion_calm -> EmotionDetail(
                nameResId = R.string.emotion_calm,
                description = stringResource(id = R.string.desc_calm),
                advice = stringResource(id = R.string.adv_calm),
                imageResId = R.drawable.staring
            )
            R.string.emotion_comfortable -> EmotionDetail(
                nameResId = R.string.emotion_comfortable,
                description = stringResource(id = R.string.desc_comfortable),
                advice = stringResource(id = R.string.adv_comfortable),
                imageResId = R.drawable.calm
            )
            R.string.emotion_indifferent -> EmotionDetail(
                nameResId = R.string.emotion_indifferent,
                description = stringResource(id = R.string.desc_indifferent),
                advice = stringResource(id = R.string.adv_indifferent),
                imageResId = R.drawable.annoyed
            )

            R.string.emotion_grateful -> EmotionDetail(
                nameResId = R.string.emotion_grateful,
                description = stringResource(id = R.string.desc_grateful),
                advice = stringResource(id = R.string.adv_grateful),
                imageResId = R.drawable.thankfull
            )
            R.string.emotion_at_ease -> EmotionDetail(
                nameResId = R.string.emotion_at_ease,
                description = stringResource(id = R.string.desc_at_ease),
                advice = stringResource(id = R.string.adv_at_ease),
                imageResId = R.drawable.satisfied
            )
            R.string.emotion_excited -> EmotionDetail(
                nameResId = R.string.emotion_excited,
                description = stringResource(id = R.string.desc_excited),
                advice = stringResource(id = R.string.adv_excited),
                imageResId = R.drawable.exited
            )
            R.string.emotion_entertained -> EmotionDetail(
                nameResId = R.string.emotion_entertained,
                description = stringResource(id = R.string.desc_entertained),
                advice = stringResource(id = R.string.adv_entertained),
                imageResId = R.drawable.staring
            )
            R.string.emotion_hopeful -> EmotionDetail(
                nameResId = R.string.emotion_hopeful,
                description = stringResource(id = R.string.desc_hopeful),
                advice = stringResource(id = R.string.adv_hopeful),
                imageResId = R.drawable.blush
            )
            R.string.emotion_happy -> EmotionDetail(
                nameResId = R.string.emotion_happy,
                description = stringResource(id = R.string.desc_happy),
                advice = stringResource(id = R.string.adv_happy),
                imageResId = R.drawable.happy
            )
            R.string.emotion_inspired -> EmotionDetail(
                nameResId = R.string.emotion_inspired,
                description = stringResource(id = R.string.desc_inspired),
                advice = stringResource(id = R.string.adv_inspired),
                imageResId = R.drawable.inspired
            )
            R.string.emotion_peaceful -> EmotionDetail(
                nameResId = R.string.emotion_peaceful,
                description = stringResource(id = R.string.desc_peaceful),
                advice = stringResource(id = R.string.adv_peaceful),
                imageResId = R.drawable.calm
            )
            R.string.emotion_satisfied -> EmotionDetail(
                nameResId = R.string.emotion_satisfied,
                description = stringResource(id = R.string.desc_satisfied),
                advice = stringResource(id = R.string.adv_satisfied),
                imageResId = R.drawable.satisfied
            )
            R.string.emotion_serene -> EmotionDetail(
                nameResId = R.string.emotion_serene,
                description = stringResource(id = R.string.desc_serene),
                advice = stringResource(id = R.string.adv_serene),
                imageResId = R.drawable.calm
            )

            else -> EmotionDetail(
                nameResId = emotionResId,
                description = stringResource(id = R.string.desc_default),
                advice = stringResource(id = R.string.adv_default),
                imageResId = R.drawable.icon1
            )
        }
    }
}
