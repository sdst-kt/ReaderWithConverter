package ex.sadisst.readerwithconverter

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import ex.sadisst.readerwithconverter.databinding.BookMainLayoutBinding
import java.io.File
import java.io.FileReader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

class BookReaderActivity() : AppCompatActivity() {
    private lateinit var binding: BookMainLayoutBinding
    private lateinit var book: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("ReaderWithConverter_Debug", "BookReaderActivity - onCreate")

        binding = BookMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bookMainText.text = "WATTAFAK"

        book = File(intent.extras!!.getString("filepath"))
        Log.d("ReaderWithConverter_Debug", "filetype: " + book.extension)

        when (book.extension) {
            "pdf" -> {
                PDFBoxResourceLoader.init(applicationContext)

                val parcelablePDFDoc = PDDocument.load(book)
                if(!parcelablePDFDoc.isEncrypted) {
                    val stripper = PDFTextStripper()
                    binding.bookMainText.text = stripper.getText(parcelablePDFDoc)
                }

                parcelablePDFDoc.close()
            }
            "json", "fb2", "mobi", "epub" -> {
                val fileReader = FileReader(book)
                binding.bookMainText.text = fileReader.readText()
            }
            else -> {
                binding.bookMainText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                Log.d("ReaderWithConverter_Debug", "cannot parse")
            }
        }
    }
}