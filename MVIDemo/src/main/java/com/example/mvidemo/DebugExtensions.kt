package com.example.mvidemo

import android.content.Context
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import io.realm.BuildConfig

fun Modifier.debugInputPointer(
    context: Context,
    timeCapsule: TimeCapsule<out UiState>,
):Modifier {
    return if (BuildConfig.DEBUG){
        this.pointerInput(Unit){
            detectTapGestures(
                onLongPress = {
                    showDebugAlertDialog(context, timeCapsule)
                }
            )
        }
    }else this
}

private fun showDebugAlertDialog(
    context: Context,
    timeTravelCapsule: TimeCapsule<out UiState>,
) {
    val alertDialogBuilder = AlertDialog.Builder(context, R.style.DebugDialogTheme)
    val adapter = ArrayAdapter<DebugState>(context, R.layout.debug_menu_item)
    adapter.addAll(timeTravelCapsule.getStates().mapIndexed(::DebugState))
    alertDialogBuilder.setAdapter(adapter) { dialog, which ->
        timeTravelCapsule.selectState(which)
    }

    alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
        dialog.dismiss()
    }

    alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
        dialog.dismiss()
    }

    val dialog = alertDialogBuilder.create()
    dialog.show()
}

private data class DebugState(val index: Int, val state: UiState) {
    override fun toString(): String {
        return "${index + 1}. $state"
    }
}