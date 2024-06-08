package com.example.goalkeeper.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goalkeeper.style.AppStyles

data class PieChartData(
    val color: Color,
    val value: Int,
    val label: String
)

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    radius: Float = 500f,
    innerRadius: Float = 250f,
    data: List<PieChartData>,
    centerText: String = ""
) {

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    val dataList by remember {
        mutableStateOf(data)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            val totalValue = data.sumOf { it.value }

            val anglePerValue = 360f / totalValue
            var startAngle = 0f

            dataList.forEach {
                val scale = 1f
                val angleToDraw = it.value * anglePerValue
                scale(scale) {
                    drawArc(
                        color = it.color,
                        startAngle = startAngle,
                        sweepAngle = angleToDraw,
                        useCenter = true,
                        size = Size(
                            width = radius * 2f,
                            height = radius * 2f
                        ),
                        topLeft = Offset(
                            x = (width - radius * 2f) / 2f,
                            y = (height - radius * 2f) / 2f
                        )
                    )
                    startAngle += angleToDraw
                }
                var rotateAngle = startAngle - angleToDraw / 2f - 90f
                var factor = 1f
                if (rotateAngle > 90f) {
                    rotateAngle = (rotateAngle + 180f) % 360f
                    factor = -0.92f
                }

                val percentage = ((it.value / totalValue.toFloat()) * 100).toInt()

                // 할 일 그리기
                drawContext.canvas.nativeCanvas.apply {
                    if (percentage > 3) {
                        rotate(rotateAngle) {
                            drawText(
                                "$percentage %",
                                circleCenter.x,
                                circleCenter.y + (radius - (radius - innerRadius) / 2f) * factor,
                                Paint().apply {
                                    textSize = 20.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = Color.White.toArgb()
                                }
                            )
                        }
                    }
                }

            }

            // 안쪽 원 그리기
            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    circleCenter.x,
                    circleCenter.y,
                    innerRadius,
                    Paint().apply {
                        color = Color(0xFFB5838D).toArgb()
                    }
                )
            }
        }

        // 안쪽 원 중앙에 centerText
        Text(
            centerText,
            modifier = Modifier
                .width(Dp(innerRadius / 1.5f))
                .padding(20.dp),
            style = AppStyles.korTitleStyle,
            textAlign = TextAlign.Center
        )
    }
}