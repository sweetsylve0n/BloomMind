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
        return EmotionDetail(
            nameResId = emotionResId,
            description = stringResource(id = getDescriptionRes(emotionResId)),
            advice = stringResource(id = getAdviceRes(emotionResId)),
            imageResId = getImageRes(emotionResId)
        )
    }

    fun getDescriptionRes(emotionResId: Int): Int {
        return when (emotionResId) {
            R.string.emotion_anxious -> R.string.desc_anxious
            R.string.emotion_tired -> R.string.desc_tired
            R.string.emotion_confused -> R.string.desc_confused
            R.string.emotion_guilty -> R.string.desc_guilty
            R.string.emotion_disappointed -> R.string.desc_disappointed
            R.string.emotion_stressed -> R.string.desc_stressed
            R.string.emotion_annoyed -> R.string.desc_annoyed
            R.string.emotion_worried -> R.string.desc_worried
            R.string.emotion_overwhelmed -> R.string.desc_overwhelmed
            R.string.emotion_sad -> R.string.desc_sad
            R.string.emotion_bored -> R.string.desc_bored
            R.string.emotion_calm -> R.string.desc_calm
            R.string.emotion_comfortable -> R.string.desc_comfortable
            R.string.emotion_indifferent -> R.string.desc_indifferent
            R.string.emotion_grateful -> R.string.desc_grateful
            R.string.emotion_at_ease -> R.string.desc_at_ease
            R.string.emotion_excited -> R.string.desc_excited
            R.string.emotion_entertained -> R.string.desc_entertained
            R.string.emotion_hopeful -> R.string.desc_hopeful
            R.string.emotion_happy -> R.string.desc_happy
            R.string.emotion_inspired -> R.string.desc_inspired
            R.string.emotion_peaceful -> R.string.desc_peaceful
            R.string.emotion_satisfied -> R.string.desc_satisfied
            R.string.emotion_serene -> R.string.desc_serene
            else -> R.string.desc_default
        }
    }

    fun getAdviceRes(emotionResId: Int): Int {
        return when (emotionResId) {
            R.string.emotion_anxious -> R.string.adv_anxious
            R.string.emotion_tired -> R.string.adv_tired
            R.string.emotion_confused -> R.string.adv_confused
            R.string.emotion_guilty -> R.string.adv_guilty
            R.string.emotion_disappointed -> R.string.adv_disappointed
            R.string.emotion_stressed -> R.string.adv_stressed
            R.string.emotion_annoyed -> R.string.adv_annoyed
            R.string.emotion_worried -> R.string.adv_worried
            R.string.emotion_overwhelmed -> R.string.adv_overwhelmed
            R.string.emotion_sad -> R.string.adv_sad
            R.string.emotion_bored -> R.string.adv_bored
            R.string.emotion_calm -> R.string.adv_calm
            R.string.emotion_comfortable -> R.string.adv_comfortable
            R.string.emotion_indifferent -> R.string.adv_indifferent
            R.string.emotion_grateful -> R.string.adv_grateful
            R.string.emotion_at_ease -> R.string.adv_at_ease
            R.string.emotion_excited -> R.string.adv_excited
            R.string.emotion_entertained -> R.string.adv_entertained
            R.string.emotion_hopeful -> R.string.adv_hopeful
            R.string.emotion_happy -> R.string.adv_happy
            R.string.emotion_inspired -> R.string.adv_inspired
            R.string.emotion_peaceful -> R.string.adv_peaceful
            R.string.emotion_satisfied -> R.string.adv_satisfied
            R.string.emotion_serene -> R.string.adv_serene
            else -> R.string.adv_default
        }
    }

    fun getImageRes(emotionResId: Int): Int {
        return when (emotionResId) {
            R.string.emotion_anxious -> R.drawable.crazy
            R.string.emotion_tired -> R.drawable.down
            R.string.emotion_confused -> R.drawable.confused
            R.string.emotion_guilty -> R.drawable.blush
            R.string.emotion_disappointed -> R.drawable.disappointed
            R.string.emotion_stressed -> R.drawable.tired
            R.string.emotion_annoyed -> R.drawable.annoyed
            R.string.emotion_worried -> R.drawable.shy
            R.string.emotion_overwhelmed -> R.drawable.scared
            R.string.emotion_sad -> R.drawable.sad
            R.string.emotion_bored -> R.drawable.sleepy
            R.string.emotion_calm -> R.drawable.staring
            R.string.emotion_comfortable -> R.drawable.calm
            R.string.emotion_indifferent -> R.drawable.annoyed
            R.string.emotion_grateful -> R.drawable.thankfull
            R.string.emotion_at_ease -> R.drawable.satisfied
            R.string.emotion_excited -> R.drawable.exited
            R.string.emotion_entertained -> R.drawable.staring
            R.string.emotion_hopeful -> R.drawable.blush
            R.string.emotion_happy -> R.drawable.happy
            R.string.emotion_inspired -> R.drawable.inspired
            R.string.emotion_peaceful -> R.drawable.calm
            R.string.emotion_satisfied -> R.drawable.satisfied
            R.string.emotion_serene -> R.drawable.calm
            else -> R.drawable.icon1
        }
    }
}
