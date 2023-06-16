package com.shehaan.tippycally

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INIT_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
//    late initialization
    private lateinit var etBillAmount : EditText
    private lateinit var seekBar : SeekBar
    private lateinit var tvTipPercentLabel : TextView
    private lateinit var tvTipValue : TextView
    private lateinit var tvTotalAmountLabel : TextView
    private lateinit var tvTipDescription : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBillAmount = findViewById(R.id.etBillAmount)
        seekBar = findViewById(R.id.seekBar)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipValue = findViewById(R.id.tvTipValue)
        tvTotalAmountLabel = findViewById(R.id.tvTotalAmountLabel)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        //set up default values
        seekBar.progress = INIT_TIP_PERCENT
        tvTipPercentLabel.text = "$INIT_TIP_PERCENT%"
        updateDescription(INIT_TIP_PERCENT)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                //put the value on the screen
                tvTipPercentLabel.text = "$progress%"
                computeTipAndPercent()
                updateDescription(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBillAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndPercent()
            }
        })
    }

    private fun computeTipAndPercent(){
        if(etBillAmount.text.isEmpty()){
            tvTipValue.text = ""
            tvTotalAmountLabel.text = ""
            return
        }
        val baseValue = etBillAmount.text.toString().toDouble()
        val tipPercent = seekBar.progress

        //calculate the tip
        val finalTip = ((baseValue * tipPercent) / 100)
        val totalValue = finalTip + baseValue

        // update the UI
        tvTipValue.text = "%.2f".format(finalTip)
        tvTotalAmountLabel.text = "%.2f".format(totalValue)
    }

    private fun updateDescription(tipValue : Int){
        val tipDescription = when (tipValue) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipValue.toFloat()  / seekBar.max,
            ContextCompat.getColor(this,R.color.worst_color),
            ContextCompat.getColor(this, R.color.best_color)
        ) as Int

        tvTipDescription.setTextColor(color)
    }
}