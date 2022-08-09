package com.nursinglab.booking.component

import android.os.Parcel
import android.os.Parcelable

class ResultComponent(
        var id: String?,
        var action: String?,
        var waktu_mulai: String?,
        var waktu_selesai: String?,
        var tanggal: String?,
        var nama_dosen: String?,
        var nama_lab: String?,
        var nama_praktikum: String?,
        var nim_mahasiswa: String?,
        var kelas: String?): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(action)
        parcel.writeString(waktu_mulai)
        parcel.writeString(waktu_selesai)
        parcel.writeString(tanggal)
        parcel.writeString(nama_dosen)
        parcel.writeString(nama_lab)
        parcel.writeString(nama_praktikum)
        parcel.writeString(nim_mahasiswa)
        parcel.writeString(kelas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultComponent> {
        override fun createFromParcel(parcel: Parcel): ResultComponent {
            return ResultComponent(parcel)
        }

        override fun newArray(size: Int): Array<ResultComponent?> {
            return arrayOfNulls(size)
        }
    }
}