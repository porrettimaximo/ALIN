function initAutocomplete() {
    const origenInput = document.getElementById('origin');
    const destinoInput = document.getElementById('destination');

    // Crear autocompletado para los campos
    const autocompleteOrigen = new google.maps.places.Autocomplete(origenInput);
    const autocompleteDestino = new google.maps.places.Autocomplete(destinoInput);

    // Opcional: Restringir las búsquedas a un país específico
    autocompleteOrigen.setComponentRestrictions({ country: ['ar'] }); // Cambia 'ar' por el código de tu país
    autocompleteDestino.setComponentRestrictions({ country: ['ar'] });

    // Opcional: Escuchar eventos de selección
    autocompleteOrigen.addListener('place_changed', () => {
        const place = autocompleteOrigen.getPlace();
        console.log('Origen seleccionado:', place);
    });

    autocompleteDestino.addListener('place_changed', () => {
        const place = autocompleteDestino.getPlace();
        console.log('Destino seleccionado:', place);
    });
}

// Inicializar el autocompletado cuando la página cargue
window.onload = initAutocomplete;
