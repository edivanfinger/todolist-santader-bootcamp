package br.com.dio.todolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.todolist.R
import br.com.dio.todolist.databinding.ItemTaskBinding
import br.com.dio.todolist.model.Task
import br.com.dio.todolist.viewmodel.TaskViewModel

class TaskListAdapter (
    private val ctx: MainActivity,
    private val dataList: MutableList<Task>,
    private val viewModel: TaskViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listenerEdit : (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_task, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val ivMore: AppCompatImageView = itemView.findViewById(R.id.iv_more)

        fun bind(position: Int) {
            val item = dataList[position]
            title.text = item.title
            date.text = "${item.date} ${item.hour}"
            ivMore.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Task) {
            val popupMenu = PopupMenu(ivMore.context, ivMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> viewModel.deleteTask(item.id!!)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }

}
