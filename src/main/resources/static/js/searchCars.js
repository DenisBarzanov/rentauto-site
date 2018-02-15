function searchCars(dateRangeId) {
    let dateRange = document.getElementById(dateRangeId);
    let dateStrings = dateRange.value.split(" - ");
    location.href = ("/car/available?startDate=" + dateStrings[0] + "&endDate=" + dateStrings[1]);
}
$('input[name="daterange"]').daterangepicker(
    {
        locale: {
            format: 'DD-MM-YYYY'
        },
        startDate: '01-02-2018',
        endDate: '05-02-2018'
    },
    function(start, end, label) {
        // alert("A new date range was chosen: " + start.format('DD-MM-YYYY') + ' to ' + end.format('DD-MM-YYYY'));
    });