
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import pm.login.R
import pm.login.models.Festival

class FestivalAdapter : ListAdapter<Festival, FestivalAdapter.FestivalViewHolder>(FestivalDiffCallback()) {
    // Opcional: sobrescreva submitList para garantir tratamento de lista vazia
    override fun submitList(list: List<Festival>?) {
        super.submitList(list ?: emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_festival, parent, false)
        return FestivalViewHolder(view)
    }

    override fun onBindViewHolder(holder: FestivalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FestivalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(concerto: Festival) {
            itemView.findViewById<TextView>(R.id.txtTitulo).text = concerto.nome ?: "Nome não disponível"
            itemView.findViewById<TextView>(R.id.txtPreco).text = (concerto.preco ?: "Preço não disponível").toString()
            itemView.findViewById<TextView>(R.id.txtDisponibilidade).text =
                "Disponíveis: ${concerto.preco ?: 0}"

            // Carregar imagem
            /*
            Glide.with(itemView.context)
                .load(livro.imagemUrl)
                .placeholder(R.drawable.placeholder_book)
                .error(R.drawable.placeholder_book)
                .into(itemView.findViewById(R.id.imgLivro))

             */
        }
    }

    class FestivalDiffCallback : DiffUtil.ItemCallback<Festival>() {
        override fun areItemsTheSame(oldItem: Festival, newItem: Festival): Boolean {
            return oldItem.id_festival== newItem.id_festival
        }

        override fun areContentsTheSame(oldItem: Festival, newItem: Festival): Boolean {
            return oldItem == newItem
        }
    }
}