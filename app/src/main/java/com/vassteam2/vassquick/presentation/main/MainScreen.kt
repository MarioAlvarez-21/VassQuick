package com.vassteam2.vassquick.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.navigation.AppScreens
import com.vassteam2.vassquick.ui.theme.VassQuickTheme
import java.util.Locale

@Composable
fun MainScreen(navController: NavHostController) {
    VassQuickTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
        ) {

            ConstraintLayout(Modifier.fillMaxSize()) {
                val (image, text1, text2, button) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top, 150.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                Text(
                    text = stringResource(R.string.welcome_to_vassquick),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 40.sp,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .constrainAs(text1) {
                            top.linkTo(image.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                Text(
                    text = stringResource(R.string.let_s_connect),
                    fontSize = 17.sp,
                    modifier = Modifier.constrainAs(text2) {
                        top.linkTo(text1.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                Button(
                    onClick = { navController.navigate(AppScreens.LoginScreen.route) },
                    shape = RoundedCornerShape(20),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp)
                        .height(56.dp)
                        .constrainAs(button) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Text(
                        text = stringResource(R.string.login).uppercase(Locale.getDefault()),
                        fontSize = 17.sp
                    )
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {


}
