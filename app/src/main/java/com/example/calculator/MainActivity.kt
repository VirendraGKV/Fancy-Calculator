package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var lastNumberic=false
    var stateError=false
    var lastDot=false

    private lateinit var expression: Expression


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun onAllClearClick(view: View) {
        binding.inputTv.text=""
        binding.resultTv.text=""
        stateError=false
        lastDot=false
        lastNumberic=false
        binding.resultTv.visibility=View.GONE
    }

    fun onDigitClick(view: View) {
        if(stateError){
            binding.inputTv.text=(view as Button).text
            stateError=false
        }else{
            binding.inputTv.append((view as Button).text)
        }
        lastNumberic=true
        onEqual()
    }



    fun onOperatorClick(view: View) {
        if(!stateError && lastNumberic){
            binding.inputTv.append((view as Button).text)
            lastDot=false
            lastNumberic=false
            onEqual()
        }
    }

    fun onClearClick(view: View) {
        binding.inputTv.text=""
        lastNumberic=false
    }

    @SuppressLint("SuspiciousIndentation")
    fun onBackClick(view: View) {
        binding.inputTv.text=binding.inputTv.text.toString().dropLast(1)
        try{
            val lastChar=binding.inputTv.text.toString().last()
                if(lastChar.isDigit()){
                    onEqual()
                }
        }catch (e:Exception){
            binding.resultTv.text=""
            binding.resultTv.visibility=View.GONE
            Log.e("last char error",e.toString())
        }
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.inputTv.text=binding.resultTv.text.toString().drop(1)

    }



    fun onEqual(){
        if(lastNumberic && !stateError){
            val txt=binding.inputTv.text.toString()
            expression=ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE

                binding.resultTv.text = "=" + result.toString()
            }
            catch (ex: ArithmeticException){
                Log.e("evaluate error",ex.toString())
                binding.resultTv.text="Error"

                stateError=true
                lastNumberic=false
            }
        }
    }
}