package com.quintonpyx.healthapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.quintonpyx.healthapp.helper.GeneralHelper
import java.util.*




class FoodAdapter(val context: Context, val foodList: ArrayList<Food>): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private lateinit var database: DatabaseReference
    private lateinit var user: FirebaseUser
  
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
//        Log.d("TAG","RAN")

      
        val view:View = LayoutInflater.from(context).inflate(R.layout.food_layout,parent,false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFood = foodList[position]
        holder.txtName.text = currentFood.name
        holder.txtCalorie.text = currentFood.calorie.toString() +"kcal"

        if(currentFood.name=="Searching..."){
            // remove add button and calorie text during search
            holder.btnAdd.visibility = View.GONE
            holder.txtCalorie.visibility = View.GONE
        }else{
            // reset add button and calorie text visibility for first food
            holder.btnAdd.visibility = View.VISIBLE
            holder.txtCalorie.visibility = View.VISIBLE
            holder.btnAdd.setOnClickListener {
                user = FirebaseAuth.getInstance().currentUser!!
                database = Firebase.database.reference
                // generate random key
                val key = database.push().key
                if (key != null) {
                    database.child("userFood").child(key).setValue(UserFood(key,user.uid,currentFood.name,currentFood.calorie,GeneralHelper.getTodayDate()))
                    Toast.makeText(this.context,"Food has been added successfully",Toast.LENGTH_SHORT).show()
                }
            // add food

        }



        }
        holder.itemView.setOnClickListener{
//           // add food to user
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class FoodViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txt_name)
        val txtCalorie = itemView.findViewById<TextView>(R.id.txt_calorie)
        val btnAdd = itemView.findViewById<TextView>(R.id.btnAdd)

    }


}