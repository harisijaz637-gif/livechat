package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoViewModel

@Composable
fun GoLiveScreen(
    viewModel: PoppoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isBroadcasting by viewModel.isUserBroadcasting.collectAsState()
    val viewerCount by viewModel.broadcastViewerCount.collectAsState()
    val diamondsEarned by viewModel.broadcastDiamonds.collectAsState()
    val durationSeconds by viewModel.broadcastDurationSeconds.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var cameraLensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }
    var roomTitle by remember { mutableStateOf("Party with me! 🌟 Gifting PK") }
    var selectedTag by remember { mutableStateOf("Music & Chat") }
    val tags = listOf("Music & Chat", "PK Battle", "Talent Show", "Late Night", "Gaming")

    var beautyFilterActive by remember { mutableStateOf(true) }
    var micMuted by remember { mutableStateOf(false) }
    var showEndSummaryDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Preview / Simulated Viewfinder
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(cameraLensFacing)
                            .build()
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (e: Exception) {
                            // Handled gracefully
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                update = { previewView ->
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(cameraLensFacing)
                            .build()
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                        } catch (e: Exception) {
                            // Handled
                        }
                    }, ContextCompat.getMainExecutor(context))
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // High fidelity simulated camera preview backdrop
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(PoppoVioletDark, PoppoSurface, Color.Black)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Camera Active (Broadcaster Mode)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap Start Live below to begin streaming to Poppo fans!",
                        fontSize = 12.sp,
                        color = PoppoTextMuted
                    )
                }
            }
        }

        // Broadcaster HUD Overlay
        if (isBroadcasting) {
            // ON-AIR BROADCAST MODE
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(14.dp)
            ) {
                // Top Live Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(PoppoRed)
                        )
                        val mins = durationSeconds / 60
                        val secs = durationSeconds % 60
                        Text(
                            text = "LIVE ${String.format("%02d:%02d", mins, secs)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text("•", fontSize = 10.sp, color = PoppoTextMuted)
                        Text("👁️ $viewerCount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PoppoCyan)
                        Text("•", fontSize = 10.sp, color = PoppoTextMuted)
                        Text("💎 $diamondsEarned", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PoppoGoldLight)
                    }

                    // End Live Button
                    IconButton(
                        onClick = {
                            viewModel.stopUserBroadcast()
                            showEndSummaryDialog = true
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .testTag("btn_end_live")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "End Live", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Broadcaster bottom tools
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Switch Camera
                    IconButton(
                        onClick = {
                            cameraLensFacing = if (cameraLensFacing == CameraSelector.LENS_FACING_FRONT) {
                                CameraSelector.LENS_FACING_BACK
                            } else {
                                CameraSelector.LENS_FACING_FRONT
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(imageVector = Icons.Default.Cameraswitch, contentDescription = "Switch Camera", tint = Color.White)
                    }

                    // Beauty Filter
                    IconButton(
                        onClick = {
                            beautyFilterActive = !beautyFilterActive
                            viewModel.showToast(if (beautyFilterActive) "Beauty Filter ON ✨" else "Filter OFF")
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (beautyFilterActive) PoppoPinkAccent else Color.Black.copy(alpha = 0.6f))
                    ) {
                        Text("✨", fontSize = 18.sp)
                    }

                    // Mute Mic
                    IconButton(
                        onClick = { micMuted = !micMuted },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (micMuted) PoppoRed else Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = if (micMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mic",
                            tint = Color.White
                        )
                    }
                }
            }
        } else {
            // PRE-LIVE SETUP MODE
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                // Top close & camera switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Back", tint = Color.White)
                    }

                    IconButton(
                        onClick = {
                            cameraLensFacing = if (cameraLensFacing == CameraSelector.LENS_FACING_FRONT) {
                                CameraSelector.LENS_FACING_BACK
                            } else {
                                CameraSelector.LENS_FACING_FRONT
                            }
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(imageVector = Icons.Default.Cameraswitch, contentDescription = "Flip", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Setup Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(PoppoOverlayDark)
                        .border(1.dp, PoppoSurfaceBorder, RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "START BROADCAST 🎙️",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = roomTitle,
                        onValueChange = { roomTitle = it },
                        label = { Text("Stream Title", color = PoppoTextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = PoppoPinkAccent,
                            unfocusedBorderColor = PoppoSurfaceBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_stream_title")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Category Tag", fontSize = 12.sp, color = PoppoTextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tags.take(3).forEach { tag ->
                            val isSelected = tag == selectedTag
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) PoppoPinkAccent else PoppoSurfaceVariant)
                                    .clickable { selectedTag = tag }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else PoppoTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.startUserBroadcast(roomTitle, selectedTag)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PoppoPinkAccent),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_start_live_now")
                    ) {
                        Text(
                            text = "START LIVE NOW 🚀",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Broadcast Summary Dialog
        if (showEndSummaryDialog) {
            Dialog(onDismissRequest = {
                showEndSummaryDialog = false
                viewModel.navigateBack()
            }) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = PoppoSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PoppoSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Broadcast Finished!",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Great job! Here are your live statistics:",
                            fontSize = 12.sp,
                            color = PoppoTextMuted
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$viewerCount", fontSize = 20.sp, fontWeight = FontWeight.Black, color = PoppoCyan)
                                Text("Total Viewers", fontSize = 11.sp, color = PoppoTextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("+$diamondsEarned", fontSize = 20.sp, fontWeight = FontWeight.Black, color = PoppoGoldLight)
                                Text("Diamonds Earned", fontSize = 11.sp, color = PoppoTextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val mins = durationSeconds / 60
                                val secs = durationSeconds % 60
                                Text(String.format("%02d:%02d", mins, secs), fontSize = 20.sp, fontWeight = FontWeight.Black, color = PoppoPinkAccent)
                                Text("Duration", fontSize = 11.sp, color = PoppoTextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                showEndSummaryDialog = false
                                viewModel.navigateBack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PoppoVioletPrimary),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("BACK TO HOME", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
