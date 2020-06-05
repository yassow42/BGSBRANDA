package com.creativeoffice.bgsbranda.Datalar

class SiparisData {

    var siparis_notu: String? = null
    var siparis_turu: String? = null
    var siparis_teklif: Int? = null
    var siparis_key: String? = null
    var musteri_key: String? = null
    var siparis_giren: String? = null
    var siparis_girme_zamani: Long? = null
    var teklif_veren: String? = null
    var teklif_veren_zaman: Long? = null
    var uretime_gonderen: String? = null
    var uretime_gonderen_zaman: Long? = null
    var ureten: String? = null
    var ureten_zaman: Long? = null
    var montaj: String? = null
    var montaj_zaman: Long? = null


    constructor()
    constructor(
        siparis_notu: String?,
        siparis_turu: String?,
        siparis_teklif: Int?,
        siparis_key: String?,
        musteri_key: String?,
        siparis_giren: String?,
        siparis_girme_zamani: Long?,
        teklif_veren: String?,
        teklif_veren_zaman: Long?,
        uretime_gonderen: String?,
        uretime_gonderen_zaman: Long?,
        ureten: String?,
        ureten_zaman: Long?,
        montaj: String?,
        montaj_zaman: Long?
    ) {
        this.siparis_notu = siparis_notu
        this.siparis_turu = siparis_turu
        this.siparis_teklif = siparis_teklif
        this.siparis_key = siparis_key
        this.musteri_key = musteri_key
        this.siparis_giren = siparis_giren
        this.siparis_girme_zamani = siparis_girme_zamani
        this.teklif_veren = teklif_veren
        this.teklif_veren_zaman = teklif_veren_zaman
        this.uretime_gonderen = uretime_gonderen
        this.uretime_gonderen_zaman = uretime_gonderen_zaman
        this.ureten = ureten
        this.ureten_zaman = ureten_zaman
        this.montaj = montaj
        this.montaj_zaman = montaj_zaman
    }


    data class TenteData(
        var tente_cephe: String? = null,
        var tente_acilim: String? = null,
        var tente_kumaskodu: String? = null,
        var tente_sacak: String? = null,
        var tente_sacak_yazisi: String? = null,
        var sanzimanYonu: String? = null,
        var profilRengi: String? = null,
        var siparis_key: String? = null
    ) {

    }

    data class KorukluTenteData(
        var tente_cephe: String? = null,
        var tente_acilim: String? = null,
        var tente_kumaskodu: String? = null,
        var tente_sacak: String? = null,
        var tente_sacak_yazisi: String? = null,
        var ipYonu: String? = null,
        var profilRengi: String? = null,
        var siparis_key: String? = null
    ) {

    }
}