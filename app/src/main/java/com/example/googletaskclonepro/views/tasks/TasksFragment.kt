package com.example.googletaskclonepro.views.tasks

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.screenViewModel
import com.example.googletaskclonepro.R
import com.example.googletaskclonepro.databinding.CreateTaskBottomSheetDialogBinding
import com.example.googletaskclonepro.databinding.FragmentTasksBinding
import com.example.googletaskclonepro.databinding.PageSelectorBottomSheetDialogBinding
import com.example.googletaskclonepro.model.task.Task
import com.example.googletaskclonepro.utils.*
import com.example.googletaskclonepro.views.TimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

const val EVENT_ARG_TASK = "event_arg_task"
const val EVENT_ARG_POSITION = "event_arg_position"
const val EVENT_DELETING_TASK = "event_deleting_task"

val categories = arrayOf(
    "Избранные",
    "Мои задачи",
    "Выполненые"
)

class TasksFragment : BaseFragment(), TasksListener, TimePickerDialog.Callbacks {

    class Screen : BaseScreen

    override val viewModel: TasksViewModel by screenViewModel()
    private lateinit var binding: FragmentTasksBinding
    private lateinit var viewPager2Adapter: ViewPager2Adapter
    private lateinit var createTaskDialog: BottomSheetDialog
    private lateinit var pageSelectorDialog: BottomSheetDialog
    private var deletingMode = false
    private var date: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        viewPager2Adapter = ViewPager2Adapter(this)
        binding.categoryViewPager2.adapter = viewPager2Adapter

        TabLayoutMediator(binding.categoryTabLayout, binding.categoryViewPager2) {tab, position ->

            if (position == 0) {
                tab.setIcon(R.drawable.ic_star)
                val tabIconColor = ContextCompat.getColor(requireContext(), R.color.blue)
                @Suppress("DEPRECATION")
                tab.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            } else {
                tab.text = categories[position]
            }
        }.attach()

        createTaskDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        createTaskDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        renderCreateTaskDialog()

        pageSelectorDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        pageSelectorDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        renderPageSelectorDialog()

        binding.createItemFab.setOnClickListener {
            createTaskDialog.show()
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            pageSelectorDialog.show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //FIXME sometimes not working
        setFragmentResultListener(EVENT_DELETING_TASK) { _, bundle ->
            deletingMode = true
            @Suppress("DEPRECATION")
            val task = bundle.getParcelable<Task>(EVENT_ARG_TASK) as Task
            val position = bundle.getInt(EVENT_ARG_POSITION)
            val snackbar = Snackbar.make(view, "Задача удалена", Snackbar.LENGTH_LONG)

            val deletingItemPos: Int? = adapters[position].removeItem(task)

            var flagDeleteTask = true

            snackbar.setAction("Отмена") {
                flagDeleteTask = false
            }
            snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (flagDeleteTask) viewModel.removeTask(task)
                    else {
                        adapters[position].addItem(task, deletingItemPos)
                    }
                    deletingMode = false
                }
            })
            snackbar.show()
        }
    }

    private fun renderCreateTaskDialog() {

        val dialogBinding = CreateTaskBottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        createTaskDialog.setContentView(dialogBinding.root)

        dialogBinding.saveTaskButton.isEnabled = false

        dialogBinding.additionalInfoImageBtn.setOnClickListener {
            dialogBinding.additionalInfoEditText.visibility = View.VISIBLE
        }
        dialogBinding.addInFavouriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            dialogBinding.addInFavouriteCheckBox.setButtonDrawable(
                if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border
            )
        }

        dialogBinding.setTimeImageBtn.setOnClickListener {
            TimePickerDialog().apply {
                setTargetFragment(this@TasksFragment, 0)
                show(this@TasksFragment.parentFragmentManager, "dialog_date")
            }
        }

        dialogBinding.taskTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                dialogBinding.saveTaskButton.isEnabled = s?.isNotBlank() == true
            }
        })

        dialogBinding.saveTaskButton.setOnClickListener {
            val task = Task(
                isCompleted = false,
                text = dialogBinding.taskTitleEditText.text.toString(),
                additionalInfo = dialogBinding.additionalInfoEditText.text.toString(),
                isFavourite = dialogBinding.addInFavouriteCheckBox.isChecked
            )
            date?.let {
                scheduleNotification(task.text, task.additionalInfo)
            }
            viewModel.createTask(task)
            createTaskDialog.dismiss()
        }
        dialogBinding.taskTitleEditText.requestFocus()

        createTaskDialog.setOnDismissListener {
            dialogBinding.apply {
                taskTitleEditText.text.clear()
                additionalInfoEditText.text.clear()
                additionalInfoEditText.visibility = View.GONE
                addInFavouriteCheckBox.isChecked = false
            }
        }

    }

    private fun renderPageSelectorDialog() {
        val dialogBinding = PageSelectorBottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        pageSelectorDialog.setContentView(dialogBinding.root)

        dialogBinding.favouriteTasksButton.setOnClickListener {
            val tab = binding.categoryTabLayout.getTabAt(0)
            tab?.select()
            pageSelectorDialog.dismiss()
        }
        dialogBinding.myTasksButton.setOnClickListener {
            val tab = binding.categoryTabLayout.getTabAt(1)
            tab?.select()
            pageSelectorDialog.dismiss()

        }
        dialogBinding.completedTasksButton.setOnClickListener {
            val tab = binding.categoryTabLayout.getTabAt(2)
            tab?.select()
            pageSelectorDialog.dismiss()
        }
        dialogBinding.createNewListButton.setOnClickListener {
            Toast.makeText(requireContext(), "Для доступа к этой функции оформите подписку", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickTask(task: Task) {
        viewModel.updateTask(task)
    }

    override fun showTaskScreen(task: Task, adapterPosition: Int) {
        viewModel.onShowDetailsCalled(task, adapterPosition)
    }

    override fun onMoveTask(from: Int, to: Int) {
        //move items
    }

    override fun observeData(lifecycleOwner: LifecycleOwner, adapter: TasksAdapter, position: Int) {
        when (position) {
            0 -> viewModel.favouriteTasks.observe(lifecycleOwner) { if(!deletingMode) adapter.tasks = it.toMutableList() }
            1 -> viewModel.tasks.observe(lifecycleOwner) { if(!deletingMode) adapter.tasks = it.toMutableList() }
            2 -> viewModel.completedTask.observe(lifecycleOwner) { if(!deletingMode) adapter.tasks = it.toMutableList() }
        }
    }

    private fun scheduleNotification(title: String, message: String) {
        val intent = Intent(requireContext().applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext().applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(requireContext().applicationContext)

        AlertDialog.Builder(requireContext())
            .setTitle("Notification scheduled")
            .setMessage(
                "Title " + title +
                        "\nMessage: " + message +
                        "\nAt " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Ok", {_,_ ->})
            .show()
    }

    private fun getTime(): Long {

        val time = Calendar.getInstance()
        time.time = date

        val calendar = Calendar.getInstance()
        calendar.set(
            time.get(Calendar.YEAR),
            time.get(Calendar.MONTH),
            time.get(Calendar.DAY_OF_MONTH),
            time.get(Calendar.HOUR),
            time.get(Calendar.MINUTE))
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "Notif chanel"
        val desc = "A description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(chanelId, name, importance)
        channel.description = desc
        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onTimeSelected(date: Date) {
        this.date = date
    }

}