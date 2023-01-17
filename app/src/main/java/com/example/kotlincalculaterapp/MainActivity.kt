package com.example.kotlincalculaterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var workingsTV: Text   View
    private lateinit var resultsTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        workingsTV = findViewById(R.id.workingsTV)
        resultsTV = findViewById(R.id.resultsTV)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    workingsTV.append(view.text)

                canAddDecimal = false
            } else
                workingsTV.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        workingsTV.text = ""
        resultsTV.text = ""
    }

    fun backSpaceAction(view: View) {
        val length = workingsTV.length()
        if (length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)
    }

    fun equalsAction(view: View) {
        resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedlist: MutableList<Any>): Float {
        var result = passedlist[0] as Float

        for (i in passedlist.indices) {
            if (passedlist[i] is Char && i != passedlist.lastIndex) {
                val operator = passedlist[i]
                val nextDigit = passedlist[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }


    private fun timesDivisionCalculate(passedlist: MutableList<Any>): MutableList<Any> {
        var list = passedlist
        while (list.contains('X') || list.contains('/') || list.contains('%')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedlist: MutableList<Any>): MutableList<Any> {
        val newlist = mutableListOf<Any>()
        var restartIndex = passedlist.size

        for (i in passedlist.indices) {
            if (passedlist[i] is Char && i != passedlist.lastIndex && i < restartIndex) {
                val operator = passedlist[i]
                val prevDigit = passedlist[i - 1] as Float
                val nextDigit = passedlist[i + 1] as Float
                when (operator) {
                    'X' -> {
                        newlist.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newlist.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    '%' -> {
                        newlist.add(prevDigit % nextDigit)
                        restartIndex = i + 1
                    }else -> {
                        newlist.add(prevDigit)
                        newlist.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newlist.add(passedlist[i])

        }
        return newlist
    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingsTV.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list


    }
}
