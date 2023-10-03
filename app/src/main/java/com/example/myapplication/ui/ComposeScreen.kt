package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.ui.model.MarvelDisplayData
import com.example.myapplication.viewmodel.MarvelViewModel
import timber.log.Timber

@Composable
fun screen(flowViewModel: FlowViewModel = viewModel()) {
    SideEffect {
        Timber.d("COMPOSE SCREEN")
    }

//    var state = flowViewModel.collectFlow.collectAsStateWithLifecycle()
//    TextOne(text = state.value.firstOrNull().toString())

    Column {
        for ((id, flow) in flowViewModel.map) {
            val s = flow.collectAsStateWithLifecycle()
            TextOne(text = "$id ${s.value.joinToString()}")
        }
    }
}

@Composable
fun TextOne(text: String) {
    SideEffect {
        Timber.d("COMPOSE TEXTONE")
    }
    Text(text = text)
}

@Composable
fun ComposeScreen(
    modifier: Modifier = Modifier,
    marvelViewModel: MarvelViewModel = viewModel()
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }

    val viewState =
        marvelViewModel.viewState.collectAsState()

    Column {
        if (viewState.value.loading) {
            Text(text = "LOADING.........")
        }

        TextButton(onClick = { marvelViewModel.getChars() }) {
            Text(text = "Load List")
        }

        TextField(
            value = viewState.value.query,
            onValueChange = { newText: String ->
                marvelViewModel.searchCharacters(newText)
            },
            label = { Text("Enter something") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        CharacterList(list = viewState.value.list)

        Counter()
        Counter1()
        f()
    }
}

@Preview
@Composable
fun f() {
    var value by remember { mutableStateOf(1) }

    Column {
        CompositionLocalProvider(localTest1 provides value) {
            SideEffect { firstRecompositionCounter.increment() }

            MyRow {
                Text("Text is ${localTest1.current}")
            }
        }

        Button(
            modifier = Modifier.testTag("button"),
            onClick = { value++ }
        ) {
            Text("Increment")
        }
    }

}

private val localTest1: ProvidableCompositionLocal<Int> = compositionLocalOf {
    0
}

private val localTest2: ProvidableCompositionLocal<Int> = staticCompositionLocalOf {
    0
}

val firstRecompositionCounter = RecompositionCounter()
val secondRecompositionCounter = RecompositionCounter()

@Composable
private fun MyRow(content: @Composable () -> Unit) {
    Row {
        SideEffect { secondRecompositionCounter.increment() }
        content()
    }
}

@Immutable
class RecompositionCounter {
    private var count = 0

    fun increment() {
        count++
    }

    fun count() = count
    fun reset() {
        count = 0
    }
}


@Composable
fun Counter() {
    // Define a state variable for the count
    val count = remember { mutableIntStateOf(0) }

    // Use SideEffect to log the current value of count
    SideEffect {
        // Called on every recomposition
        Timber.d("Count0 is ${count.intValue}")
    }

    println("recomposed outer")

    Column {
        Button(onClick = { count.intValue++ }) {
            Text("Increase Count")
        }

        // With every state update, text is changed and recomposition is triggered
//        Column {
//            println("recomposed inner")
//            Text("Counter ${count.intValue}")
//        }
        InnerCounter(count = count)
    }
}

@Composable
fun InnerCounter(count: MutableIntState) {
    // With every state update, text is changed and recomposition is triggered
    Column {
        Text("Counter ${count.intValue}")
    }
}

@Composable
fun Counter1() {
    // Define a state variable for the count
    val count = remember { mutableStateOf(0) }

    // Use SideEffect to log the current value of count
    SideEffect {
        // Called on every recomposition
        Timber.d("Count1 is ${count.value}")
    }

    println("0 inner")
    Column {
        Button(onClick = { count.value++ }) {
            // This recomposition doesn't trigger the outer side effect
            // every time button has been tapped
            println("1 inner")
            Text("Increase Count ${count.value}")
        }
    }
}


@Composable
fun CharacterList(modifier: Modifier = Modifier, list: List<MarvelDisplayData>) {
    LazyColumn(modifier) {
        items(items = list, key = {
            it.name
        }) {
            Column {
                Text(text = it.name, style = TextStyle(fontSize = 20.sp))
                AsyncImage(
                    model = it.imageUrl,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    Text("Hi there!", Modifier.padding(top = 32.dp))
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = layout { measurable, constraints ->
    // Measure the composable
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.placeRelative(0, placeableY)
    }
}
