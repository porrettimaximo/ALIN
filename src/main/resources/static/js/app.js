// Variables globales
let currentQuote = null
let currentFormData = null

// DOM Content Loaded
document.addEventListener("DOMContentLoaded", () => {
  console.log("DOM cargado, inicializando funciones...")
  initializeTabs()
  initializeQuoteForm()
  initializeTrackingForm()
  initializeVolumeCalculation()
  console.log("Funciones inicializadas correctamente")
})

// Funcionalidad de pestañas
function initializeTabs() {
  const tabButtons = document.querySelectorAll(".tab-button")
  const tabContents = document.querySelectorAll(".tab-content")

  tabButtons.forEach((button) => {
    button.addEventListener("click", () => {
      const tabId = button.getAttribute("data-tab")

      // Remove active class from all buttons and contents
      tabButtons.forEach((btn) => btn.classList.remove("active"))
      tabContents.forEach((content) => content.classList.remove("active"))

      // Add active class to clicked button and corresponding content
      button.classList.add("active")
      document.getElementById(tabId + "-tab").classList.add("active")
    })
  })
}

// Funcionalidad del formulario de cotización
function initializeQuoteForm() {
  console.log("Inicializando formulario de cotización...")
  const form = document.getElementById("quote-form")
  const calculateBtn = document.getElementById("calculate-btn")
  const toggleBreakdownBtn = document.getElementById("toggle-breakdown")
  const acceptQuoteBtn = document.getElementById("accept-quote")

  if (!form) {
    console.error("No se encontró el formulario quote-form")
    return
  }
  
  if (!calculateBtn) {
    console.error("No se encontró el botón calculate-btn")
    return
  }

  console.log("Formulario y botón encontrados, agregando event listeners...")
  form.addEventListener("submit", handleQuoteSubmission)
  toggleBreakdownBtn.addEventListener("click", toggleCostBreakdown)
  acceptQuoteBtn.addEventListener("click", acceptQuote)
  // Agregar autocompletado a origen y destino
  setupAutocomplete('origin');
  setupAutocomplete('destination');
  console.log("Event listeners agregados correctamente")
}

function setupAutocomplete(inputId) {
  const input = document.getElementById(inputId);
  if (!input) return;
  // Crear contenedor de sugerencias
  let suggestionBox = document.createElement('div');
  suggestionBox.className = 'autocomplete-suggestions';
  suggestionBox.style.position = 'absolute';
  suggestionBox.style.background = '#fff';
  suggestionBox.style.border = '1px solid #e0e7ef';
  suggestionBox.style.zIndex = '1000';
  suggestionBox.style.width = input.offsetWidth + 'px';
  suggestionBox.style.maxHeight = '200px';
  suggestionBox.style.overflowY = 'auto';
  suggestionBox.style.display = 'none';
  input.parentNode.appendChild(suggestionBox);

  input.addEventListener('input', async function() {
    const value = input.value.trim();
    if (value.length < 2) {
      suggestionBox.style.display = 'none';
      return;
    }
    try {
      const res = await fetch(`/api/places/autocomplete?input=${encodeURIComponent(value)}`);
      if (!res.ok) throw new Error('Error en autocompletado');
      const predictions = JSON.parse(await res.text());
      suggestionBox.innerHTML = '';
      if (predictions.length === 0) {
        suggestionBox.style.display = 'none';
        return;
      }
      predictions.forEach(pred => {
        const item = document.createElement('div');
        item.className = 'autocomplete-item';
        item.style.padding = '0.5em 1em';
        item.style.cursor = 'pointer';
        item.style.borderBottom = '1px solid #f0f0f0';
        item.textContent = pred.description;
        item.onclick = function() {
          input.value = pred.description;
          suggestionBox.style.display = 'none';
        };
        suggestionBox.appendChild(item);
      });
      suggestionBox.style.display = 'block';
    } catch (e) {
      suggestionBox.style.display = 'none';
    }
  });
  // Ocultar sugerencias al perder foco
  input.addEventListener('blur', function() {
    setTimeout(() => { suggestionBox.style.display = 'none'; }, 200);
  });
  // Mostrar sugerencias al enfocar si hay
  input.addEventListener('focus', function() {
    if (suggestionBox.innerHTML.trim() !== '') suggestionBox.style.display = 'block';
  });
}

