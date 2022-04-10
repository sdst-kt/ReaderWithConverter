package ex.sadisst.readerwithconverter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ex.sadisst.readerwithconverter.databinding.ListStorageBinding
import java.io.File

class ListStorageActivity : AppCompatActivity() {
    private lateinit var binding: ListStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ListStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val path = intent.getStringExtra("path")
        val root = File(path)

        val filesAndFolders = root.listFiles()
        if (filesAndFolders == null || filesAndFolders.isEmpty()) {
            binding.tvNoFiles.visibility = View.VISIBLE
            return
        } else {
            binding.tvNoFiles.visibility = View.GONE
        }

        val rv = binding.rvBooks
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = ListStorageAdapter(applicationContext, filesAndFolders)
    }
}