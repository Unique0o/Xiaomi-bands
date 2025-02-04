package com.example.logifitappp.ui.components.HealthInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.theme.Violet500
import com.example.logifitappp.utils.Constants.bloodTypeTitles
import com.example.logifitappp.utils.Constants.genderTitles
import com.example.logifitappp.utils.Constants.heightTitles
import com.example.logifitappp.utils.Constants.weightTitles

@Composable
fun HealthInfoCard(
    title: String,
    initialValue: String,
    unit: String,
    imageRes: Int,
    onEditClick: () -> Unit,  // Handles special cases
    onSaveClick: (String) -> Unit  // Saves edited values for weight & height
) {
    var isEditing by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(initialValue) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)), // Light gray background
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditing) {
                        TextField(
                            value = textValue,
                            onValueChange = { textValue = it },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        
                        Text(
                            text = when (title) {
                                in weightTitles -> "$textValue $unit"
                                in heightTitles -> "$textValue $unit"
                                in bloodTypeTitles -> "$initialValue $unit"
                                in genderTitles ->  "$initialValue $unit"
                                else -> {""}
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = when (title) {
                                    in weightTitles -> Green298
                                    in heightTitles -> Orange390
                                    in bloodTypeTitles -> Rose120
                                    in genderTitles -> Violet500
                                    else -> Violet500
                                }
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (!isEditing) {
                        IconButton(
                            onClick = {
                                if (title in bloodTypeTitles || title in genderTitles) {
                                    onEditClick()  // Handle externally for blood type & gender
                                } else {
                                    isEditing = true  // Enable editing for weight & height
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                if (isEditing) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = {
                            onSaveClick(textValue)  // Save new value
                            isEditing = false
                        }) {
                            Text("Ok", color = Green298)
                        }
                        TextButton(onClick = { isEditing = false }) {
                            Text("Cancel", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}
