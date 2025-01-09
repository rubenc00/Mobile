import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pm.login.R
import pm.login.models.Concerto

class ConcertoAdapter : ListAdapter<Concerto, ConcertoAdapter.ConcertoViewHolder>(ConcertoDiffCallback()) {
    // Opcional: sobrescreva submitList para garantir tratamento de lista vazia
    override fun submitList(list: List<Concerto>?) {
        super.submitList(list ?: emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_festival, parent, false)
        return ConcertoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConcertoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ConcertoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(concerto: Concerto) {
            itemView.findViewById<TextView>(R.id.txtTitulo).text = concerto.nome ?: "Nome não disponível"
            itemView.findViewById<TextView>(R.id.txtPreco).text = (concerto.preco ?: "Preço não disponível").toString()
            itemView.findViewById<TextView>(R.id.txtDisponibilidade).text =
                "Disponíveis: ${concerto.preco ?: 0}"


        }
    }

    class ConcertoDiffCallback : DiffUtil.ItemCallback<Concerto>() {
        override fun areItemsTheSame(oldItem: Concerto, newItem: Concerto): Boolean {
            return oldItem.id_concerto== newItem.id_concerto
        }

        override fun areContentsTheSame(oldItem: Concerto, newItem: Concerto): Boolean {
            return oldItem == newItem
        }
    }
}