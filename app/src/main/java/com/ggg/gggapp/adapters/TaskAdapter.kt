package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.gggapp.R
import com.ggg.gggapp.model.Task
import com.ggg.gggapp.utils.generateInitials

class TaskAdapter(private val context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private lateinit var myListener: OnItemClickListener
    private var tasks: MutableList<Task> = ArrayList()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }
    class TaskViewHolder (listener: OnItemClickListener, itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: TextView? = null
        var creator: TextView? = null
        var executor: TextView? = null
        var deadline: TextView? = null
        var status: TextView? = null
        var updatedAt: TextView? = null

        init {
            header = itemView.findViewById(R.id.header)
            creator = itemView.findViewById(R.id.creator)
            executor = itemView.findViewById(R.id.executor)
            deadline = itemView.findViewById(R.id.deadline)
            status = itemView.findViewById(R.id.status)
            updatedAt = itemView.findViewById(R.id.updatedAt)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            myListener,
            LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        if (tasks.size != 0) {
            tasks.sortedWith(compareByDescending<Task> { task -> task.creation_date}.thenByDescending { task -> task.date_of_update })
            val userDataCreator = tasks[position].creator.userData
            val userDataExecutor = tasks[position].executor.userData
            val creator = "Постановщик:  ${generateInitials(userDataCreator.lastname, userDataCreator.firstname, userDataCreator.middlename)}"
            val executor = "Исполнитель: ${generateInitials(userDataExecutor.lastname, userDataExecutor.firstname, userDataExecutor.middlename)}"
            holder.creator?.text = creator
            holder.executor?.text = executor
            holder.deadline?.text = tasks[position].deadline
            holder.status?.text = tasks[position].status
            holder.updatedAt?.text = if (tasks[position].date_of_update.isNullOrEmpty()){
                tasks[position].creation_date
            } else {
                tasks[position].date_of_update
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTasks(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}