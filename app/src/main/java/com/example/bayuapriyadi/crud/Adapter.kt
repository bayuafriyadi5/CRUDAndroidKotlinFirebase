package com.example.bayuapriyadi.crud

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

@Suppress("UNREACHABLE_CODE", "NAME_SHADOWING")
class Adapter(val mCtx: Context, val layoutResId: Int, val list: List<Users> )
    : ArrayAdapter<Users>(mCtx,layoutResId,list){

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val textNama = view.findViewById<TextView>(R.id.textNama)
        val textStatus = view.findViewById<TextView>(R.id. textStatus)

        val textUpdate = view.findViewById<TextView>(R.id.TextUpdate)
        val textDelete = view.findViewById<TextView>(R.id.TextDelete)

        val user = list[position]

        textNama.text = user.nama
        textStatus.text = user.status

        textUpdate.setOnClickListener {
            showUpdateDialog(user)
        }
        textDelete.setOnClickListener {
            Deleteinfo(user)
        }

        return view
    }

    private fun Deleteinfo(user: Users) {
        val progressDialog = ProgressDialog(context,
            R.style.Theme_MaterialComponents_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Deleting...")
        progressDialog.show()
        val mydatabase = FirebaseDatabase.getInstance().getReference("USERS")
        mydatabase.child(user.id).removeValue()
        Toast.makeText(mCtx,"Deleted!!",Toast.LENGTH_SHORT).show()
        val intent = Intent(context, show::class.java)
        context.startActivity(intent)
    }

        private fun showUpdateDialog(user: Users) {
            val builder = AlertDialog.Builder(mCtx)

            builder.setTitle("Update")

            val inflater = LayoutInflater.from(mCtx)

            val view = inflater.inflate(R.layout.update, null)

            val textNama = view.findViewById<EditText>(R.id.inputNama)
            val textStatus = view.findViewById<EditText>(R.id.inputStatus)

            textNama.setText(user.nama)
            textStatus.setText(user.status)

            builder.setView(view)

            builder.setPositiveButton("Update") { dialog, which ->

                val dbUsers = FirebaseDatabase.getInstance().getReference("USERS")

                val nama = textNama.text.toString().trim()

                val status = textStatus.text.toString().trim()

                if (nama.isEmpty()){
                    textNama.error = "please enter name"
                    textNama.requestFocus()
                    return@setPositiveButton
                }

                if (status.isEmpty()){
                    textStatus.error = "please enter status"
                    textStatus.requestFocus()
                    return@setPositiveButton
                }

                val user = Users(user.id,nama,status)

                dbUsers.child(user.id).setValue(user).addOnCompleteListener {
                    Toast.makeText(mCtx,"Updated",Toast.LENGTH_SHORT).show()
                }

            }

            builder.setNegativeButton("No") { dialog, which ->

            }

            val alert = builder.create()
            alert.show()

        }

}