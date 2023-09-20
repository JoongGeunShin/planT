package com.example.plant.main_fragment.calendar.ui.calendar

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.Calendar

class TodayDecorator: DayViewDecorator {
    private var date = CalendarDay.today()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(StyleSpan(Typeface.BOLD))
        view?.addSpan(RelativeSizeSpan(1.4f))
        view?.addSpan(ForegroundColorSpan(Color.parseColor("#1D872A")))
    }
}

//주말강조및 도트 데코레이터
//class EventDecorator(dates: Collection<CalendarDay>): DayViewDecorator {
//
//    var dates: HashSet<CalendarDay> = HashSet(dates)
//
//    override fun shouldDecorate(day: CalendarDay?): Boolean {
//        return dates.contains(day)
//    }
//
//    override fun decorate(view: DayViewFacade?) {
//        view?.addSpan(DotSpan(5F, Color.parseColor("#1D872A")))
//    }
//}
class EventDecorator() : DayViewDecorator {

    private var color = 0
    private lateinit var dates: HashSet<CalendarDay>

    constructor(color: Int, dates: Collection<CalendarDay>) : this() {
        this.color = color
        this.dates = HashSet(dates)
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(10F, color))
    }
}

class SaturdayDecorator : DayViewDecorator {

    private val calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay): Boolean {
        day.copyTo(calendar)
        val weekDay: Int = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SATURDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#87CEFA")))
    }
}

class SundayDecorator : DayViewDecorator {
    private val calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay): Boolean {
        day.copyTo(calendar)
        val weekDay = calendar[Calendar.DAY_OF_WEEK]
        return weekDay == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#F08080")))
    }
}