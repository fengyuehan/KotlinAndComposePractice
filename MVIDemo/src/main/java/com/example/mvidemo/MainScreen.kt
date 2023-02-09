package com.example.mvidemo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    Column {
        Toolbar(timeMachine = viewModel.timeMachine)
        when {
            state.isLoading -> {
                ContentWithProgress()
            }
            state.data.isNotEmpty() -> {
                MainScreenContent(
                    state.data,
                    state.isShowAddDialog,
                    onItemCheckedChanged = {
                            index, isChecked -> viewModel.onItemCheckedChanged(index, isChecked)
                    },
                    onAddButtonClick = { viewModel.changeAddDialogState(true) },
                    onDialogDismissClick = { viewModel.changeAddDialogState(false) },
                    onDialogOkClick = { text -> viewModel.addNewItem(text) },
                )
            }
        }
    }

}

@Composable
fun MainScreenContent(
    todos: List<MainScreenItem>,
    isShowAddDialog: Boolean,
    onItemCheckedChanged: (Int, Boolean) -> Unit,
    onAddButtonClick: () -> Unit,
    onDialogDismissClick: () -> Unit,
    onDialogOkClick: (String) -> Unit,
) {
    Box {
        LazyColumn(content = {
            itemsIndexed(todos) { index, item ->
                when (item) {
                    is MainScreenItem.MainScreenAddButtonItem -> {
                        AddButton(onAddButtonClick)
                    }
                    is MainScreenItem.MainScreenTodoItem -> {
                        TodoListItem(item, onItemCheckedChanged, index)
                    }
                }
            }
        })

        if (isShowAddDialog) {
            AddNewItemDialog(onDialogDismissClick, onDialogOkClick)
        }
    }
}

@Composable
fun AddNewItemDialog(onDialogDismissClick: () -> Unit, onDialogOkClick: (String) -> Unit) {
    var text by remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = {  },text =  {
        TextField(value = text, onValueChange = {
            newText ->
            text = newText
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Blue,
            disabledIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Blue,
            backgroundColor = Color.LightGray,
        ))
    },
        confirmButton = {
            Button(
                onClick = { onDialogOkClick(text) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
            ) {
                Text(text = "Ok", style = TextStyle(color = Color.White, fontSize = 12.sp))
            }
        }, dismissButton = {
            Button(
                onClick = onDialogDismissClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
            ) {
                Text(text = "Cancel", style = TextStyle(color = Color.White, fontSize = 12.sp))
            }
        }
        )
}

@Composable
fun AddButton(onAddButtonClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.icon_plus_blue),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.Center)
                .clickable (onClick = onAddButtonClick)
        )
    }
}

@Composable
fun TodoListItem(
    item: MainScreenItem.MainScreenTodoItem,
    onItemCheckedChanged: (Int, Boolean) -> Unit,
    index: Int
) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onItemCheckedChanged(index, !item.isChecked) },
            colors = CheckboxDefaults.colors(Color.Blue)
        )
        Text(
            text = item.text, modifier = Modifier
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {

                        }
                    )
                },
            style = TextStyle(color = Color.Black, fontSize = 14.sp)
        )
    }
}

@Composable
private fun ContentWithProgress() {
    Surface(color = Color.LightGray) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun Toolbar(timeMachine: TimeCapsule<MainScreenState>) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .background(color = Color.Blue)
            .fillMaxWidth()
            .debugInputPointer(LocalContext.current, timeMachine),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp),
            text = stringResource(id = R.string.main_screen_title),
            color = Color.White,
            fontSize = 18.sp,
            style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Start,
        )
    }
}