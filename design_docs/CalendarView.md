# MonthView
	- Should work off of an MonthAdapter (takes care of recycling views, binding data to views, etc)
	- Provide functionality for editing records associated with a given day
	- How to build? Need month changer at the top,
	- Fully localized (eventually)
	- How to handle the month adapter?
		- all loading of data should happen inside the onMonthChanged listener
		- all data will extend CalendarDay
			- method for checking if day is part of current month
			- method for checking if there is one or more records associated with that day
		- a new array of data will be set through setData(List<CalendarDay> dayList)
			- this will only include objects that have record
			- as part of that method, it will create a backing array that is getCount() long and fill the empty days
				with empty CalendarDay