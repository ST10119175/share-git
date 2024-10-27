import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.adapter.Product
import com.example.adapter.R

class ProductAdapter(private val context: Context, private val productList: ArrayList<Product>) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val product = productList[position]

        val imageView = view.findViewById<ImageView>(R.id.imgproduct)
        val nameView = view.findViewById<TextView>(R.id.Lot)
        val categoryView = view.findViewById<TextView>(R.id.category)

        imageView.setImageResource(product.imageID)
        nameView.text = product.name
        categoryView.text = product.category

        return view
    }
}
