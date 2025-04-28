package com.example.agil_financiera

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agil_financiera.adapter.ItemAdapter
import com.example.agil_financiera.databinding.ActivityCustomersBinding
import com.example.agil_financiera.dbHelper.DBhelper

class Customers : AppCompatActivity(){
    private lateinit var dbHelper:DBhelper
    private lateinit var binding:ActivityCustomersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        dbHelper = DBhelper(this)
        viewCustomer()
    }
    private fun viewCustomer(){
        val customersList= dbHelper.getCustomers(this)
        val adapter = ItemAdapter(this,customersList)
        val rv = binding.recycle01
        rv.adapter = adapter
        adapter.setOnItemClickListener(object: ItemAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@Customers,CustomerDetails::class.java)
                intent.putExtra("name",customersList[position].name)
                intent.putExtra("phone",customersList[position].phone)
                intent.putExtra("balance",customersList[position].balance)
                intent.putExtra("email",customersList[position].email)
                intent.putExtra("accNo",customersList[position].AccountNo)
                intent.putExtra("ifsc",customersList[position].ifsc)
                startActivity(intent)
            }
        })
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
    }
}