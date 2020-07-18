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
    var foto1: String? = null
    var foto2: String? = null
    var foto3: String? = null
    var foto4: String? = null


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
        montaj_zaman: Long?,
        foto1: String?,
        foto2: String?,
        foto3: String?,
        foto4: String?
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
        this.foto1 = foto1
        this.foto2 = foto2
        this.foto3 = foto3
        this.foto4 = foto4
    }


    data class MafsallıTente(
        var cephe: String? = null,
        var acilim: String? = null,
        var kumaskodu: String? = null,
        var sacak_turu: String? = null,
        var sacak_yazisi: String? = null,
        var motor: String? = null,
        var sanzimanYonu: String? = null,
        var ayakTuru: String?=null,
        var mantolama: String?=null,
        var profilRengi: String? = null,
        var siparis_key: String? = null,
        var eksikler: String? = null
    ) {

    }

    data class KorukluTenteData(
        var cephe: String? = null,
        var acilim: String? = null,
        var kumaskodu: String? = null,
        var sacak_turu: String? = null,
        var sacak_biyesi_rengi:String?=null,
        var serit_rengi:String?=null,
        var serit_rengi_adeti:String?=null,
        var tente_sacak_yazisi: String? = null,
        var ipYonu: String? = null,
        var profilRengi: String? = null,
        var ayakTuru:String?=null,
        var siparis_key: String? = null,
        var eksikler: String? = null

    ) {

        
    }

    data class PergoleData(
        var pergole_turu: String? = null,
        var cephe: String? = null,
        var acilim: String? = null,
        var arka_yukseklik: String? = null,
        var on_yukseklik: String? = null,
        var kumas_rengi: String? = null,
        var profil_rengi: String? = null,
        var led: String? = null,
        var motor_yonu: String? = null,
        var korner_direk_olcusu: String? = null,
        var korner_direk_adeti: String? = null,
        var cam_kaydi_olcusu: String? = null,
        var cam_kaydi_adeti: String? = null,
        var pergole_cesidi: String? = null,
        var etrafinda_cam_varmi: String? = null,
        var siparis_key: String? = null,
        var eksikler: String? = null

    ) {

    }




    data class SemsiyeData (
        var semsiye_turu: String? = null,
        var genislik: String? = null,
        var kumas_rengi: String? = null,
        var sacak_yazisi: String? = null,
        var siparis_key: String? = null,
        var eksikler: String? = null

    ) {


    }


    data class KarpuzData (

        var genislik: String? = null,
        var yukseklik: String? = null,
        var kumas_rengi: String? = null,
        var sacak_turu: String? = null,
        var sacak_yazisi: String? = null,
        var biye_rengi: String? = null,
        var serit_rengi: String? = null,
        var siparis_key: String? = null,
        var eksikler: String? = null

    ) {


    }

    data class SeffafData (

        var seffaf_mika_eni: String? = null,
        var pvc_rengi: String? = null,
        var alt_pvc: String? = null,
        var ust_pvc: String? = null,
        var fermuar: String? = null,
        var boru_yeri: String? = null,
        var ekstra_sacak: String? = null,
        var siparis_key: String? = null,
        var eksikler: String? = null



    ) {


    }



    data class Wintend(
        var cephe: String? = null,
        var kolboyu: String? = null,
        var kumaskodu: String? = null,
        var sacak_turu: String? = null,
        var sacak_yazisi: String? = null,
        var motor: String? = null,
        var sanzimanYonu: String? = null,
        var ayakTuru: String?=null,
        var mantolama: String?=null,
        var profilRengi: String? = null,
        var siparis_key: String? = null,
        var eksikler: String? = null

    ) {

    }
    data class Diger(
        var olculer :String?=null,
        var siparis_key: String? = null,
        var eksikler: String? = null

    ) {

    }


}