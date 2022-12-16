package com.example.recipes_book.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.R
import com.example.recipes_book.adapters.viewHolders.StepViewHolder
import com.example.recipes_book.models.retrofit.AnalyzedInstruction
import com.example.recipes_book.models.retrofit.Step

class StepAdapter(private val steps: List<Step>, private val color: Int): RecyclerView.Adapter<StepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cooking_step_item, parent, false)

        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.stepNumber.text = steps[position].number.toString()
        holder.stepNumber.setBackgroundColor(color)
        holder.step.text = steps[position].step
    }

    override fun getItemCount(): Int = steps.size
}