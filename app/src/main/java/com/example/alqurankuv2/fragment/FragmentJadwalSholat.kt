package com.example.alqurankuv2.fragment

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.example.alqurankuv2.R
import com.example.alqurankuv2.model.main.DaftarKota
import com.example.alqurankuv2.utils.ClientAsyncTask
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vivekkaushik.datepicker.DatePickerTimeline
import kotlinx.android.synthetic.main.fragment_jadwal_sholat.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

class FragmentJadwalSholat : BottomSheetDialogFragment() {

    lateinit var strArg: String
    lateinit var listDaftarKota: MutableList<DaftarKota>
    lateinit var daftarKotaAdapter: ArrayAdapter<DaftarKota>
    lateinit var progressDialog: ProgressDialog

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireView().parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    companion object {
        @JvmStatic
        fun newInstance(string: String?): FragmentJadwalSholat {
            val fragment = FragmentJadwalSholat()
            val args = Bundle()
            args.putString("Jadwal Sholat", string)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        strArg = requireArguments().getString("Jadwal Sholat").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_jadwal_sholat, container, false)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Mohon Tunggu")
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Sedang menampilkan jadwal...")

        //show data spinner
        val spKota: Spinner = rootView.findViewById(R.id.spinKota)
        listDaftarKota = ArrayList()
        daftarKotaAdapter = ArrayAdapter(
            requireActivity().getApplicationContext(),
            android.R.layout.simple_spinner_item,
            listDaftarKota as ArrayList<DaftarKota>
        )
        daftarKotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spKota.adapter = daftarKotaAdapter
        spKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>) {}
            override fun onItemSelected(p0: AdapterView<*>, view: View?, position: Int, id: Long) {
                val spinKota = daftarKotaAdapter.getItem(position)
                getDataJadwal(spinKota?.id)
            }
        }

        //show date time
        val datePickerTimeline: DatePickerTimeline = rootView.findViewById(R.id.dateTimeline)
        val date = Calendar.getInstance()
        val mYear: Int = date.get(Calendar.YEAR)
        val mMonth: Int = date.get(Calendar.MONTH)
        val mDay: Int = date.get(Calendar.DAY_OF_MONTH)

        datePickerTimeline.setInitialDate(mYear, mMonth, mDay)
        datePickerTimeline.setDisabledDateColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorAccent
            )
        )
        datePickerTimeline.setActiveDate(date)

        val dates = arrayOf(Calendar.getInstance().time)
        datePickerTimeline.deactivateDates(dates)

        //get data kota
        getDataKota()

        return rootView
    }

    private fun getDataJadwal(id: Int?) {
        try {
            progressDialog.show()
            val idKota = id.toString()
            val current = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//            val tanggal = current.format(Date())
//            val tahun = current.format(Year())
//            val bulan = current.format(Month())
//            val url = "https://api.banghasan.com/sholat/format/json/jadwal/kota/$idKota/tanggal/$tanggal"
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(current) ?: Date()

            val tahun = calendar.get(Calendar.YEAR)
            val bulan = calendar.get(Calendar.MONTH) + 1
            val tanggal = calendar.get(Calendar.DAY_OF_MONTH)

            val url = "https://api.myquran.com/v2/sholat/jadwal/$idKota/$tahun/$bulan/$tanggal"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {
                    try {
                        progressDialog.dismiss()
                        val jsonObject = JSONObject(result)
                        val strJadwal = jsonObject.getJSONObject("data")
                        val strData = strJadwal.getJSONObject("jadwal")

                        tvShubuh.text = strData.getString("subuh")
                        tvDzuhur.text = strData.getString("dzuhur")
                        tvAshar.text = strData.getString("ashar")
                        tvMaghrib.text = strData.getString("maghrib")
                        tvIsya.text = strData.getString("isya")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
            task.execute(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDataKota() {
        try {
            progressDialog.show()
//            val url = "https://api.banghasan.com/sholat/format/json/kota"
            val url = "https://api.myquran.com/v2/sholat/kota/semua"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {
                    try {
                        progressDialog.dismiss()
                        val jsonObject = JSONObject(result)
                        val jsonArray = jsonObject.getJSONArray("data")
                        var daftarKota: DaftarKota?
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            daftarKota = DaftarKota()
                            daftarKota.id = obj.getInt("id")
                            daftarKota.lokasi= obj.getString("lokasi")
                            listDaftarKota.add(daftarKota)
                        }
                        daftarKotaAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
            task.execute(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}