// Cálculo de volumen
function initializeVolumeCalculation() {
  const heightInput = document.getElementById("height")
  const widthInput = document.getElementById("width")
  const lengthInput = document.getElementById("length")
  const volumeDisplay = document.getElementById("volume-display")
  const volumeValue = document.getElementById("volume-value")

  function calculateAndDisplayVolume() {
    const height = Number.parseFloat(heightInput.value) || 0
    const width = Number.parseFloat(widthInput.value) || 0
    const length = Number.parseFloat(lengthInput.value) || 0

    if (height > 0 && width > 0 && length > 0) {
      const volume = (height * width * length) / 1000000 // cm³ to m³
      volumeValue.textContent = volume.toFixed(3)
      volumeDisplay.style.display = "block"
    } else {
      volumeDisplay.style.display = "none"
    }
  }

  heightInput.addEventListener("input", calculateAndDisplayVolume)
  widthInput.addEventListener("input", calculateAndDisplayVolume)
  lengthInput.addEventListener("input", calculateAndDisplayVolume)
}

// Manejar el envío del formulario de cotización
async function handleQuoteSubmission(event) {
  event.preventDefault()

  const formData = new FormData(event.target)
  const quoteData = {
    origin: formData.get("origin"),
    destination: formData.get("destination"),
    weight: Number.parseFloat(formData.get("weight")),
    height: Number.parseFloat(formData.get("height")) || 0,
    width: Number.parseFloat(formData.get("width")) || 0,
    length: Number.parseFloat(formData.get("length")) || 0,
    urgency: formData.get("urgency") === "on",
    scheduledDate: formData.get("scheduledDate"),
    paymentMethod: formData.get("paymentMethod")
  }

  // Validate required fields
  if (!quoteData.origin || !quoteData.destination || !quoteData.weight) {
    alert("Por favor complete todos los campos obligatorios")
    return
  }

  // Incluir el campo pallets en los datos de la cotización
  const palletsInput = document.getElementById("pallets");
  quoteData.pallets = Number.parseInt(palletsInput.value) || 0;

  currentFormData = quoteData
  await calculateQuote(quoteData)
}

// Calcular cotización
async function calculateQuote(quoteData) {
  console.log("Iniciando cálculo de cotización:", quoteData)
  const calculateBtn = document.getElementById("calculate-btn")
  const btnText = calculateBtn.querySelector(".btn-text")
  const loadingSpinner = calculateBtn.querySelector(".loading-spinner")

  // Show loading state
  btnText.style.display = "none"
  loadingSpinner.style.display = "flex"
  calculateBtn.disabled = true

  try {
    console.log("Enviando request a /cotizaciones/calcular")
    const response = await fetch("/cotizaciones/calcular", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(quoteData),
    })

    console.log("Response status:", response.status)
    
    if (response.ok) {
      const cotizacion = await response.json()
      // Adaptar la respuesta para mostrar los datos relevantes
      let total = 0
      let costoDistancia = 0
      let costoPeso = 0
      let costoVolumen = 0
      cotizacion.tarifas.forEach(tarifa => {
        if (tarifa['@type'] === "TarifaPorKm" || tarifa.tipo_tarifa === "KM") costoDistancia = tarifa.valor
        if (tarifa['@type'] === "TarifaPorKg" || tarifa.tipo_tarifa === "KG") costoPeso = tarifa.valor
        if (tarifa['@type'] === "TarifaPorEspacio" || tarifa.tipo_tarifa === "ESPACIO") costoVolumen = tarifa.valor
        total += tarifa.valor
      })
      // Guardar todos los datos relevantes en localStorage
      const result = {
        ...quoteData,
        totalCost: total,
        distanceCost: costoDistancia,
        weightCost: costoPeso,
        volumeCost: costoVolumen,
        distancia: cotizacion.distancia, // Usar el campo correcto
        estimatedDays: cotizacion.estimatedDays,
        availableDrivers: cotizacion.availableDrivers,
        validUntil: cotizacion.validUntil,
        volume: cotizacion.volume,
        urgency: quoteData.urgency
      }
      localStorage.setItem('cotizacionActual', JSON.stringify(result));
      window.location.href = '/indexCotizacion.html';
      return;
    } else {
      const errorText = await response.text()
      console.error("Error response:", errorText)
      if (errorText.includes('No se encontró ruta entre los puntos')) {
        mostrarMensajeErrorRuta();
        return;
      }
      throw new Error("Error al calcular la cotización: " + response.status)
    }
  } catch (error) {
    console.error("Error:", error)
    alert("Error al calcular la cotización. Por favor intente nuevamente.")
  } finally {
    // Hide loading state
    btnText.style.display = "block"
    loadingSpinner.style.display = "none"
    calculateBtn.disabled = false
  }
}

