package com.ronnyery.caloriafit

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItens = arrayListOf<MainItem>()

        mainItens.add(
            MainItem(1,
                R.drawable.ic_announcement_black,
                R.string.imc,
                colorValue = R.color.colorPrimary))

        mainItens.add(
            MainItem(2,
                R.drawable.ic_announcement_black,
                R.string.tmd,
                colorValue = R.color.colorAccent))


        val adapter = MainAdapter(mainItens,this)


        val linearLayoutCompat = GridLayoutManager(this, 2)

        recyclerView.layoutManager = linearLayoutCompat

        recyclerView.adapter = adapter






    }

     private class MainAdapter(val list:ArrayList<MainItem>, val context: Context): RecyclerView.Adapter<MainViewHolder>(), MainViewHolder.OnItemClickListener
     {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MainViewHolder
        {
            val view = LayoutInflater.from(context).inflate(R.layout.item_main,p0,false)
            return MainViewHolder(view)

        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        override fun onBindViewHolder(p0: MainViewHolder, p1: Int)
        {
            val mainItem = list[p1]
            p0.bind(mainItem,this)
        }

         override fun onClick(position: Int)
         {
             val mainItem = this.list[position]
             when(mainItem.id)
             {
                 1 ->
                 {
                     context.startActivity(Intent(context,ImcActivity::class.java))
                     return
                 }

                 2 ->
                 {
                     context.startActivity(Intent(context,TmbActivity::class.java))

                     return
                 }
             }
         }

    }

    private class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        interface OnItemClickListener
        {
            fun onClick(position:Int)
        }

        fun bind(mainItem: MainItem, listener: OnItemClickListener)
        {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, mainItem.colorValue))
            imgViewMain.setImageResource(mainItem.imgId)
            imgViewText.text = itemView.context.getString(mainItem.textId)

            itemView.setOnClickListener {
                listener.onClick(adapterPosition)

            }
        }

         val imgViewMain: ImageView
         val imgViewText: TextView


        init {
            imgViewMain = itemView.findViewById(R.id.item_main_img)
            imgViewText = itemView.findViewById(R.id.item_mais_text)
        }
    }
}
