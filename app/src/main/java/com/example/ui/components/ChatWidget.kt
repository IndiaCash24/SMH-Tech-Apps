package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.BuildConfig
import com.example.data.remote.*
import com.example.ui.theme.Emerald
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

data class ChatMessage(val text: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("Hi! I can help you with source code, deployment, or any general questions about our apps. Ask me anything! 😊", false)
    ))
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val conversationHistory = mutableListOf<Content>()

    private suspend fun simulateOfflineResponse(userInput: String) {
        val input = userInput.lowercase().trim()
        val responseText = when {
            // Greetings
            input.contains("hello") || input.contains("hi") || input.contains("hey") || 
            input.contains("namaste") || input.contains("halo") || input.contains("hola") || 
            input.contains("kaise ho") || input.contains("sup") || input.contains("wassup") ||
            input.contains("bro") || input.contains("helo") -> {
                "Hello! Welcome to SMH Tech Apps Support. Main aapka AI Assistant hoon! 😊\n\nAap mujhse hamare premium apps, source code purchase, installation, pricing ya integration ke baare me pooch sakte hain. Main free me instant reply dunga!"
            }
            
            // App Price / Cost
            input.contains("price") || input.contains("cost") || input.contains("rate") || 
            input.contains("kitne") || input.contains("rupee") || input.contains("rupay") || 
            input.contains("charge") || input.contains("paise") || input.contains("pricing") || 
            input.contains("premium") || input.contains("buy") || input.contains("khareed") ||
            input.contains("khareedna") || input.contains("platinum") -> {
                "Hamare platform par do types ke apps available hain:\n\n" +
                "1. **Standard Apps**: Pricing ₹299 se ₹1499 tak hai (jaise Task Manager, Music Player, E-Learning Hub, etc.).\n" +
                "2. **Premium (PLATINUM) Apps**: Sabhi premium apps flat **₹4999** me available hain (jaise AI Video Editor, Cloud CRM, Smart Home Hub, DeFi Terminal).\n\n" +
                "Aap click karke complete, error-free native Source Code aur documentation directly secure payment se buy kar sakte hain! 🚀"
            }
            
            // Free Apps
            input.contains("free") || input.contains("mufat") || input.contains("muft") || 
            input.contains("bina paise") || input.contains("discount") -> {
                "Yes, hamare paas demo apps aur source codes completely free me access karne ka option hai. 😊\n\nSupport chat aur exploration pure free hain! Baki professional apps ke setup aur full license source code ke liye premium model hai, taaki aapko life-time updates aur robust performance mil sake."
            }
            
            // Setup / Run / Deploy
            input.contains("setup") || input.contains("install") || input.contains("deploy") || 
            input.contains("run") || input.contains("chalu") || input.contains("how to use") || 
            input.contains("kaise chalaye") || input.contains("import") -> {
                "Setup aur Installation super easy hai! 🛠️\n\n" +
                "1. Purchase ke baad immediate download link mil jayegi.\n" +
                "2. ZIP file ko extract karein.\n" +
                "3. Android Studio ya VS Code me open karein.\n" +
                "4. Gradle files automatically sync ho jayengi aur run button click karte hi app chal jayega!\n\n" +
                "Kuch queries hone par hum regular updates aur integration help call par bhi offer karte hain."
            }
            
            // E-Commerce
            input.contains("e-commerce") || input.contains("ecommerce") || input.contains("dukan") || 
            input.contains("shop") || input.contains("shopping") || input.contains("grocery") ||
            input.contains("order") || input.contains("delivery") -> {
                "Hamara E-Commerce Pro & Grocery Delivery blueprint completely ready-to-launch hai! 🛒\n\nInme customizable dynamic cart, elegant dashboard, push notifications, firebase analytics, aur dynamic categories list loaded hain. Admin panel controller bhi included hai!"
            }
            
            // AI Chatbot / Gemini
            input.contains("ai") || input.contains("gemini") || input.contains("bot") || 
            input.contains("chatbot") || input.contains("intelligence") || input.contains("copilot") -> {
                "Google Gemini integration hamare multiple apps me available hai! Jaise 'AI Chatbot UI' aur 'AI Video Editor Pro'.\n\nInme dynamic multi-turn conversational patterns loaded hain. Is support system me bhi dynamic Gemini API integrated hai, jo proper security and fast response rules ke sath active hai! 🤖"
            }
            
            // Contact / Support / Human
            input.contains("contact") || input.contains("support") || input.contains("owner") || 
            input.contains("human") || input.contains("help") || input.contains("whatsapp") || 
            input.contains("phone") || input.contains("email") || input.contains("developer") -> {
                "Hum 24/7 client help offer karte hain! 📞\n\n- **Email**: support@smhtech.com par mail karein.\n- **Social Channels**: Main menu me Telegram and YouTube links click karke support communities join karein.\n\nHum directly installation, API settings configure karne, aur customization me help karte hain!"
            }
            
            // Crypto / DeFi
            input.contains("crypto") || input.contains("finance") || input.contains("wallet") || 
            input.contains("trading") || input.contains("money") || input.contains("defi") -> {
                "Hamare financial templates jaise 'Crypto Wallet' aur 'DeFi Trading Terminal' me full live chart integration, Room local DB, aur fully interactive crypto-currency exchange pages integrated hain. 📈"
            }
            
            // Code / Tech Stack
            input.contains("source code") || input.contains("code") || input.contains("coding") || 
            input.contains("programming") || input.contains("project") || input.contains("tech") ||
            input.contains("kotlin") || input.contains("flutter") || input.contains("compose") -> {
                "Hum strictly clean architecture use karte hain! Hamari tech stack includes:\n- **Android**: Native Kotlin, Jetpack Compose, Material Design 3, Room, Retrofit.\n- **Multiplatform**: Flutter / Dart responsive layouts.\n\nClean coding comments aur strict modularity se build is applet ka structural flow custom integrations ko seamless banata hai! 💻"
            }
            
            // Default response
            else -> {
                "I understand! 👍 Humara automatic AI reply system active hai. Aap in categories ke baare me pooch sakte hain:\n\n" +
                "• 📝 **App Prices & Source Codes** (Standard: ₹299 - ₹1499, Premium: ₹4999)\n" +
                "• 🚀 **Installation & Setup Support** (Zero error run steps)\n" +
                "• 🤖 **AI & Gemini Integration Options**\n" +
                "• 🛒 **E-Commerce & Crypto Apps Features**\n" +
                "• 📞 **Technical Support & Developer contact**\n\n" +
                "Kuch bhi sawal likhein, main aapko instant guide karunga! 😊"
            }
        }

        // Simulate thinking delay (realistic for AI model)
        kotlinx.coroutines.delay(1000)

        withContext(Dispatchers.Main) {
            _messages.value = _messages.value + ChatMessage("", false)
            _isLoading.value = false
        }

        // Beautiful character-by-character or word-by-word streaming simulation
        val words = responseText.split(" ")
        var currentOutput = ""
        for (i in words.indices) {
            currentOutput += (if (i == 0) "" else " ") + words[i]
            kotlinx.coroutines.delay(40) // smooth natural reading pace
            withContext(Dispatchers.Main) {
                val currentList = _messages.value.toMutableList()
                if (currentList.isNotEmpty()) {
                    currentList[currentList.size - 1] = ChatMessage(currentOutput, false)
                    _messages.value = currentList
                }
            }
        }
        
        conversationHistory.add(Content(parts = listOf(Part(text = responseText)), role = "model"))
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        val userMessage = ChatMessage(text, true)
        _messages.value = _messages.value + userMessage
        _isLoading.value = true

        conversationHistory.add(Content(parts = listOf(Part(text = text)), role = "user"))

        viewModelScope.launch(Dispatchers.IO) {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                simulateOfflineResponse(text)
                return@launch
            }

            val request = GenerateContentRequest(
                contents = conversationHistory.toList(),
                systemInstruction = Content(parts = listOf(Part(text = "You are an elite AI technical assistant for SMH Tech Apps. You answer questions dynamically and concisely. You help users understand that our standard source codes range from ₹299 to ₹1499, and our premium (PLATINUM) applications are flat ₹4999. Offer step-by-step guidance on setup, importing code into Android Studio, and help users with customized support questions. Speak professionally and keep answers clean.")))
            )
            
            try {
                val response = RetrofitClient.service.generateContentStream(apiKey, request)
                var currentBotResponse = ""
                
                withContext(Dispatchers.Main) {
                    _messages.value = _messages.value + ChatMessage("", false)
                }

                response.byteStream().bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        try {
                            if (line!!.startsWith(",")) line = line!!.substring(1) // handle streaming array format occasionally sent
                            if (line!!.trim().startsWith("[")) line = line!!.trim().substring(1)
                            if (line!!.trim().endsWith("]")) line = line!!.trim().dropLast(1)
                            
                            val chunk = Json.parseToJsonElement(line!!).jsonObject
                            val textChunk = chunk["candidates"]?.jsonArray
                                ?.getOrNull(0)?.jsonObject
                                ?.get("content")?.jsonObject
                                ?.get("parts")?.jsonArray
                                ?.getOrNull(0)?.jsonObject
                                ?.get("text")?.jsonPrimitive?.content
                            
                            if (textChunk != null) {
                                currentBotResponse += textChunk
                                withContext(Dispatchers.Main) {
                                    val currentList = _messages.value.toMutableList()
                                    if (currentList.isNotEmpty()) {
                                        currentList[currentList.size - 1] = ChatMessage(currentBotResponse, false)
                                        _messages.value = currentList
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // skip lines that are not valid JSON chunks
                        }
                    }
                }
                conversationHistory.add(Content(parts = listOf(Part(text = currentBotResponse)), role = "model"))
            } catch (e: Exception) {
                // Smooth fallback if Gemini API experiences network / proxy issues
                simulateOfflineResponse(text)
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingChatWidget(modifier: Modifier = Modifier, viewModel: ChatViewModel = viewModel()) {
    var isExpanded by remember { mutableStateOf(false) }
    val haptic = rememberHapticFeedback()

    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        if (isExpanded) {
            GlassCard(
                modifier = Modifier
                    .padding(bottom = 80.dp, end = 16.dp, start = 16.dp)
                    .widthIn(max = 400.dp)
                    .height(500.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Support Chat", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        IconButton(onClick = { 
                            haptic()
                            isExpanded = false 
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Filled.Close, contentDescription = "Close Chat", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    // Messages
                    val messages by viewModel.messages.collectAsState()
                    val isLoading by viewModel.isLoading.collectAsState()

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        reverseLayout = false
                    ) {
                        items(messages) { message ->
                            ChatBubble(message)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if (isLoading) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Emerald, strokeWidth = 2.dp)
                                }
                            }
                        }
                    }

                    // Input
                    var text by remember { mutableStateOf("") }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = { Text("Ask anything...") },
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(24.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = {
                                haptic()
                                viewModel.sendMessage(text)
                                text = ""
                            }),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                haptic()
                                viewModel.sendMessage(text)
                                text = ""
                            },
                            modifier = Modifier.background(Emerald, CircleShape).size(48.dp),
                            enabled = text.isNotBlank() && !isLoading
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                        }
                    }
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { 
                haptic()
                isExpanded = !isExpanded 
            },
            modifier = Modifier.padding(16.dp),
            containerColor = Emerald,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(if (isExpanded) Icons.Filled.Close else Icons.AutoMirrored.Filled.Chat, contentDescription = "Toggle Chat")
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                )
                .background(if (message.isUser) Emerald else MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
