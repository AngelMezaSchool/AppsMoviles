package com.example.agil_financiera.model

data class Customer(
    var name:String,
    var phone:Long,
    var balance:Double,
    var email:String,
    var AccountNo:String,
    var ifsc:String)