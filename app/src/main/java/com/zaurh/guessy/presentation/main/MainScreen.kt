package com.zaurh.guessy.presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zaurh.guessy.domain.model.ResultModel
import com.zaurh.guessy.ui.theme.AgifyTheme
import com.zaurh.guessy.ui.theme.Blush
import com.zaurh.guessy.ui.theme.ButtonBlue
import com.zaurh.guessy.ui.theme.ErrorRed
import com.zaurh.guessy.ui.theme.FemalePink
import com.zaurh.guessy.ui.theme.FieldTint
import com.zaurh.guessy.ui.theme.Ink
import com.zaurh.guessy.ui.theme.InkSoft
import com.zaurh.guessy.ui.theme.MaleBlue
import com.zaurh.guessy.ui.theme.Mint
import com.zaurh.guessy.ui.theme.Sky
import java.util.Locale

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel()
) {
    MainScreen(
        state = viewModel.state,
        onIntent = viewModel::onIntent
    )
}

private enum class Gender { MALE, FEMALE }

private fun ResultModel.genderOrNull(): Gender? = when (gender?.lowercase()) {
    "male" -> Gender.MALE
    "female" -> Gender.FEMALE
    else -> null
}

private val Gender.color get() = if (this == Gender.MALE) MaleBlue else FemalePink

/** What the card is showing, derived from [MainState]. */
private sealed interface CardContent {
    data object Idle : CardContent
    data object Loading : CardContent
    data class Error(val message: String) : CardContent
    data class Unknown(val name: String) : CardContent
    data class Guess(val result: ResultModel, val gender: Gender) : CardContent
}

private fun MainState.toCardContent(): CardContent {
    val result = result
    return when {
        query.isBlank() -> CardContent.Idle
        error.isNotEmpty() -> CardContent.Error(error)
        result == null -> if (isLoading) CardContent.Loading else CardContent.Idle
        else -> result.genderOrNull()
            ?.let { CardContent.Guess(result, it) }
            ?: CardContent.Unknown(result.name)
    }
}

@Composable
fun MainScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit
) {
    val content = state.toCardContent()
    var showAbout by rememberSaveable { mutableStateOf(false) }

    // Personal touch: the bottom of the gradient warms up to pink for female guesses.
    val bottomColor by animateColorAsState(
        targetValue = if ((content as? CardContent.Guess)?.gender == Gender.FEMALE) Blush else Sky,
        animationSpec = tween(durationMillis = 600),
        label = "backgroundBottom"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Mint, bottomColor)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter your name\nI will try to find your gender.",
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center,
                color = Ink
            )
            Spacer(Modifier.height(24.dp))
            ResultCard(content = content, isRefreshing = state.isLoading)
            Spacer(Modifier.height(36.dp))
            NameField(
                query = state.query,
                onQueryChange = { onIntent(MainIntent.QueryChanged(it)) }
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { showAbout = true },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ButtonBlue,
                    contentColor = Ink
                ),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
            ) {
                Text(
                    text = "About",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }
    }

    if (showAbout) {
        AboutDialog(onDismiss = { showAbout = false })
    }
}

@Composable
private fun ResultCard(content: CardContent, isRefreshing: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp),
        shape = MaterialTheme.shapes.large,
        backgroundColor = Color.White,
        elevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = content,
                // Only animate when the kind of content (or the guessed name) changes.
                contentKey = { if (it is CardContent.Guess) it.result.name to it.gender else it::class },
                transitionSpec = {
                    (fadeIn(tween(250)) + scaleIn(initialScale = 0.92f, animationSpec = tween(250)))
                        .togetherWith(fadeOut(tween(150)))
                },
                contentAlignment = Alignment.Center,
                label = "cardContent"
            ) { target ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 244.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (target) {
                        CardContent.Idle -> IdleContent()
                        CardContent.Loading -> CircularProgressIndicator(color = Sky)
                        is CardContent.Error -> MessageContent(
                            title = "Oops!",
                            message = target.message,
                            titleColor = ErrorRed
                        )
                        is CardContent.Unknown -> MessageContent(
                            title = "Hmm…",
                            message = "I don't know \"${target.name}\" yet.\nPlease enter a valid name."
                        )
                        is CardContent.Guess -> GuessContent(
                            result = target.result,
                            gender = target.gender,
                            modifier = Modifier.alpha(if (isRefreshing) 0.55f else 1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IdleContent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "?",
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h1.copy(
                brush = Brush.linearGradient(listOf(MaleBlue, FemalePink))
            )
        )
        Text(
            text = "Type a name below",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = InkSoft
        )
    }
}

@Composable
private fun MessageContent(
    title: String,
    message: String,
    titleColor: Color = Ink
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = title, fontSize = 34.sp, fontWeight = FontWeight.Bold, color = titleColor)
        Text(
            text = message,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Ink
        )
    }
}

