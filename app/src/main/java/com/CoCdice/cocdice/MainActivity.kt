package com.CoCdice.cocdice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var line: Int = 0
    private val maxLine: Int = 10
    private val list: List<String> = mutableListOf("")
    private var switch: Boolean = false

    /* ダイスを振る */
    private fun diceRoll(quantity: Int ,type: Int ,string: String = ""): String {
        var rand = 0
//        val history = textView.text
        var extreme = ""
        var san = ""
        var text = ""
        var value: Int

        if (quantity > 1) {
            for (i in 1..quantity) {
                rand += Random.nextInt(type) + 1
            }
        } else {
            rand = Random.nextInt(type) + 1
            if (type == 100 && string != "SAN Check" && switch == false) { // 1D100時、クリファン表記
                if (rand <= 5){
                    extreme = " Critical!"
                } else if (rand >=96) {
                    extreme = " Fumble..."
                }
            }
        }
        if (line > maxLine) {
            // １行削除
        }

        if (switch == true) { // SAN値減少
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

        if (string == "SAN Check" && switch == false) { // SANチェック
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

        text = "$text $quantity D $type -> $rand $san $extreme \n"
        textView.text = text

        return text
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide() // タイトルバー非表示

        // 初期表示
        textView.text = "iaia Haster \n"
        numberPickerSan.minValue = 0
        numberPickerSan.maxValue = 99
        numberPickerSan.value = 80
        numberPickerQuantity.minValue = 1
        numberPickerQuantity.maxValue = 10
        numberPickerQuantity.value = 1
        numberPickerType.minValue = 1
        numberPickerType.maxValue = 100
        numberPickerType.value = 6

        var history = textView.text

//        switchSan.isChecked


        buttonClear.setOnClickListener {
            textView.text = ""
        }

        // ダイスロール
        button1d4.setOnClickListener {
            history = textView.text.toString()
            history = "$history" + diceRoll(1,4)
            textView.text = history
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