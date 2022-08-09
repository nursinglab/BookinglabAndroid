package com.nursinglab.booking.component

class ResponseComponent(
        var error: Int,
        var status: String,
        var records: RecordsComponent,
        var result: List<ResultComponent>,
        var booking: List<BookingIdComponent>)