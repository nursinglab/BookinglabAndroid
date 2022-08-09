package com.nursinglab.booking.component

import android.os.Parcel
import android.os.Parcelable

class BookingIdComponent(
        var nama_dosen: String? = null,
        var nama: String? = null,
        var id: String? = null): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama_dosen)
        parcel.writeString(nama)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookingIdComponent> {
        override fun createFromParcel(parcel: Parcel): BookingIdComponent {
            return BookingIdComponent(parcel)
        }

        override fun newArray(size: Int): Array<BookingIdComponent?> {
            return arrayOfNulls(size)
        }
    }

}