package com.example.mycashbook.database

class CashflowModel(id:Int, tanggal:String, masukKeluar:String, nominal:Int, keterangan:String) {
    constructor() : this(0, "", "",0,"")

    private var id:Int
        get() {
            return id
        }
    var masukKeluar:String
    var tanggal:String
    var nominal:Int
    var keterangan:String

    init {
        this.id = id
        this.masukKeluar = masukKeluar
        this.tanggal = tanggal
        this.nominal = nominal
        this.keterangan = keterangan
    }



}