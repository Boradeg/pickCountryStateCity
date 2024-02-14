// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package 

data class Country (
    val msg: String,
    val data: List<Datum>,
    val error: Boolean
)

data class Datum (
    val country: String,
    val cities: List<String>,
    val iso2: String,
    val iso3: String
)
