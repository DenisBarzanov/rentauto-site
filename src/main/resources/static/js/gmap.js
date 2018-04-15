function initMap() {
    let uluru = {lat: 42.734718, lng: 23.293138};
    let map = new google.maps.Map(document.getElementById('map'), {
        zoom: 16,
        center: uluru
    });
    let marker = new google.maps.Marker({
        position: uluru,
        map: map
    });
}