@Composable
private fun GuessContent(
    result: ResultModel,
    gender: Gender,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = result.name.replaceFirstChar { it.titlecase(Locale.getDefault()) },
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = gender.name.lowercase(),
            fontSize = 48.sp,
            lineHeight = 52.sp,
            fontWeight = FontWeight.Bold,
            color = gender.color
        )
        Spacer(Modifier.height(8.dp))
        GenderSymbol(gender = gender, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(20.dp))
        ProbabilityBar(probability = result.probability.toFloat(), color = gender.color)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Probability: ${result.probability}",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Ink
        )
    }
}

/** Animated bar that fills up to the guess probability. */
@Composable
private fun ProbabilityBar(probability: Float, color: Color) {
    var started by rememberSaveable { mutableStateOf(false) }
    val fill by animateFloatAsState(
        targetValue = if (started) probability.coerceIn(0f, 1f) else 0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 120f),
        label = "probability"
    )
    androidx.compose.runtime.LaunchedEffect(Unit) { started = true }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(8.dp)
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fill)
                .background(color, RoundedCornerShape(50))
        )
    }
}

/** ♂ / ♀ drawn on a canvas so it renders the same on every device (no emoji fonts). */
@Composable
private fun GenderSymbol(gender: Gender, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.minDimension
        val stroke = s * 0.11f
        val style = Stroke(width = stroke, cap = StrokeCap.Round)
        when (gender) {
            Gender.MALE -> {
                val radius = s * 0.26f
                val center = Offset(s * 0.38f, s * 0.62f)
                drawCircle(Ink, radius, center, style = style)
                val start = Offset(center.x + radius * 0.72f, center.y - radius * 0.72f)
                val tip = Offset(s * 0.9f, s * 0.1f)
                drawLine(Ink, start, tip, stroke, StrokeCap.Round)
                drawLine(Ink, tip, Offset(tip.x - s * 0.3f, tip.y), stroke, StrokeCap.Round)
                drawLine(Ink, tip, Offset(tip.x, tip.y + s * 0.3f), stroke, StrokeCap.Round)
            }
            Gender.FEMALE -> {
                val radius = s * 0.24f
                val center = Offset(s / 2, s * 0.3f)
                drawCircle(Ink, radius, center, style = style)
                drawLine(Ink, Offset(s / 2, center.y + radius), Offset(s / 2, s * 0.95f), stroke, StrokeCap.Round)
                drawLine(Ink, Offset(s * 0.32f, s * 0.77f), Offset(s * 0.68f, s * 0.77f), stroke, StrokeCap.Round)
            }
        }
    }
}

@Composable
private fun NameField(query: String, onQueryChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.body1.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        placeholder = { Text("Enter name…", fontSize = 18.sp, color = InkSoft) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Ink) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Clear", tint = Ink)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrectEnabled = false,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Ink,
            backgroundColor = FieldTint,
            cursorColor = Ink,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        title = { Text("About Guessy", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
        text = {
            Text(
                text = "Type your name, or anyone's, and Guessy will guess the gender " +
                    "behind it.\n\nGuesses come from genderize.io statistics and are just " +
                    "for fun, so don't take them too seriously!",
                fontSize = 16.sp,
                color = Ink
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it", fontWeight = FontWeight.SemiBold, color = Sky)
            }
        }
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun MainScreenMalePreview() {
    AgifyTheme {
        MainScreen(
            state = MainState(
                query = "Mark",
                result = ResultModel(count = 1, gender = "male", name = "Mark", probability = 1.0)
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun MainScreenFemalePreview() {
    AgifyTheme {
        MainScreen(
            state = MainState(
                query = "Mercedes",
                result = ResultModel(count = 1, gender = "female", name = "Mercedes", probability = 0.99)
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun MainScreenIdlePreview() {
    AgifyTheme {
        MainScreen(state = MainState(), onIntent = {})
    }
}
