package com.ronnyery.caloriafit

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_list_calc.*


class ListCalcActivity : AppCompatActivity(),clickItemList
{

    lateinit var adapter: AdapterList

    lateinit var list: MutableList<SqlHelper.Register>

    override fun onClick(id: String, position: Int)
    {

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_delete))
            .setMessage(getString(R.string.mesage_delete))
            .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, which ->

                dialog.dismiss()
            })
            .setPositiveButton(R.string.delete) { dialog, which ->
                val helper = SqlHelper.getInstance(this)
                helper.deleteItem(id)

                list.removeAt(position)
                adapter.notifyItemRemoved(position)

                Toast.makeText(this,"Regitro deletado com sucesso", Toast.LENGTH_SHORT).show()

            }
            .create()

        alertDialog.show()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)

        if(intent.extras != null)
        {
            val type = intent.getStringExtra("type")

            val sqlHelper = SqlHelper.getInstance(this)

            list = sqlHelper.getRegisterByType(type)

            adapter = AdapterList(list, this,this)

            val decorate = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)

            recyclerViewList.addItemDecoration(decorate)
//
            recyclerViewList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
//
            recyclerViewList.adapter = adapter
        }
    }


    class AdapterList(val list: MutableList<SqlHelper.Register>, val context: Context, val listener: clickItemList): RecyclerView.Adapter<AdapterList.RegisterViewHolder>()
    {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RegisterViewHolder
        {
            val view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,p0,false)

            return RegisterViewHolder(view)
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        override fun onBindViewHolder(p0: RegisterViewHolder, p1: Int)
        {

            p0.textView.text = p0.itemView.context.getString(R.string.result,list[p1].response,list[p1].createDate)

            p0.itemView.setOnClickListener {

                listener.onClick(list[p1].id, p1)


            }
        }


        class RegisterViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)
        {
            var textView: TextView = itemView.findViewById(android.R.id.text1) as TextView
        }


    }


}
