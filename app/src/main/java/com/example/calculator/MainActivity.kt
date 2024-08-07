package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.lang.StringBuilder
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var resultTv: TextView
    private lateinit var solutionTv: TextView
    private lateinit var buttonChangeSign: MaterialButton
    private lateinit var buttonSquareRoot: MaterialButton
    private lateinit var buttonDivide: MaterialButton
    private lateinit var buttonMultiply: MaterialButton
    private lateinit var buttonPlus: MaterialButton
    private lateinit var buttonMinus: MaterialButton
    private lateinit var buttonEquals: MaterialButton
    private lateinit var button0: MaterialButton
    private lateinit var button1: MaterialButton
    private lateinit var button2: MaterialButton
    private lateinit var button3: MaterialButton
    private lateinit var button4: MaterialButton
    private lateinit var button5: MaterialButton
    private lateinit var button6: MaterialButton
    private lateinit var button7: MaterialButton
    private lateinit var button8: MaterialButton
    private lateinit var button9: MaterialButton
    private lateinit var buttonAC: MaterialButton
    private lateinit var buttonDot: MaterialButton

    private var firstValue: Double? = 0.0
    private var secondValue: Double? = null
    private var resultValue: Double? = null
    private var equation: StringBuilder = StringBuilder().append("0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultTv = findViewById(R.id.result_tv)
        solutionTv = findViewById(R.id.solution_tv)
        buttonSquareRoot = findViewById(R.id.button_square_root)
        buttonChangeSign = findViewById(R.id.button_sign_change)
        buttonDivide = findViewById(R.id.button_divide)
        buttonMultiply = findViewById(R.id.button_multiply)
        buttonPlus = findViewById(R.id.button_plus)
        buttonMinus = findViewById(R.id.button_minus)
        buttonEquals = findViewById(R.id.button_equals)
        button0 = findViewById(R.id.button_zero)
        button1 = findViewById(R.id.button_1)
        button2 = findViewById(R.id.button_2)
        button3 = findViewById(R.id.button_3)
        button4 = findViewById(R.id.button_4)
        button5 = findViewById(R.id.button_5)
        button6 = findViewById(R.id.button_6)
        button7 = findViewById(R.id.button_7)
        button8 = findViewById(R.id.button_8)
        button9 = findViewById(R.id.button_9)
        buttonAC = findViewById(R.id.button_ac)
        buttonDot = findViewById(R.id.button_dot)

        buttonSquareRoot.setOnClickListener(this)
        buttonChangeSign.setOnClickListener(this)
        buttonDivide.setOnClickListener(this)
        buttonMultiply.setOnClickListener(this)
        buttonPlus.setOnClickListener(this)
        buttonMinus.setOnClickListener(this)
        buttonEquals.setOnClickListener(this)
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        buttonAC.setOnClickListener(this)
        buttonDot.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val button = view as MaterialButton
        val buttonText = button.text.toString()

        when (button.id) {
            R.id.button_ac -> clearAll()
            R.id.button_sign_change -> onPlusMinusClicked()
            R.id.button_square_root -> calculateSquareRoot()
            R.id.button_equals -> performCalculation()
            R.id.button_plus, R.id.button_minus, R.id.button_multiply, R.id.button_divide -> setOperation(buttonText)
            else -> handleInput(buttonText)
        }

        solutionTv.text = equation.toString()

        val finalResult = getResult(equation.toString())
        if (finalResult != "Err") {
            resultTv.text = finalResult
        }
    }

    private fun setOperation(operator: String) {
        if (isNewExpression) {
            isNewExpression = false
            equation.clear().append(resultTv.text.toString())
        }
        if (equation.isNotEmpty() && !equation.endsWith(" ")) {
            equation.append(" $operator ")
        }
    }

    private fun clearAll() {
        firstValue = 0.0
        secondValue = null
        resultValue = null
        equation.clear().append("0")
        updateDisplay()
    }

    private fun onPlusMinusClicked() {
        val operators = listOf('+', '-', '*', '/')
        val equationStr = equation.toString()

        val lastOperatorIndex = equationStr.indexOfLast { it in operators }

        if (lastOperatorIndex == -1) {
            if (equation.startsWith('-')) {
                equation.deleteCharAt(0)
            } else {
                equation.insert(0, '-')
            }
        } else {
            val afterOperatorIndex = lastOperatorIndex + 1

            var pos = afterOperatorIndex
            while (pos < equationStr.length && equationStr[pos] == ' ') {
                pos++
            }

            if (pos < equationStr.length && equationStr[pos] == '-') {
                equation.deleteCharAt(pos)
            } else {
                equation.insert(pos, '-')
            }
        }

        updateDisplay()
    }


    private fun calculateSquareRoot() {
        if (equation.isNotEmpty() && !equation.endsWith(" ")) {
            val value = equation.toString().toDoubleOrNull()
            if (value != null) {
                if (value < 0) {
                    showNegativeSquareRootError()
                } else {
                    val squareRoot = sqrt(value)
                    equation.clear().append(squareRoot)
                    updateDisplay()
                }
            } else {
                showInvalidInputError()
            }
        }
    }

    private fun showNegativeSquareRootError() {
        Log.d("MainActivity", "Showing negative square root error")

        resultTv.text = getString(R.string.error_negative_square_root)
        solutionTv.text = equation.toString()
        Log.d("MainActivity", "After setting error, resultTv: ${resultTv.text}, solutionTv: ${solutionTv.text}")

    }

    private fun showInvalidInputError() {
        resultTv.text = getString(R.string.error_invalid_input)
        solutionTv.text = equation.toString()
    }

    private fun showDivisionByZeroError() {
        resultTv.text = getString(R.string.error_division_by_zero)
        solutionTv.text = null
    }

    private var isNewExpression: Boolean = false

    private fun performCalculation() {
        if (equation.isNotEmpty() && !equation.endsWith(" ")) {
            val finalResult = getResult(equation.toString())
            if (finalResult != "Err") {
                resultTv.text = finalResult
                equation.clear().append(finalResult)
                Log.d("MainActivity", "Performing calculation, solutionTv: ${solutionTv.text}, resultTv: ${resultTv.text}")
                updateDisplay()
                isNewExpression = true
            }
        }
    }

    private fun handleInput(input: String) {
        if (isNewExpression) {
            if (input in listOf("+", "-", "*", "/")) {
                isNewExpression = false
            } else {
                equation.clear()
                isNewExpression = false
            }
        }

        if (equation.startsWith("0") && input != ".") {
            equation.deleteCharAt(0)

        }

        val lastNumber = equation.split(Regex("[+\\-*/]")).last()
        if (input == "." && lastNumber.contains(".")) {
            return
        }

        equation.append(input)
        Log.d("MainActivity", "Handling input, solutionTv: ${solutionTv.text}, resultTv: ${resultTv.text}")
        updateDisplay()
    }


    private fun updateDisplay() {
        solutionTv.text = equation.toString()
    }

    private fun getResult(data: String): String {
        return try {
            val context: org.mozilla.javascript.Context = org.mozilla.javascript.Context.enter()
            context.optimizationLevel = -1
            val scriptable: org.mozilla.javascript.Scriptable = context.initStandardObjects()
            val finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null)
            if (finalResult is Double && finalResult.isInfinite()) {
                showDivisionByZeroError()
                return "Err"
            }
            var resultString = finalResult.toString()
            if (resultString.endsWith(".0")) {
                resultString = resultString.replace(".0", "")
            }
            resultString
        } catch (e: Exception) {
            "Err"
        }
    }
}
