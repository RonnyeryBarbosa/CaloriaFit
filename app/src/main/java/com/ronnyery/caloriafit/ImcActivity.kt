package com.ronnyery.caloriafit

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_imc.*

class ImcActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        imcSend.setOnClickListener(sendListener)
    }

    private val sendListener = View.OnClickListener {

        if(!validate())
        {
            Toast.makeText(this, R.string.fields_message,Toast.LENGTH_LONG).show()

            return@OnClickListener
        }

        val height = imcHeight.text.toString()
        val weight = imcWeight.text.toString().toDouble()


        val imc = calculateImc(height.toInt(),weight)

        val resId:Int = imcResponse(imc)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.imc_response, imc))
            .setMessage(resId)
            .setNegativeButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->

                dialog.dismiss()
            })
            .setPositiveButton(R.string.save) { dialog, which ->

                val helper = SqlHelper.getInstance(this)
                val calcId = helper.addItem(SqlHelper.TYPE_IMC, imc)

                if (calcId > 0)
                {
                    Toast.makeText(this, R.string.calc_saved, Toast.LENGTH_LONG).show()

                    openListCalcActivity()
                }

            }
            .create()

        alertDialog.show()

        val im: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(imcWeight.windowToken,0)
        im.hideSoftInputFromWindow(imcHeight.windowToken,0)


    }

    private fun openListCalcActivity() {
        val intent = Intent(this, ListCalcActivity::class.java)
        intent.putExtra("type", SqlHelper.TYPE_IMC)

        startActivity(intent)
    }

    @StringRes
    private fun imcResponse(imc: Double): Int
    {
        return when {
            imc < 15 -> R.string.imc_severely_low_weight
            imc < 16 -> R.string.imc_very_low_weight
            imc < 18.5 -> R.string.imc_low_weight
            imc < 25 -> R.string.normal
            imc < 30 -> R.string.imc_high_weight
            imc < 35 -> R.string.imc_so_high_weight
            imc < 40 -> R.string.imc_severely_high_weight
            else -> R.string.imc_extreme_weight
        }

    }



    private fun calculateImc(height: Int, weight: Double): Double
    {
        //peso / altura * altura

        Log.d("PESO", "$height")

        Log.d("PESO", "${((height.toDouble()/100))}")
        return (weight / ((height.toDouble()/100) * (height.toDouble()/100).toDouble()))

    }

    fun validate(): Boolean
    {
        return !imcHeight.text.toString().startsWith("0")
                &&  !imcWeight.text.toString().startsWith("0")
                &&  imcHeight.text.toString().isNotEmpty()
                &&  imcWeight.text.toString().isNotEmpty()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when(item!!.itemId)
        {
            R.id.menu_list -> openListCalcActivity()
        }
        return super.onOptionsItemSelected(item)
    }

}
