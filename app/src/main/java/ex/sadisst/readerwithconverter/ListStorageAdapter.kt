package ex.sadisst.readerwithconverter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import java.io.File
import java.lang.Exception

class ListStorageAdapter(private var context: Context, private var filesList: Array<File>) :
    Adapter<ListStorageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.tv_filename)
        var imageView: ImageView = itemView.findViewById(R.id.icon_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedFile: File = filesList[position]
        holder.textView.text = selectedFile.name

        if(selectedFile.isFile) holder.imageView.setImageResource(R.drawable.ic_file_48)
        else holder.imageView.setImageResource(R.drawable.ic_folder_48)

        holder.itemView.setOnClickListener {
            when {
                selectedFile.isDirectory -> {
                    Toast.makeText(context,
                        "FOLDER: " + selectedFile.name, Toast.LENGTH_SHORT)
                        .show()

                    val selectThing = Intent(context.applicationContext, ListStorageActivity::class.java)
                    selectThing.putExtra("path", selectedFile.absolutePath)
                    selectThing.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(selectThing)
                }
                selectedFile.isFile -> {
                    Toast.makeText(context,
                        "FILE: " + selectedFile.name, Toast.LENGTH_SHORT)
                        .show()

                    try {
                        val selectThing = Intent(this.context, BookReaderActivity::class.java)
                        selectThing.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        selectThing.putExtra("filepath", selectedFile.absolutePath)
                        context.startActivity(selectThing)
                    } catch (e: Exception) {
                        Log.d("ReaderWithConverter_Debug", e.message.toString())
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return filesList.size
    }
}