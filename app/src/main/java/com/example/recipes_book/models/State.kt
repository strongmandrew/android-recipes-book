package com.example.recipes_book.models

data class State<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): State<T> {
            return State(Status.SUCCESS, data, null)
        }

        fun <T> empty(): State<T> {
            return State(Status.EMPTY, null, null)
        }

        fun <T> error(message: String?): State<T> {
            return State(Status.ERROR, null, message)
        }

        fun <T> loading(): State<T> {
            return State(Status.LOADING, null, null)
        }

    }



}
