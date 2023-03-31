package ua.sviatkuzbyt.newsnow.ui.elements

sealed class ProgressBarMode {
    object Nothing: ProgressBarMode()
    object LoadNew: ProgressBarMode()
    object LoadMore: ProgressBarMode()
}