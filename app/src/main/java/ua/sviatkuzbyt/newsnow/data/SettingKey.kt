package ua.sviatkuzbyt.newsnow.data

data class SettingKey(
    val key: String,
    var value: String
)

data class SettingValues(
    val value: String,
    val valueShort: String
)