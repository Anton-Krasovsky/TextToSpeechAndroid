package by.tigertosh.texttospeechandroid

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import by.tigertosh.texttospeechandroid.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        textToSpeech = TextToSpeech(this, this)

        setupListener()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale("ru", "RU"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Язык не поддерживается", Toast.LENGTH_SHORT)
                    .show()
            } else {
                textToSpeech.setSpeechRate(0.75f)
                binding.speakButton.isEnabled = true
            }
        }
    }

    private fun speakText(text: String) {
        when {
            text.matches(Regex(".*[a-zA-Z].*")) -> {
                textToSpeech.setLanguage(Locale.US)
            }
            text.matches(Regex(".*[а-яА-ЯёЁ].*")) -> {
                textToSpeech.setLanguage(Locale("ru", "RU"))
            }
            text.matches(Regex(".*[\\u0600-\\u06FF].*")) -> {
                textToSpeech.setLanguage(Locale("ar", "SA"))
            }
            else -> {
                textToSpeech.setLanguage(Locale.US)
            }
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun setupListener() = with(binding) {
        speakButton.setOnClickListener {
            speakText(editText.text.toString())
        }

        deleteButton.setOnClickListener {
            editText.text.clear()
        }
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}