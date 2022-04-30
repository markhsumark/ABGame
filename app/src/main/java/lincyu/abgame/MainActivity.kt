package lincyu.abgame

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.children

class MainActivity : Activity() {

    lateinit var tv_result: TextView
    lateinit var et_input: EditText
    lateinit var btn_calculate: Button
    lateinit var btn_cheat: Button
    lateinit var tv_cheat: TextView
    lateinit var btn_again: Button
    lateinit var sv_record: ScrollView
    lateinit var tv_record: TextView
    var ansTimes: Int = 0

    var isCheating: Boolean = false

    var answer: IntArray = intArrayOf(0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v("MyMessage", "This line was executed!")

        tv_result = findViewById(R.id.tv_result)
        et_input = findViewById(R.id.et_input)
        btn_calculate = findViewById(R.id.btn_calculate)
        btn_cheat= findViewById(R.id.btn_cheat)
        tv_cheat = findViewById(R.id.tv_cheat)
        btn_again = findViewById(R.id.btn_again)
        sv_record = findViewById(R.id.sv_record)
        tv_record = findViewById(R.id.tv_record)

        if (btn_calculate != null) {
            btn_calculate.setOnClickListener{ checkAnswer() }
        } else {
            Log.v("MyMessage", "btn_calculate is null.")
        }

        if (btn_cheat != null) {
            btn_cheat.setOnClickListener{ showCheatAnswer() }
        } else {
            Log.v("MyMessage", "btn_cheat is null.")
        }
        if (btn_again != null) {
            btn_again.setOnClickListener{ resetGame() }
        } else {
            Log.v("MyMessage", "btn_again is null.")
        }

        Log.d("MyMessage", "Answer: " + answer[0]
                + answer[1] + answer[2] + answer[3])
        generateAnswer()
        Log.d("MyMessage", "Answer: ${answer[0]}" +
                "${answer[1]}${answer[2]}${answer[3]}")
    }

    /*
    class MyListener: View.OnClickListener {
        override fun onClick(p0: View?) {
        }
    }
    */

    private fun showCheatAnswer() {
        if(isCheating){
            tv_cheat.text = ""
            isCheating = false
        }  else{
            val ans = "(Answer is: ${answer[0]}" +"${answer[1]}${answer[2]}${answer[3]})"
            Toast.makeText(this,
                ans,
                Toast.LENGTH_LONG).show()
            Log.v("myMessage", "cheat")
            tv_cheat.text = ans
            isCheating = true
        }


    }

    private fun checkAnswer(){
        var input_str: String = et_input.text.toString()

        if (input_str.length != 4 || isRepeat()) {
            Toast.makeText(this,
                R.string.input_error,
                Toast.LENGTH_LONG).show()
            return
        }
        var result = compare(input_str)
        tv_result.text = result
        appendRecord(input_str, result)

        et_input.setText("")
    }

    private fun generateAnswer() {
        for (i in 0..3) {
            var breakflag: Boolean = true
            do {
                breakflag = true
                answer[i] = (Math.random()*10).toInt()
                for (j in 0..(i-1)) {
                    if (answer[i] == answer[j]) {
                        breakflag = false
                        break
                    }
                }
            } while (!breakflag)
        }
    }

    private fun compare(input_str: String): String {
        var guess: Int = 0
        try {
            guess = input_str.toInt()
        } catch (e: NumberFormatException) {
            return getString(R.string.input_error)
        }
        var guessarray: IntArray = intArrayOf(0, 0, 0, 0)
        var x: Int = 10000
        for (i in 0..3) {
            guessarray[i] = (guess%x)/(x/10)
            x = x/10
        }
        var a: Int = 0
        var b: Int = 0

        for (i in 0..3) {
            if (guessarray[i] == answer[i]) {
                a += 1
            }

            for (j in 0..3) {
                if (i == j) continue
                if (guessarray[i] == answer[j]) {
                    b += 1
                }
            }
        }
        return (getString(R.string.result_hint) + " " + a + "A" + b + "B")
    }
    private fun resetGame(){
        generateAnswer()
        tv_result.text = ""
        Toast.makeText(this,
                        "重置",
                        Toast.LENGTH_SHORT)
        tv_cheat.text = ""
        ansTimes = 0
        tv_record.text = ""
    }
    private fun isRepeat(): Boolean{
        var input_str: String = et_input.text.toString()
        var repeatList: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        for(num in input_str){
//            Log.v("", num.digitToInt().toString())
            if(repeatList[num.digitToInt()]>= 1){
                return true
            }
            repeatList[num.digitToInt()] += 1
        }
        return false
    }

    private fun appendRecord(input_str: String, result: String) {
        val recordMsg = ansTimes.toString() + " : " + input_str + result
        ansTimes += 1
        tv_record.append(recordMsg+ "\n")

    }
}