function mostrarMensajeErrorRuta() {
  let errorDiv = document.getElementById('ruta-error-msg');
  if (!errorDiv) {
    errorDiv = document.createElement('div');
    errorDiv.id = 'ruta-error-msg';
    errorDiv.style.background = '#fee2e2';
    errorDiv.style.color = '#b91c1c';
    errorDiv.style.padding = '1em';
    errorDiv.style.margin = '1em 0';
    errorDiv.style.borderRadius = '0.7em';
    errorDiv.style.fontWeight = '600';
    errorDiv.style.textAlign = 'center';
    errorDiv.textContent = 'No se encontró ruta entre el origen y destino seleccionados. Por favor, revisa los datos o elige otra combinación.';
    const form = document.getElementById('quote-form');
    form.parentNode.insertBefore(errorDiv, form);
  } else {
    errorDiv.style.display = 'block';
  }
}

// Mostrar resultado de la cotización
function displayQuoteResult(result) {
  const quoteResultDiv = document.getElementById("quote-result")
  const totalPrice = document.getElementById("total-price")
  const validUntil = document.getElementById("valid-until")
  const estimatedDays = document.getElementById("estimated-days")
  const distance = document.getElementById("distance")
  const driversBadge = document.getElementById("drivers-badge")

  // Update main quote information
  totalPrice.textContent = `$${result.totalCost.toLocaleString("es-AR")}`
  validUntil.textContent = result.validUntil
  estimatedDays.textContent = result.estimatedDays
  distance.textContent = Math.round(result.baseDistance)
  driversBadge.textContent = `${result.availableDrivers} transportistas disponibles`

  // Update cost breakdown
  updateCostBreakdown(result)

  // Show the result card with animation
  quoteResultDiv.style.display = "block"
  quoteResultDiv.classList.add("animate-fadeIn")
}

// Actualizar desglose de costos
function updateCostBreakdown(result) {
  document.getElementById("distance-cost").textContent = `$${result.distanceCost.toLocaleString("es-AR")}`
  document.getElementById("weight-cost").textContent = `$${result.weightCost.toLocaleString("es-AR")}`
  document.getElementById("breakdown-total").textContent = `$${result.totalCost.toLocaleString("es-AR")}`

  // Volume cost (show only if > 0)
  const volumeCostItem = document.getElementById("volume-cost-item")
  if (result.volumeCost > 0) {
    document.getElementById("volume-cost").textContent = `$${result.volumeCost.toLocaleString("es-AR")}`
    volumeCostItem.style.display = "flex"
  } else {
    volumeCostItem.style.display = "none"
  }

  // Urgency cost (show only if > 0)
  const urgencyCostItem = document.getElementById("urgency-cost-item")
  if (result.urgencyCost > 0) {
    document.getElementById("urgency-cost").textContent = `$${result.urgencyCost.toLocaleString("es-AR")}`
    urgencyCostItem.style.display = "flex"
  } else {
    urgencyCostItem.style.display = "none"
  }
}

