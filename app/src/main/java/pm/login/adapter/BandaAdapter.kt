import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pm.login.R
import pm.login.models.Banda

class BandaAdapter : ListAdapter<Banda, BandaAdapter.BandaViewHolder>(BandaDiffCallback()) {
    // Opcional: sobrescreva submitList para garantir tratamento de lista vazia
    override fun submitList(list: List<Banda>?) {
        super.submitList(list ?: emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BandaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banda, parent, false)
        return BandaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BandaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BandaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(banda: Banda) {
            itemView.findViewById<TextView>(R.id.txtTitulo).text = banda.nome ?: "Nome não disponível"
            itemView.findViewById<TextView>(R.id.txtPreco).text = (banda.preco ?: "Preço não disponível").toString()
            itemView.findViewById<TextView>(R.id.txtDisponibilidade).text =
                "Disponíveis: ${banda.preco ?: 0}"


        }
    }

    class BandaDiffCallback : DiffUtil.ItemCallback<Banda>() {
        override fun areItemsTheSame(oldItem: Banda, newItem: Banda): Boolean {
            return oldItem.id_banda== newItem.id_banda
        }

        override fun areContentsTheSame(oldItem: Banda, newItem: Banda): Boolean {
            return oldItem == newItem
        }
    }
}