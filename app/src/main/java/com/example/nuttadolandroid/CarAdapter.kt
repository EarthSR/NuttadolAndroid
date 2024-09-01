import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.nuttadolandroid.Car
import com.example.nuttadolandroid.databinding.ItemCarBinding
import com.example.nuttadolandroid.R

// Extension function to generate image URL
fun String?.toImageUrl(): String? {
    return this?.takeIf { !it.contains("*") }?.let { "http://192.168.1.101:3000/uploads/$it" }
}

// Adapter for displaying cars
class CarAdapter(private var cars: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(private val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(car: Car) {
            binding.textBrand.text = "Brand: ${car.Brand}"
            binding.textModel.text = "Model: ${car.Model}"
            binding.textYear.text = "Year: ${car.Year}"
            binding.textColor.text = "Color: ${car.Color}"
            binding.textPrice.text = "Price: ${car.Price}"
            binding.textTransmissionType.text = "Transmission Type: ${car.TransmissionType}"
            binding.textFuelType.text = "Fuel Type: ${car.FuelType}"
            binding.textNumberOfDoors.text = "Number of Doors: ${car.NumberOfDoors}"
            binding.textNumberOfSeats.text = "Number of Seats: ${car.NumberOfSeats}"

            val imageUrl = car.CarImage.toImageUrl()
            Log.d("CarAdapter", "Loading image from URL: $imageUrl")

            Glide.with(binding.carImage.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.pexels) // Ensure this image exists
                .error(R.drawable.ic_launcher_background) // Ensure this image exists
                .into(binding.carImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount() = cars.size

    fun updateCars(newCars: List<Car>) {
        val diffCallback = CarDiffCallback(cars, newCars)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        cars = newCars
        diffResult.dispatchUpdatesTo(this)
    }
}

// DiffUtil callback for calculating differences
class CarDiffCallback(
    private val oldList: List<Car>,
    private val newList: List<Car>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
