package com.example.microfinancieraapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.ui.semantics.setSelection
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.microfinancieraapp.databinding.ActivityAccountStatusBinding
import java.util.Calendar

class AccountStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountStatusBinding
    private lateinit var transactionsAdapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMonthSpinner()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupMonthSpinner() {
        val months = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonths.adapter = adapter

        // Select the current month
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        binding.spinnerMonths.setSelection(currentMonth)
    }

    private fun setupRecyclerView() {
        transactionsAdapter = TransactionsAdapter(getSampleTransactions())
        binding.rvTransactions.apply {
            layoutManager = LinearLayoutManager(this@AccountStatusActivity)
            adapter = transactionsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@AccountStatusActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun getSampleTransactions(): List<Transaction> {
        return listOf(
            Transaction("15/04/2025", "Pago mensual préstamo #12345", 350.00, true),
            Transaction("10/04/2025", "Comisión por retraso", 25.00, true),
            Transaction("15/03/2025", "Pago mensual préstamo #12345", 350.00, true),
            Transaction("15/02/2025", "Pago mensual préstamo #12345", 350.00, true)
        )
    }

    private fun setupClickListeners() {
        binding.btnMakePayment.setOnClickListener {
            startActivity(Intent(this, MakePaymentActivity::class.java))
        }

        binding.ivAlert.setOnClickListener {
            showAlertsDialog()
        }
    }

    private fun showAlertsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Alertas")
            .setMessage("No tienes pagos pendientes por el momento.")
            .setPositiveButton("Aceptar", null)
            .show()
    }
}

// Model class for transactions
data class Transaction(
    val date: String,
    val description: String,
    val amount: Double,
    val isPaid: Boolean
)

// Adapter for the RecyclerView
class TransactionsAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.tvDate.text = transaction.date
        holder.tvDescription.text = transaction.description
        holder.tvAmount.text =
            if (transaction.isPaid) "-${transaction.amount}" else "+${transaction.amount}"
        holder.tvAmount.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (transaction.isPaid) android.R.color.holo_red_dark else android.R.color.holo_green_dark
            )
        )

        if (transaction.isPaid) {
            holder.tvStatus.text = "COMPLETADO"
            holder.tvStatus.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.bg_status_paid
            )
        } else {
            holder.tvStatus.text = "PENDIENTE"
            holder.tvStatus.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.bg_status_pending
            )
        }
    }

    override fun getItemCount() = transactions.size
}