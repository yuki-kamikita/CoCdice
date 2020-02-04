package com.CoCdice.cocdice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var switch: Boolean = false

    /* ダイスを振る */
    private fun diceRoll(quantity: Int ,type: Int ,string: String = ""): String {
        var rand = 0
        val history = textView.text
        var extreme = ""
        var san = ""
        var text = ""
        var value: Int

        if (quantity > 1) { // 複数ダイス
            for (i in 1..quantity) {
                rand += Random.nextInt(type) + 1
            }
        } else { // ダイス1つ
            rand = Random.nextInt(type) + 1
            if (type == 100 && string != "SAN Check" && switch == false) { // 1D100時、クリファン表記
                if (rand <= 5){
                    extreme = " Critical!"
                } else if (rand >=96) {
                    extreme = " Fumble..."
                }
            }
        }

        if (switch == true && string != "SAN Check") { // SAN値減少
            value = numberPickerSan.value - rand
            san = "/ " + numberPickerSan.value.toString() + " - $rand = $value"
            text = "SAN Decrease"

            if (value < 0) { // SAN0
                value = 0
                extreme = "Character Lost"
            } else if (rand >= numberPickerSan.value*0.2) {
                extreme = "不定の狂気"
            } else if (rand >= 5) {
                extreme = "一時的狂気判定"
            }

            numberPickerSan.value = value

            switchSan.setChecked(false)
            switch = false
        }

        if (string == "SAN Check") { // SANチェック
            san = "≦ " + numberPickerSan.value
            if (rand <= numberPickerSan.value) {
                san += " Success!"
            } else {
                san += " Failure..."
                switchSan.setChecked(true)
                switch = true
            }
            text = string
        }

        text = "$text $quantity D $type -> $rand $san $extreme \n$history"
        textView.text = text

        return text
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide() // タイトルバー非表示

        // 初期表示
        val string = listOf("Ia! Ia! Cthulhu fhtagn!\n","Ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn.\n","The window! The window!\n")
        textView.text = "Ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn.\n" // ランダム表示にしようかな
        numberPickerSan.minValue = 0
        numberPickerSan.maxValue = 99
        numberPickerSan.value = 80
        numberPickerQuantity.minValue = 1
        numberPickerQuantity.maxValue = 10
        numberPickerQuantity.value = 1
        val data = arrayOf("2", "3", "4", "5", "6", "8", "10", "12", "16", "20", "24", "100")
        numberPickerType.setDisplayedValues(data)
//        numberPickerType.value = 6

        var history = textView.text

        buttonClear.setOnClickListener {
            textView.text = ""
        }

        // ダイスロール
        button1d4.setOnClickListener {
            diceRoll(1,4)
        }
        button1d6.setOnClickListener {
            diceRoll(1,6)
        }
        button1d10.setOnClickListener {
            diceRoll(1,10)
        }
        button1d100.setOnClickListener {
            diceRoll(1,100)
        }

        buttonSan.setOnClickListener {
            diceRoll(1,100, "SAN Check")
        }

        buttonRoll.setOnClickListener {
            diceRoll(numberPickerQuantity.value, numberPickerType.value)
        }

        // トグルスイッチ 手動on/off
        switchSan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) { // switch ON
                switch = true
//                textView.text = "true"
            } else { // switch OFF
                switch = false
//                textView.text = "false"
            }

        }
    }
}

// 多言語対応
//