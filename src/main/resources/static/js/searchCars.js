function searchCars(startDateId, endDateId) {
    let startDate = document.getElementById(startDateId);
    let endDate = document.getElementById(endDateId);
    location.href = "/car/available?startDate=" + formatDate(new Date(startDate.value)) + "&endDate=" + formatDate(new Date(endDate.value));
}
function formatDate(date) {
    let day = date.getDate();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();

    return day + '-' + month + '-' + year;
}
$('input[name="startDate"], input[name="endDate"]').daterangepicker(
    {
        singleDatePicker: true,
        showDropdowns: true
    });