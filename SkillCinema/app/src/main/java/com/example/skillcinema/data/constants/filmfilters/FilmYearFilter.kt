package com.example.skillcinema.data.constants.filmfilters

import java.time.Year

enum class FilmYearFilter (val filter: Int) {

    YEAR_FROM(1998),
    YEAR_TO(Year.now().value)

}