// Alternar visibilidad del desglose de costos
function toggleCostBreakdown() {
  const breakdown = document.getElementById("cost-breakdown")
  const toggleBtn = document.getElementById("toggle-breakdown")

  if (breakdown.style.display === "none" || breakdown.style.display === "") {
    breakdown.style.display = "block"
    toggleBtn.textContent = "Ocultar Desglose de Costos"
  } else {
    breakdown.style.display = "none"
    toggleBtn.textContent = "Ver Desglose de Costos"
  }
}

// Aceptar cotización
async function acceptQuote() {
  if (!currentFormData || !currentQuote) {
    alert("Error: No hay cotización disponible")
    return
  }

  // Mostrar modal de método de pago
  document.getElementById('payment-modal').style.display = 'flex';

  // Confirmar pago
  document.getElementById('confirm-payment-btn').onclick = async function() {
    const paymentMethod = document.getElementById('paymentMethodModal').value;
    document.getElementById('payment-modal').style.display = 'none';
    await processPayment(paymentMethod);
  };
  // Cancelar
  document.getElementById('cancel-payment-btn').onclick = function() {
    document.getElementById('payment-modal').style.display = 'none';
  };
}

async function processPayment(paymentMethod) {
  const acceptBtn = document.getElementById("accept-quote")
  acceptBtn.disabled = true
  acceptBtn.textContent = "Procesando..."

  try {
    // Obtener usuario logueado
    const user = JSON.parse(localStorage.getItem("user"))
    if (!user) {
      alert("Debe iniciar sesión para continuar")
      acceptBtn.disabled = false
      acceptBtn.textContent = "Aceptar y Reservar Viaje"
      return
    }

    const requestData = {
      ...currentFormData,
      totalCost: currentQuote.totalCost,
      clienteId: user.id,
      metodoPago: paymentMethod
    }

    const response = await fetch("/api/quote/accept", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    })

    if (response.ok) {
      const result = await response.json()
      
      if (result.success) {
        alert(`¡Cotización aceptada! Se le asignará un transportista en breve.\nCódigo de reserva: ${result.codigoSeguimiento}`)
        // Reset form
        document.getElementById("quote-form").reset()
        document.getElementById("quote-result").style.display = "none"
        document.getElementById("volume-display").style.display = "none"
        currentQuote = null
        currentFormData = null
        loadMisEnvios();
      } else {
        alert(`Error: ${result.message}`)
      }
    } else {
      throw new Error("Error al procesar la solicitud")
    }
  } catch (error) {
    console.error("Error:", error)
    alert("Error al procesar la solicitud. Por favor intente nuevamente.")
  } finally {
    acceptBtn.disabled = false
    acceptBtn.textContent = "Aceptar y Reservar Viaje"
  }
}

// Tracking form functionality
function initializeTrackingForm() {
  const form = document.getElementById("tracking-form")
  form.addEventListener("submit", handleTrackingSubmission)
}

// Handle tracking form submission
async function handleTrackingSubmission(event) {
  event.preventDefault()

  const trackingCodeInput = document.getElementById("tracking-code")
  const trackingCode = trackingCodeInput.value.trim()
  const resultDiv = document.getElementById("tracking-result")

  resultDiv.style.display = "none"
  resultDiv.textContent = ""

  if (!trackingCode) {
    alert("Por favor ingrese el código de reserva")
    return
  }

  try {
    const response = await fetch(`/api/tracking/${encodeURIComponent(trackingCode)}`)

    if (response.ok) {
      const data = await response.json()
      resultDiv.innerHTML = `
        <p><strong>Origen:</strong> ${data.origin}</p>
        <p><strong>Destino:</strong> ${data.destination}</p>
        <p><strong>Estado:</strong> ${data.status}</p>
      `
    } else if (response.status === 404) {
      resultDiv.textContent = "Envío no encontrado"
    } else {
      throw new Error("Error al obtener el seguimiento")
    }
  } catch (error) {
    console.error("Error:", error)
    resultDiv.textContent = "Ocurrió un error al consultar el seguimiento"
  } finally {
    resultDiv.style.display = "block"
  }
}

// Función placeholder para cargar envíos del cliente
function loadMisEnvios() {
  console.log("Función loadMisEnvios llamada")
  // Esta función se implementará cuando sea necesario
}
