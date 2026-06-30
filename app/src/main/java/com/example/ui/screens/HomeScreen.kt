package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import com.example.R
import com.example.data.local.entity.AppEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.hapticClickable
import com.example.ui.theme.Emerald
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SharedViewModel

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.viewinterop.AndroidView
import android.net.Uri
import android.widget.VideoView
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

enum class SlideType { VIDEO, IMAGE }

data class StorySlide(
    val type: SlideType,
    val url: String = "",
    val imageRes: Int = 0,
    val title: String,
    val description: String
)

data class Story(
    val id: String,
    val title: String,
    val avatarRes: Int,
    val slides: List<StorySlide>
)

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: SharedViewModel, 
    onNavigateToAppDetails: (String) -> Unit, 
    onNavigateToNotifications: () -> Unit,
    onNavigateToApps: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allApps by viewModel.allApps.collectAsState()
    val uriHandler = LocalUriHandler.current
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { 3 })
    var selectedStoryIndex by remember { mutableStateOf<Int?>(null) }
    
    val sampleStories = remember {
        listOf(
            Story(
                id = "1",
                title = "AI Chatbot",
                avatarRes = R.drawable.img_app_icon,
                slides = listOf(
                    StorySlide(
                        type = SlideType.VIDEO,
                        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                        title = "Conversational AI Builder",
                        description = "Deploy robust local or cloud Gemini chat agents in minutes."
                    ),
                    StorySlide(
                        type = SlideType.IMAGE,
                        imageRes = R.drawable.img_hero_banner,
                        title = "Stunning Dashboards",
                        description = "Pre-loaded with dark mode glassmorphic templates and analytics."
                    )
                )
            ),
            Story(
                id = "2",
                title = "Crypto Pro",
                avatarRes = R.drawable.img_onboarding,
                slides = listOf(
                    StorySlide(
                        type = SlideType.VIDEO,
                        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                        title = "Secure Crypto Terminal",
                        description = "Track decentralized exchanges and hardware wallets on one screen."
                    ),
                    StorySlide(
                        type = SlideType.IMAGE,
                        imageRes = R.drawable.img_app_icon,
                        title = "Realtime Analytics",
                        description = "Beautiful custom charts using high performance native engines."
                    )
                )
            ),
            Story(
                id = "3",
                title = "E-Comm UI",
                avatarRes = R.drawable.img_hero_banner,
                slides = listOf(
                    StorySlide(
                        type = SlideType.IMAGE,
                        imageRes = R.drawable.img_onboarding,
                        title = "Full E-Commerce Stack",
                        description = "Beautiful client application integrated with a comprehensive admin console."
                    ),
                    StorySlide(
                        type = SlideType.VIDEO,
                        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                        title = "Lightning Fast Flow",
                        description = "Optimized checkout screens to minimize cart abandonment."
                    )
                )
            ),
            Story(
                id = "4",
                title = "Video Studio",
                avatarRes = R.drawable.img_app_icon,
                slides = listOf(
                    StorySlide(
                        type = SlideType.VIDEO,
                        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                        title = "AI Video Editor",
                        description = "Apply rich filters, overlays, and smart audio tracks dynamically."
                    ),
                    StorySlide(
                        type = SlideType.IMAGE,
                        imageRes = R.drawable.img_hero_banner,
                        title = "Unlimited Exports",
                        description = "Zero watermarks and instant offline video rendering libraries."
                    )
                )
            ),
            Story(
                id = "5",
                title = "Smart IoT",
                avatarRes = R.drawable.img_onboarding,
                slides = listOf(
                    StorySlide(
                        type = SlideType.IMAGE,
                        imageRes = R.drawable.img_app_icon,
                        title = "IoT Hardware Hub",
                        description = "Connect and sync home devices over ultra-low latency dashboards."
                    ),
                    StorySlide(
                        type = SlideType.VIDEO,
                        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                        title = "Unified Management",
                        description = "Get detailed analytics and automation controls for any smart device."
                    )
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        while(true) {
            kotlinx.coroutines.delay(3000)
            val nextPage = (pagerState.currentPage + 1) % 3
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "SMH Tech Apps",
                        fontWeight = FontWeight.Black,
                        color = Emerald,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Premium Ready Codebase",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
            },
            actions = {
                // Left Side: Circular Image button displaying the APK Android logo (R.drawable.img_app_icon)
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .hapticClickable {
                            // Professional click action
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "APK logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                    )
                }

                // Right Side: Large highly polished Notification Button
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .hapticClickable { onNavigateToNotifications() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = Emerald,
                        modifier = Modifier.size(24.dp)
                    )
                    // Notification red badge indicator
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF1744))
                            .align(Alignment.TopEnd)
                            .offset(x = (-2).dp, y = 2.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        // Instagram Style Stories Horizontal LazyRow
        Text(
            text = "Active Broadcasts",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            items(sampleStories.size) { index ->
                val story = sampleStories[index]
                StoryItem(story = story) {
                    selectedStoryIndex = index
                }
            }
        }

        // Hero Banner Slider
        androidx.compose.foundation.pager.HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(32.dp))
                .background(Slate900)
        ) { page ->
            val images = listOf(R.drawable.img_hero_banner, R.drawable.img_onboarding, R.drawable.img_app_icon)
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "Banner $page",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.6f
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Emerald.copy(alpha = 0.5f), Color.Transparent)
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(24.dp)
                ) {
                    Text(if (page == 0) "Premium Apps" else if (page == 1) "Fast Deployment" else "Ready Source Code", color = Emerald, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (page == 0) "Professional App\nDeployment" else if (page == 1) "Zero Setup Time" else "Complete Access", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Horizontal Scroll
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Latest Apps", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Text("View All", color = Emerald, style = MaterialTheme.typography.labelLarge, modifier = Modifier.hapticClickable { onNavigateToApps() })
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(allApps.take(3)) { app ->
                AppCardHorizontal(app, onNavigateToAppDetails)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Categories", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        val categories = listOf("Finance", "Business", "Earning", "Education", "Productivity", "Tools", "Health")
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(categories) { cat ->
                AssistChip(
                    onClick = { },
                    label = { Text(cat) },
                    colors = AssistChipDefaults.assistChipColors(labelColor = MaterialTheme.colorScheme.onBackground)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        // Features Section
        Text("What You Get", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        val features = listOf(
            "Admin Panel" to "Full control over your app data",
            "User Panel" to "Professional UI/UX for your users",
            "Play Store Ready" to "App public release support",
            "Full Source Code" to "Complete ownership of your project"
        )
        Column(modifier = Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            features.forEach { (title, desc) ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Emerald)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        // Comprehensive Partner Profile Section
        GlassCard(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Section 1: Intro Header with Brand Logo and Verified Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Small circular premium brand logo
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "SMH Tech Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "SMH Tech Pvt Ltd",
                                style = MaterialTheme.typography.titleMedium,
                                color = Emerald,
                                fontWeight = FontWeight.ExtraBold
                            )
                            // Government Verified Badge
                            Box(
                                modifier = Modifier
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                    .background(Emerald.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "VERIFIED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Your Trusted Software Partner!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Kya aap apne business ko online le jana chahte hain par high cost se pareshan hain? Ab chinta chhodiye! SMH Tech Pvt Ltd aapke liye laya hai sabse sasti aur behtareen application development services.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)))
                Spacer(modifier = Modifier.height(20.dp))

                // Section 2: Why Choose Us? (With beautiful, high-quality icon badges)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Why Choose Us?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Features
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Feature 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(Emerald.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Emerald,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Government Verified Company",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Fully registered and legally trusted Private Limited company.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Feature 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(Color(0xFFFFB300).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "₹",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFFFB300),
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Lowest Price Guarantee",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Market se sabse kam daam (Low Price) me premium applications.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Feature 3
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "24/7 Support Chat Available",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Jab bhi zarurat ho, hamari support team aapke sath hai.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)))
                Spacer(modifier = Modifier.height(20.dp))

                // Section 3: Our Verified Track Record
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color(0xFF8E24AA).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFF8E24AA),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Our Verified Track Record",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Metric Cards Grid
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Metric 1: Users
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(androidx.compose.foundation.shape.CircleShape)
                                            .background(Emerald.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = null,
                                            tint = Emerald,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = "100k+",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = Emerald
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Happy Users",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Metric 2: Apps
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(androidx.compose.foundation.shape.CircleShape)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = "90k+",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Apps Selling",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Metric 3: Stars
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(androidx.compose.foundation.shape.CircleShape)
                                            .background(Color(0xFFF57C00).copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFF57C00),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = "4.9/5",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFF57C00)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Star Rating",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Metric 4: Trusted
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(androidx.compose.foundation.shape.CircleShape)
                                            .background(Color(0xFF00796B).copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = null,
                                            tint = Color(0xFF00796B),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = "100%",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF00796B)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Trusted Company",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)))
                Spacer(modifier = Modifier.height(20.dp))

                // Section 4: Connect With Us
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color(0xFFE91E63).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null,
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Connect With Us",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Apna dream app banwane ke liye aaj hi contact karein!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Beautiful interactive social buttons with custom logos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // YouTube Button
                    Button(
                        onClick = { uriHandler.openUri("https://www.youtube.com") },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_youtube),
                                contentDescription = "YouTube",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("YouTube", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Telegram Button
                    Button(
                        onClick = { uriHandler.openUri("https://t.me/smhtech") },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E88E5),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_telegram),
                                contentDescription = "Telegram",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Telegram", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Instagram Button
                    Button(
                        onClick = { uriHandler.openUri("https://instagram.com/smhtech") },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD81B60),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_instagram),
                                contentDescription = "Instagram",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Instagram", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        }
        com.example.ui.components.FloatingChatWidget(modifier = Modifier.align(Alignment.BottomEnd).fillMaxSize())
        
        // Animated Stories Fullscreen Dialog Overlay
        selectedStoryIndex?.let { index ->
            StoryViewerDialog(
                story = sampleStories[index],
                onDismiss = { selectedStoryIndex = null }
            )
        }
    }
}

@Composable
fun StoryItem(
    story: Story,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Story Border")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    // Vibrant neon gradient for the border ring (corner color ring animation)
    val gradientColors = listOf(
        Color(0xFFFF1744), // Neon Red
        Color(0xFFD500F9), // Neon Purple
        Color(0xFF2979FF), // Neon Blue
        Color(0xFF00E676), // Neon Green
        Color(0xFFFFEA00), // Neon Yellow
        Color(0xFFFF1744)  // Neon Red
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(84.dp)
            .hapticClickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            // Animated Glowing Ring
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(rotationAngle)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                colors = gradientColors,
                                center = center
                            ),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
                        )
                    }
            )
            
            // Outer Spacer / Inner Background ring
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                // Main Avatar image
                Image(
                    painter = painterResource(id = story.avatarRes),
                    contentDescription = story.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = story.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun StoryViewerDialog(
    story: Story,
    onDismiss: () -> Unit
) {
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { story.slides.size })
    val coroutineScope = rememberCoroutineScope()
    
    // Automatically transition to the next slide every 7 seconds
    LaunchedEffect(pagerState.currentPage) {
        kotlinx.coroutines.delay(7000)
        if (pagerState.currentPage < story.slides.size - 1) {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        } else {
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Horizontal Pager for swiping slides (supporting both image & video)
            androidx.compose.foundation.pager.HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val slide = story.slides[page]
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (slide.type == SlideType.VIDEO) {
                        StoryVideoPlayer(
                            videoUrl = slide.url,
                            onVideoFinished = {
                                if (pagerState.currentPage < story.slides.size - 1) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                } else {
                                    onDismiss()
                                }
                            }
                        )
                    } else {
                        Image(
                            painter = painterResource(id = slide.imageRes),
                            contentDescription = slide.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Bottom ambient glow gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    // Description text details
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 24.dp, vertical = 64.dp)
                    ) {
                        Text(
                            text = slide.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = slide.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Progress indicators & Top profile bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 32.dp)
            ) {
                // Segmented Stories Progress indicator line
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    story.slides.forEachIndexed { index, _ ->
                        val progress = when {
                            index < pagerState.currentPage -> 1f
                            index > pagerState.currentPage -> 0f
                            else -> {
                                val progressAnim = remember { Animatable(0f) }
                                LaunchedEffect(pagerState.currentPage) {
                                    progressAnim.snapTo(0f)
                                    progressAnim.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(7000, easing = LinearEasing)
                                    )
                                }
                                progressAnim.value
                            }
                        }

                        LinearProgressIndicator(
                            progress = { progress },
                            color = Emerald,
                            trackColor = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                // Header Profile bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = story.avatarRes),
                            contentDescription = story.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = story.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close Story",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoryVideoPlayer(
    videoUrl: String,
    onVideoFinished: () -> Unit
) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(Uri.parse(videoUrl))
                setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = false
                    // Enable hardware acceleration and start playing automatically
                    start()
                }
                setOnCompletionListener {
                    onVideoFinished()
                }
            }
        },
        update = { _ -> },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun AppCardHorizontal(app: AppEntity, onClick: (String) -> Unit) {
    GlassCard(modifier = Modifier.width(200.dp).hapticClickable { onClick(app.id) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(MaterialTheme.shapes.medium).background(MaterialTheme.colorScheme.surfaceVariant)) {
                Image(
                    painter = painterResource(id = R.drawable.img_onboarding),
                    contentDescription = "App Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(app.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(app.category, style = MaterialTheme.typography.bodySmall, color = Emerald)
            Spacer(modifier = Modifier.height(8.dp))
            Text("₹${app.price}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
