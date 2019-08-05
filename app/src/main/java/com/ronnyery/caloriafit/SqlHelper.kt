package com.ronnyery.caloriafit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class SqlHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME , null, DB_VERSION)
{



    companion object
    {
        var INSTANCE: SqlHelper? = null
        val DB_NAME = "fitness.bd"
        val DB_VERSION = 1
        val TYPE_IMC = "imc"
        val TYPE_TMB = "tmb"


        @Synchronized
        fun getInstance(context: Context): SqlHelper
        {
            if (INSTANCE == null)
                INSTANCE = SqlHelper(context.applicationContext)
            return INSTANCE!!
        }

    }





    override fun onCreate(db: SQLiteDatabase?)
    {
        db?.execSQL("CREATE TABLE calc (id INTEGER PRIMARY KEY, type_calc TEXT, res DECIMAL, created_date DATETIME)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
    }

    fun getRegisterByType(type:String): MutableList<Register>
    {
        val list: MutableList<Register> = mutableListOf()

        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM calc WHERE type_calc = ?", arrayOf(type))

        try
        {
            if(cursor.moveToFirst())
            {

                do {

                    val register = Register(
                        id = cursor.getString(cursor.getColumnIndex("id")),
                        type = cursor.getString(cursor.getColumnIndex("type_calc")),
                        response = cursor.getDouble(cursor.getColumnIndex("res")),
                        createDate = cursor.getString(cursor.getColumnIndex("created_date"))
                    )

                    list.add(register)
                }while (cursor.moveToNext())

                db.setTransactionSuccessful()


            }

        }
        catch (e: Exception)
        {
            Log.e("Teste", e.message)
        }
        finally
        {
            if (cursor != null && !cursor.isClosed)
            {
                cursor.close()
            }

        }

        return list


    }




    fun addItem(type: String, response: Double): Long
    {
        val db = writableDatabase
        db.beginTransaction()

        var calcId: Long = 0

        try {

            val values = ContentValues()

            values.put("type_calc", type)
            values.put("res", response)

            val dateFormat: SimpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss",Locale("pt","BR"))
            val format = dateFormat.format(Calendar.getInstance().time)
            values.put("created_date", format)

           calcId = db.insertOrThrow("calc",null,values)

            db.setTransactionSuccessful()

        }catch (e: Exception)
        {
            Log.e("Teste", e.message,e)
        }
        finally
        {
            db.endTransaction()
        }

        return calcId


    }


    fun deleteItem(id: String)
    {

        val db = readableDatabase
//
////        // Define 'where' part of query.
//        val selection = "id LIKE ?"
////        // Specify arguments in placeholder order.
//        val selectionArgs = arrayOf(id)
////        // Issue SQL statement.
//        val deletedRows = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs)


        try {

            db.delete("calc", "id = ?", arrayOf(id))
        }
        catch (e:Exception)
        {
            Log.d("error delete", e.message)
        }
        finally {
            db.close()
        }


    }

    inner class Register
        (val type: String = "",
         val response:Double = 0.0,
         val createDate:String = "",
         val id:String = "0")
    {
        override fun toString(): String {
            return "Register(response=$response)"
        }


    }



}