package com.example.logifitappp.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.ui.components.BottomSheetSelectableItem
import java.util.Locale

data class CountryPhoneCodeSelectableItem(
    override val id: Int,
    val self: CountryPhoneCodeEnum
): BottomSheetSelectableItem(id) {
    override fun toString() = App.context.getString(self.label)
}

enum class CountryPhoneCodeEnum(
    val code: String,
    @DrawableRes val icon: Int,
    val identifiers: List<String>,
    @StringRes val label: Int
) {
    ARGENTINA("+54", R.drawable.ic_argentina, listOf("AR", "ARG"), R.string.argentina),
    BOLIVIA("+591", R.drawable.ic_bolivia, listOf("BO", "BOL"), R.string.bolivia),
    BRAZIL("+55", R.drawable.ic_brazil, listOf("BR", "BRA"), R.string.brazil),
    CANADA("+1", R.drawable.ic_canada,listOf("CA", "CAN"), R.string.canada),
    CHILE("+56", R.drawable.ic_chile, listOf("CL", "CHL"), R.string.chile),
    COLOMBIA("+57", R.drawable.ic_colombia, listOf("CO", "COL"), R.string.colombia),
    COSTA_RICA("+506", R.drawable.ic_costa_rica, listOf("CR", "CRI"), R.string.costa_rica),
    CUBA("+53", R.drawable.ic_cuba, listOf("CU", "CUB"), R.string.cuba),
    DOMINICAN_REPUBLIC("+1 809", R.drawable.ic_dominican_republic, listOf("DO", "DOM"), R.string.dominican_republic),
    ECUADOR("+593", R.drawable.ic_ecuador,listOf("EC", "ECU"), R.string.ecuador),
    EL_SALVADOR("+503", R.drawable.ic_el_salvador,listOf("SV", "SLV"), R.string.el_salvador),
    GUATEMALA("+502", R.drawable.ic_guatemala, listOf("GT", "GTM"), R.string.guatemala),
    HONDURAS("+504", R.drawable.ic_honduras,listOf("HN", "HND"), R.string.honduras),
    MEXICO("+52", R.drawable.ic_mexico, listOf("MX", "MEX"), R.string.mexico),
    NICARAGUA("+505", R.drawable.ic_nicaragua, listOf("NI", "NIC"), R.string.nicaragua),
    PANAMA("+507", R.drawable.ic_panama, listOf("PA", "PAN"), R.string.panama),
    PARAGUAY("+595", R.drawable.ic_paraguay, listOf("PY", "PRY"), R.string.paraguay),
    PERU("+51", R.drawable.ic_peru, listOf("PE", "PER"), R.string.peru),
    PUERTO_RICO("+1 787", R.drawable.ic_puerto_rico, listOf("PR", "PRI"), R.string.puerto_rico),
    UNITED_STATES("+1", R.drawable.ic_united_states, listOf("US", "USA"), R.string.united_states),
    URUGUAY("+598", R.drawable.ic_uruguay, listOf("UY", "URY"), R.string.uruguay),
    VENEZUELA("+58", R.drawable.ic_venezuela, listOf("VE", "VEN"), R.string.venezuela);

    companion object {
        fun findByLocaleSystem() = fromIdentifier(Locale.getDefault().getISO3Country())

        fun findByPhone(phone: String): CountryPhoneCodeEnum? {
            entries.forEach {
                if (phone.startsWith(it.code)) return it
            }

            return null
        }

        private fun fromIdentifier(identifier: String): CountryPhoneCodeEnum? {
            entries.forEach {
                if (it.identifiers.contains(identifier)) return it
            }

            return null
        }

        fun toBottomSheetSelectableItems() = entries.map {
            CountryPhoneCodeSelectableItem(
                id = it.ordinal,
                self = it
            )
        }
    }
}