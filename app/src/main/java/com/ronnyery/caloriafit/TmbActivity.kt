package com.ronnyery.caloriafit

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_imc.*
import kotlinx.android.synthetic.main.activity_tmb.*

class TmbActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)


        tmbSend.setOnClickListener(sendListener)


    }

    private val sendListener = View.OnClickListener {

        if(!validate())
        {
            Toast.makeText(this, R.string.fields_message, Toast.LENGTH_LONG).show()

            return@OnClickListener
        }

        val height = tmbHeight.text.toString()
        val weight = tmbWeight.text.toString().toDouble()
        val age = tmbAge.text.toString().toInt()


        val tmb = calculateTmb(height.toInt(),weight, age)

        val cal = tmbResponse(tmb)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.tmb_response, tmb))
            .setMessage(getString(R.string.caloria,cal))
            .setNegativeButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->

                dialog.dismiss()
            })
            .setPositiveButton(R.string.save) { dialog, which ->

                val helper = SqlHelper.getInstance(this)
                val calcId = helper.addItem(SqlHelper.TYPE_TMB, tmb)

                if (calcId > 0)
                {
                    Toast.makeText(this, R.string.calc_saved, Toast.LENGTH_LONG).show()

                    openListTmbActivity()
                }

            }
            .create()

        alertDialog.show()

        val im: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(tmbWeight.windowToken,0)
        im.hideSoftInputFromWindow(tmbHeight.windowToken,0)
        im.hideSoftInputFromWindow(tmbAge.windowToken,0)



    }

    private fun calculateTmb(height: Int, weight: Double, age: Int): Double
    {
        return 66 + (weight * 13.8) + (5 * height) - (6.8 * age)

    }


    private fun openListTmbActivity()
    {
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", SqlHelper.TYPE_TMB)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when(item!!.itemId)
        {
            R.id.menu_list -> openListTmbActivity()
        }
        return super.onOptionsItemSelected(item)
    }


    fun validate(): Boolean
    {
        return !tmbHeight.text.toString().startsWith("0")
                &&  !tmbWeight.text.toString().startsWith("0")
                &&  !tmbAge.text.toString().startsWith("0")
                &&  tmbHeight.text.toString().isNotEmpty()
                &&  tmbWeight.text.toString().isNotEmpty()
                &&  tmbAge.text.toString().isNotEmpty()


    }


    fun tmbResponse(tmb:Double): Double
    {

        Log.d("selectd", tmbLifeStyle.selectedItemPosition.toString())
        return when(tmbLifeStyle.selectedItemPosition)
        {
            0 -> tmb * 1.2
            1 -> tmb * 1.375
            2 -> tmb * 1.55
            3 -> tmb * 1.725
            4 -> tmb * 1.9
            else -> 0.0
        }
    }

}
