
# 🚚 ALIN – Plataforma de Cotización y Seguimiento Logístico (DEMO)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Demo](https://img.shields.io/badge/STATUS-DEMO-blue?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-green?style=for-the-badge)

> 🧩 **Versión demo** del proyecto **ALIN**, creada como ejercicio académico y demostración de desarrollo web full-stack.  
> Esta demo ilustra el flujo completo de cotización, cálculo de ruta y seguimiento logístico.

---

## 🧭 Descripción general

**ALIN** (*Automated Logistics & INformation*) es una plataforma web que permite **simular cotizaciones y reservas de transporte de carga** entre distintas ciudades.  
Calcula automáticamente el **precio, distancia y ruta** en tiempo real mediante la API de **Google Maps**, con una interfaz moderna, oscura y responsiva.

> 🔍 Este proyecto fue desarrollado con fines de aprendizaje y presentación (no productivo).

---

## 💡 Objetivos del proyecto

- Permitir la **cotización inmediata** de envíos según peso, volumen y destino.  
- Calcular rutas, costos y tiempos estimados de entrega.  
- Visualizar **mapas dinámicos** con trazado del recorrido.  
- Simular el **seguimiento en tiempo real** del envío (en tránsito, entregado, pendiente).  

---

## ⚙️ Tecnologías utilizadas

| Capa | Tecnologías |
|------|--------------|
| **Frontend** | HTML5 · CSS3 · JavaScript · Bootstrap |
| **Backend** | Java · Spring Boot |
| **API externa** | Google Maps Platform (Geocoding / Directions) |
| **Build tool** | Maven |
| **Servidor local** | `localhost:8080` |

---

## 🚀 Ejecución local

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/porrettimaximo/ALIN.git
   cd ALIN
   git checkout 19-07
````

2. **Compilar y ejecutar:**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Abrir el navegador en:**

   ```
   http://localhost:8080
   ```

4. **Probar las funciones principales:**

   * Nueva cotización.
   * Cálculo de precio y distancia.
   * Visualización del mapa con la ruta generada.
   * Seguimiento del envío con información de contacto simulada.

---

## ✨ Funcionalidades destacadas

✅ Cotización instantánea (peso, pallets, volumen, servicio urgente).
✅ Ruta y distancia calculadas en tiempo real.
✅ Seguimiento de envíos con estado dinámico.
✅ Interfaz moderna en modo oscuro.
✅ Precios claros y reserva inmediata.

---

## 🧾 Vistas del sistema

### 🟩 Solicitud de cotización

![Solicitud de cotización](./docs/Imagen1.jpg)

---

### 🟦 Cotización generada y ruta estimada

![Cotización generada](./docs/Imagen2.jpg)

---

### 🟧 Seguimiento del envío

![Seguimiento del envío](./docs/Imagen3.jpg)

---

## ⚠️ Limitaciones de la versión demo

* No incluye autenticación real ni base de datos persistente.
* Las tarifas y datos son **ficticios**.
* API de Google Maps utilizada con fines **educativos**.
* No gestiona transacciones reales ni reservas efectivas.

---

## 🧑‍💻 Autor

**Máximo Porretti**
Estudiante de Licenciatura en Sistemas – UNRN
Desarrollador Full-Stack Jr.
🔗 [GitHub: porrettimaximo](https://github.com/porrettimaximo)

---

## 📜 Licencia

Este proyecto se distribuye bajo licencia **MIT**.
Podés usarlo libremente con fines educativos o de portfolio.
No recomendado para entornos productivos sin revisión técnica.

---

> 💬 *ALIN – Simplificando la logística, un kilómetro a la vez.*

```

---

📌 **Instrucciones finales para tus imágenes:**

Creá una carpeta en tu repo llamada `/docs` y poné ahí las imágenes que subiste, renombradas así:

```

docs/
├─ Imagen1.jpg   # Cotización inicial
├─ Imagen2.jpg   # Resultado de cotización
└─ Imagen3.jpg   # Seguimiento del envío

```

¿Querés que te haga también una **versión resumida tipo portfolio** (para poner en tu GitHub o CV, más corta y orientada a mostrar tus skills)?
```
