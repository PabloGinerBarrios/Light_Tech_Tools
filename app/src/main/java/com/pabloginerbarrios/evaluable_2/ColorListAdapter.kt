package com.pabloginerbarrios.evaluable_2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.graphics.Color
import androidx.core.content.ContextCompat

class ColorListAdapter(context: Context, private val colorList: MutableList<ColorData>): ArrayAdapter<ColorData>(context, R.layout.item_color, colorList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // Si convertView es nulo, inflamos la vista y creamos un ViewHolder
            val inflater = LayoutInflater.from(context)
            rowView = inflater.inflate(R.layout.item_color, parent, false)

            viewHolder = ViewHolder().apply {
                colorSquare = rowView.findViewById(R.id.colorSquare)
                colorName = rowView.findViewById(R.id.colorName)
                colorCode = rowView.findViewById(R.id.colorCode)
                colorDate = rowView.findViewById(R.id.colorDate)
            }

            // Asociamos el ViewHolder a la vista con el método setTag
            rowView.tag = viewHolder
        } else {
            // Si convertView no es nulo, recuperamos el ViewHolder de la vista reciclada
            rowView = convertView
            viewHolder = rowView.tag as ViewHolder
        }

        val colorData = getItem(position)

        viewHolder.colorSquare.setBackgroundColor(Color.parseColor(colorData?.color))
        viewHolder.colorName.text = colorData?.name
        viewHolder.colorCode.text = colorData?.color
        viewHolder.colorDate.text = colorData?.saveDate

        if (colorData?.isSelected == true) {
            // Cambiar la apariencia cuando está seleccionado
            rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
        } else {
            // Restaurar la apariencia predeterminada
            rowView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }

        rowView.setOnClickListener {
            colorData?.toggleSelection()
            notifyDataSetChanged()
        }

        return rowView
    }

    fun getSelectedColors(): List<ColorData> {
        return colorList.filter { it.isSelected }
    }

    fun removeSelectedColors(databaseHandler: DatabaseHandler) {
        val selectedColors = getSelectedColors()

        for(selectedColor in selectedColors) {
            databaseHandler.deleteColor(selectedColor)
        }

        colorList.removeAll(selectedColors)
        notifyDataSetChanged()
    }
}

private class ViewHolder {
    lateinit var colorSquare: View
    lateinit var colorName: TextView
    lateinit var colorCode: TextView
    lateinit var colorDate: TextView
}